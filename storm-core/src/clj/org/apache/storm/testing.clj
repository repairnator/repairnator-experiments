;; Licensed to the Apache Software Foundation (ASF) under one
;; or more contributor license agreements.  See the NOTICE file
;; distributed with this work for additional information
;; regarding copyright ownership.  The ASF licenses this file
;; to you under the Apache License, Version 2.0 (the
;; "License"); you may not use this file except in compliance
;; with the License.  You may obtain a copy of the License at
;;
;; http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns org.apache.storm.testing
  (:require [org.apache.storm.daemon
             [nimbus :as nimbus]
             [local-supervisor :as local-supervisor]
             [common :as common]
             [worker :as worker]
             [executor :as executor]])
  (:import [org.apache.commons.io FileUtils]
           [org.apache.storm ProcessSimulator]
           [org.apache.storm.daemon.supervisor Supervisor StandaloneSupervisor SupervisorUtils ReadClusterState])
  (:import [java.io File])
  (:import [java.util HashMap ArrayList])
  (:import [java.util.concurrent.atomic AtomicInteger])
  (:import [java.util.concurrent ConcurrentHashMap])
  (:import [org.apache.storm.utils Time Utils RegisteredGlobalState])
  (:import [org.apache.storm.tuple Fields Tuple TupleImpl])
  (:import [org.apache.storm.task TopologyContext])
  (:import [org.apache.storm.generated GlobalStreamId Bolt KillOptions])
  (:import [org.apache.storm.messaging IContext])
  (:import [org.apache.storm.testing FeederSpout FixedTupleSpout FixedTuple
            TupleCaptureBolt SpoutTracker BoltTracker NonRichBoltTracker
            TestWordSpout MemoryTransactionalSpout])
  (:import [org.apache.storm.security.auth ThriftServer ThriftConnectionType ReqContext AuthUtils])
  (:import [org.apache.storm.generated NotAliveException AlreadyAliveException StormTopology ErrorInfo
            ExecutorInfo InvalidTopologyException Nimbus$Iface Nimbus$Processor SubmitOptions TopologyInitialStatus
            KillOptions RebalanceOptions ClusterSummary SupervisorSummary TopologySummary TopologyInfo
            ExecutorSummary AuthorizationException GetInfoOptions NumErrorsChoice])
  (:import [org.apache.storm.transactional TransactionalSpoutCoordinator])
  (:import [org.apache.storm.transactional.partitioned PartitionedTransactionalSpoutExecutor])
  (:import [org.apache.storm.tuple Tuple])
  (:import [org.apache.storm.generated StormTopology])
  (:import [org.apache.storm.task TopologyContext])
  (:require [org.apache.storm [zookeeper :as zk]])
  (:require [org.apache.storm.messaging.loader :as msg-loader])
  (:require [org.apache.storm.daemon.acker :as acker])
  (:use [org.apache.storm cluster util thrift config log local-state]))

(defn feeder-spout
  [fields]
  (FeederSpout. (Fields. fields)))

(defn local-temp-path
  []
  (str (System/getProperty "java.io.tmpdir") (if-not on-windows? "/") (uuid)))

(defn delete-all
  [paths]
  (dorun
    (for [t paths]
      (if (.exists (File. t))
        (try
          (FileUtils/forceDelete (File. t))
          (catch Exception e
            (log-message (.getMessage e))))))))

(defmacro with-local-tmp
  [[& tmp-syms] & body]
  (let [tmp-paths (mapcat (fn [t] [t `(local-temp-path)]) tmp-syms)]
    `(let [~@tmp-paths]
       (try
         ~@body
         (finally
           (delete-all ~(vec tmp-syms)))))))

(defn start-simulating-time!
  []
  (Time/startSimulating))

(defn stop-simulating-time!
  []
  (Time/stopSimulating))

 (defmacro with-simulated-time
   [& body]
   `(try
     (start-simulating-time!)
     ~@body
     (finally
       (stop-simulating-time!))))

(defn advance-time-ms! [ms]
  (Time/advanceTime ms))

(defn advance-time-secs! [secs]
  (advance-time-ms! (* (long secs) 1000)))

(defnk add-supervisor
  [cluster-map :ports 2 :conf {} :id nil]
  (let [tmp-dir (local-temp-path)
        port-ids (if (sequential? ports)
                   ports
                   (doall (repeatedly ports (:port-counter cluster-map))))
        supervisor-conf (merge (:daemon-conf cluster-map)
                               conf
                               {STORM-LOCAL-DIR tmp-dir
                                SUPERVISOR-SLOTS-PORTS port-ids})
        id (or id (Utils/uuid))
        isupervisor (proxy [StandaloneSupervisor] []
                        (generateSupervisorId [] id))
        daemon (local-supervisor/mk-local-supervisor supervisor-conf (:shared-context cluster-map) isupervisor)]
    (swap! (:supervisors cluster-map) conj daemon)
    (swap! (:tmp-dirs cluster-map) conj tmp-dir)
    daemon))

(defn mk-shared-context [conf]
  (if-not (conf STORM-LOCAL-MODE-ZMQ)
    (msg-loader/mk-local-context)))

(defn start-nimbus-daemon [conf nimbus]
  (let [server (ThriftServer. conf (Nimbus$Processor. nimbus)
                              ThriftConnectionType/NIMBUS)
        nimbus-thread (Thread. (fn [] (.serve server)))]
    (log-message "Starting Nimbus server...")
    (.start nimbus-thread)
    server))


;; returns map containing cluster info
;; local dir is always overridden in maps
;; can customize the supervisors (except for ports) by passing in map for :supervisors parameter
;; if need to customize amt of ports more, can use add-supervisor calls afterwards
(defnk mk-local-storm-cluster [:supervisors 2 :ports-per-supervisor 3 :daemon-conf {} :inimbus nil :supervisor-slot-port-min 1024 :nimbus-daemon false]
  (let [zk-tmp (local-temp-path)
        [zk-port zk-handle] (if-not (contains? daemon-conf STORM-ZOOKEEPER-SERVERS)
                              (zk/mk-inprocess-zookeeper zk-tmp))
        nimbus-tmp (local-temp-path)
        daemon-conf (merge (read-storm-config)
                           {TOPOLOGY-SKIP-MISSING-KRYO-REGISTRATIONS true
                            ZMQ-LINGER-MILLIS 0
                            TOPOLOGY-ENABLE-MESSAGE-TIMEOUTS false
                            TOPOLOGY-TRIDENT-BATCH-EMIT-INTERVAL-MILLIS 50
                            STORM-CLUSTER-MODE "local"
                            BLOBSTORE-SUPERUSER (System/getProperty "user.name")
                            BLOBSTORE-DIR nimbus-tmp}
                           (if-not (contains? daemon-conf STORM-ZOOKEEPER-SERVERS)
                             {STORM-ZOOKEEPER-PORT zk-port
                              STORM-ZOOKEEPER-SERVERS ["localhost"]})
                           daemon-conf)
        port-counter (mk-counter supervisor-slot-port-min)
        nimbus (nimbus/service-handler
                (assoc daemon-conf STORM-LOCAL-DIR nimbus-tmp)
                (if inimbus inimbus (nimbus/standalone-nimbus)))
        context (mk-shared-context daemon-conf)
        nimbus-thrift-server (if nimbus-daemon (start-nimbus-daemon daemon-conf nimbus) nil)
        cluster-map {:nimbus nimbus
                     :port-counter port-counter
                     :daemon-conf daemon-conf
                     :supervisors (atom [])
                     :state (mk-distributed-cluster-state daemon-conf)
                     :storm-cluster-state (mk-storm-cluster-state daemon-conf)
                     :tmp-dirs (atom [nimbus-tmp zk-tmp])
                     :zookeeper (if (not-nil? zk-handle) zk-handle)
                     :shared-context context
                     :nimbus-thrift-server nimbus-thrift-server}
        supervisor-confs (if (sequential? supervisors)
                           supervisors
                           (repeat supervisors {}))]

    (doseq [sc supervisor-confs]
      (add-supervisor cluster-map :ports ports-per-supervisor :conf sc))
    cluster-map))

(defn get-supervisor [cluster-map supervisor-id]
  (let [finder-fn #(= (.getId %) supervisor-id)]
    (find-first finder-fn @(:supervisors cluster-map))))

(defn kill-supervisor [cluster-map supervisor-id]
  (let [finder-fn #(= (.getId %) supervisor-id)
        supervisors @(:supervisors cluster-map)
        sup (find-first finder-fn
                        supervisors)]
    ;; tmp-dir will be taken care of by shutdown
    (reset! (:supervisors cluster-map) (remove-first finder-fn supervisors))
    (.close sup)))

(defn kill-local-storm-cluster [cluster-map]
  (.shutdown (:nimbus cluster-map))
  (if (not-nil? (:nimbus-thrift-server cluster-map))
    (do
      (log-message "shutting down thrift server")
      (try
        (.stop (:nimbus-thrift-server cluster-map))
        (catch Exception e (log-message "failed to stop thrift")))
      ))
  (.close (:state cluster-map))
  (.disconnect (:storm-cluster-state cluster-map))
  (doseq [s @(:supervisors cluster-map)]
    (.shutdownAllWorkers s nil ReadClusterState/THREAD_DUMP_ON_ERROR)
    (.close s))
  (ProcessSimulator/killAllProcesses)
  (if (not-nil? (:zookeeper cluster-map))
    (do
      (log-message "Shutting down in process zookeeper")
      (zk/shutdown-inprocess-zookeeper (:zookeeper cluster-map))
      (log-message "Done shutting down in process zookeeper")))
  (doseq [t @(:tmp-dirs cluster-map)]
    (log-message "Deleting temporary path " t)
    (try
      (rmr t)
      ;; on windows, the host process still holds lock on the logfile
      (catch Exception e (log-message (.getMessage e)))) ))

(def TEST-TIMEOUT-MS
  (let [timeout (System/getenv "STORM_TEST_TIMEOUT_MS")]
    (parse-int (if timeout timeout "10000"))))

(defmacro while-timeout [timeout-ms condition & body]
  `(let [end-time# (+ (System/currentTimeMillis) ~timeout-ms)]
     (log-debug "Looping until " '~condition)
     (while ~condition
       (when (> (System/currentTimeMillis) end-time#)
         (let [thread-dump# (Utils/threadDump)]
           (log-message "Condition " '~condition  " not met in " ~timeout-ms "ms")
           (log-message thread-dump#)
           (throw (AssertionError. (str "Test timed out (" ~timeout-ms "ms) " '~condition)))))
       ~@body)
     (log-debug "Condition met " '~condition)))

(defn wait-for-condition
  ([apredicate]
    (wait-for-condition TEST-TIMEOUT-MS apredicate))
  ([timeout-ms apredicate]
    (while-timeout timeout-ms (not (apredicate))
      (Time/sleep 100))))

(defn is-supervisor-waiting [^Supervisor supervisor]
   (.isWaiting supervisor))


(defn wait-until-cluster-waiting
  "Wait until the cluster is idle. Should be used with time simulation."
  ([cluster-map] (wait-until-cluster-waiting cluster-map TEST-TIMEOUT-MS))
  ([cluster-map timeout-ms]
  ;; wait until all workers, supervisors, and nimbus is waiting
  (let [supervisors @(:supervisors cluster-map)
        workers (filter (partial satisfies? common/DaemonCommon) (clojurify-structure (ProcessSimulator/getAllProcessHandles)))
        daemons (concat
                  [(:nimbus cluster-map)]
                  ; because a worker may already be dead
                  workers)]
    (while-timeout timeout-ms (or (not (every? (memfn waiting?) daemons))
                                (not (every? is-supervisor-waiting supervisors)))
                   (Thread/sleep (rand-int 20))
                   ;;      (doseq [d daemons]
                   ;;        (if-not ((memfn waiting?) d)
                   ;;          (println d)))
                   ))))

(defn advance-cluster-time
  ([cluster-map secs increment-secs]
   (loop [left secs]
     (when (> left 0)
       (let [diff (min left increment-secs)]
         (advance-time-secs! diff)
         (wait-until-cluster-waiting cluster-map)
         (recur (- left diff))))))
  ([cluster-map secs]
   (advance-cluster-time cluster-map secs 1)))

(defmacro with-local-cluster
  [[cluster-sym & args] & body]
  `(let [~cluster-sym (mk-local-storm-cluster ~@args)]
     (try
       ~@body
       (catch Throwable t#
         (log-error t# "Error in cluster")
         (throw t#))
       (finally
         (let [keep-waiting?# (atom true)
               f# (future (while @keep-waiting?# (simulate-wait ~cluster-sym)))]
           (kill-local-storm-cluster ~cluster-sym)
           (reset! keep-waiting?# false)
            @f#
           (.setSubject (ReqContext/context) nil))))))

(defmacro with-simulated-time-local-cluster
  [& args]
  `(with-simulated-time
     (with-local-cluster ~@args)))

(defmacro with-inprocess-zookeeper
  [port-sym & body]
  `(with-local-tmp [tmp#]
                   (let [[~port-sym zks#] (zk/mk-inprocess-zookeeper tmp#)]
                     (try
                       ~@body
                       (finally
                         (zk/shutdown-inprocess-zookeeper zks#))))))

(defn submit-local-topology
  [nimbus storm-name conf topology]
  (when-not (Utils/isValidConf conf)
    (throw (IllegalArgumentException. "Topology conf is not json-serializable")))
  (.submitTopology nimbus storm-name nil (to-json conf) topology))

(defn submit-local-topology-with-opts
  [nimbus storm-name conf topology submit-opts]
  (when-not (Utils/isValidConf conf)
    (throw (IllegalArgumentException. "Topology conf is not json-serializable")))
  (.submitTopologyWithOpts nimbus storm-name nil (to-json conf) topology submit-opts))

(defn mocked-convert-assignments-to-worker->resources [storm-cluster-state storm-name worker->resources]
  (fn [existing-assignments]
    (let [topology-id (common/get-storm-id storm-cluster-state storm-name)
          existing-assignments (into {} (for [[tid assignment] existing-assignments]
                                          {tid (:worker->resources assignment)}))
          new-assignments (assoc existing-assignments topology-id worker->resources)]
      new-assignments)))

(defn mocked-compute-new-topology->executor->node+port [storm-cluster-state storm-name executor->node+port]
  (fn [new-scheduler-assignments existing-assignments]
    (let [topology-id (common/get-storm-id storm-cluster-state storm-name)
          existing-assignments (into {} (for [[tid assignment] existing-assignments]
                                          {tid (:executor->node+port assignment)}))
          new-assignments (assoc existing-assignments topology-id executor->node+port)]
      new-assignments)))

(defn mocked-compute-new-scheduler-assignments []
  (fn [nimbus existing-assignments topologies scratch-topology-id]
    existing-assignments))

(defn submit-mocked-assignment
  [nimbus storm-cluster-state storm-name conf topology task->component executor->node+port worker->resources]
  (with-var-roots [common/storm-task-info (fn [& ignored] task->component)
                   nimbus/compute-new-scheduler-assignments (mocked-compute-new-scheduler-assignments)
                   nimbus/convert-assignments-to-worker->resources (mocked-convert-assignments-to-worker->resources
                                                          storm-cluster-state
                                                          storm-name
                                                          worker->resources)
                   nimbus/compute-new-topology->executor->node+port (mocked-compute-new-topology->executor->node+port
                                                                      storm-cluster-state
                                                                      storm-name
                                                                      executor->node+port)]
    (submit-local-topology nimbus storm-name conf topology)))

(defn mk-capture-launch-fn [capture-atom]
  (fn [supervisor storm-id port worker-id mem-onheap]
    (let [supervisor-id (:supervisor-id supervisor)
          conf (:conf supervisor)
          existing (get @capture-atom [supervisor-id port] [])]
      (set-worker-user! conf worker-id "")
      (swap! capture-atom assoc [supervisor-id port] (conj existing storm-id)))))

(defn find-worker-id
  [supervisor-conf port]
  (let [supervisor-state (supervisor-state supervisor-conf)
        worker->port (ls-approved-workers supervisor-state)]
    (first ((reverse-map worker->port) port))))

(defn find-worker-port
  [supervisor-conf worker-id]
  (let [supervisor-state (supervisor-state supervisor-conf)
        worker->port (ls-approved-workers supervisor-state)]
    (worker->port worker-id)))

(defnk aggregated-stat
  [cluster-map storm-name stat-key :component-ids nil]
  (let [state (:storm-cluster-state cluster-map)
        nimbus (:nimbus cluster-map)
        storm-id (common/get-storm-id state storm-name)
        component->tasks (reverse-map
                           (common/storm-task-info
                             (.getUserTopology nimbus storm-id)
                             (from-json (.getTopologyConf nimbus storm-id))))
        component->tasks (if component-ids
                           (select-keys component->tasks component-ids)
                           component->tasks)
        task-ids (apply concat (vals component->tasks))
        assignment (.assignment-info state storm-id nil)
        taskbeats (.taskbeats state storm-id (:task->node+port assignment))
        heartbeats (dofor [id task-ids] (get taskbeats id))
        stats (dofor [hb heartbeats] (if hb (stat-key (:stats hb)) 0))]
    (reduce + stats)))

(defn emitted-spout-tuples
  [cluster-map topology storm-name]
  (aggregated-stat
    cluster-map
    storm-name
    :emitted
    :component-ids (keys (.get_spouts topology))))

(defn transferred-tuples
  [cluster-map storm-name]
  (aggregated-stat cluster-map storm-name :transferred))

(defn acked-tuples
  [cluster-map storm-name]
  (aggregated-stat cluster-map storm-name :acked))

(defn simulate-wait
  [cluster-map]
  (if (Time/isSimulating)
    (advance-cluster-time cluster-map 10)
    (Thread/sleep 100)))

(defprotocol CompletableSpout
  (exhausted?
    [this]
    "Whether all the tuples for this spout have been completed.")
  (cleanup
    [this]
    "Cleanup any global state kept")
  (startup
    [this]
    "Prepare the spout (globally) before starting the topology"))

(extend-type FixedTupleSpout
  CompletableSpout
  (exhausted? [this]
              (= (-> this .getSourceTuples count)
                 (.getCompleted this)))
  (cleanup [this]
           (.cleanup this))
  (startup [this]))

(extend-type TransactionalSpoutCoordinator
  CompletableSpout
  (exhausted? [this]
              (exhausted? (.getSpout this)))
  (cleanup [this]
           (cleanup (.getSpout this)))
  (startup [this]
           (startup (.getSpout this))))

(extend-type PartitionedTransactionalSpoutExecutor
  CompletableSpout
  (exhausted? [this]
              (exhausted? (.getPartitionedSpout this)))
  (cleanup [this]
           (cleanup (.getPartitionedSpout this)))
  (startup [this]
           (startup (.getPartitionedSpout this))))

(extend-type MemoryTransactionalSpout
  CompletableSpout
  (exhausted? [this]
              (.isExhaustedTuples this))
  (cleanup [this]
           (.cleanup this))
  (startup [this]
           (.startup this)))

(defn spout-objects [spec-map]
  (for [[_ spout-spec] spec-map]
    (-> spout-spec
        .get_spout_object
        deserialized-component-object)))

(defn capture-topology
  [topology]
  (let [topology (.deepCopy topology)
        spouts (.get_spouts topology)
        bolts (.get_bolts topology)
        all-streams (apply concat
                           (for [[id spec] (merge (clojurify-structure spouts)
                                                  (clojurify-structure bolts))]
                             (for [[stream info] (.. spec get_common get_streams)]
                               [(GlobalStreamId. id stream) (.is_direct info)])))
        capturer (TupleCaptureBolt.)]
    (.set_bolts topology
                (assoc (clojurify-structure bolts)
                  (uuid)
                  (Bolt.
                    (serialize-component-object capturer)
                    (mk-plain-component-common (into {} (for [[id direct?] all-streams]
                                                          [id (if direct?
                                                                (mk-direct-grouping)
                                                                (mk-global-grouping))]))
                                               {}
                                               nil))))
    {:topology topology
     :capturer capturer}))

;; TODO: mock-sources needs to be able to mock out state spouts as well
(defnk complete-topology
  [cluster-map topology
   :mock-sources {}
   :storm-conf {}
   :cleanup-state true
   :topology-name nil
   :timeout-ms TEST-TIMEOUT-MS]
  ;; TODO: the idea of mocking for transactional topologies should be done an
  ;; abstraction level above... should have a complete-transactional-topology for this
  (let [{topology :topology capturer :capturer} (capture-topology topology)
        storm-name (or topology-name (str "topologytest-" (uuid)))
        state (:storm-cluster-state cluster-map)
        spouts (.get_spouts topology)
        replacements (map-val (fn [v]
                                (FixedTupleSpout.
                                  (for [tup v]
                                    (if (map? tup)
                                      (FixedTuple. (:stream tup) (:values tup))
                                      tup))))
                              mock-sources)]
    (doseq [[id spout] replacements]
      (let [spout-spec (get spouts id)]
        (.set_spout_object spout-spec (serialize-component-object spout))))
    (doseq [spout (spout-objects spouts)]
      (when-not (extends? CompletableSpout (.getClass spout))
        (throw (RuntimeException. (str "Cannot complete topology unless every spout is a CompletableSpout (or mocked to be); failed by " spout)))))

    (doseq [spout (spout-objects spouts)]
      (startup spout))

    (submit-local-topology (:nimbus cluster-map) storm-name storm-conf topology)
    (when (Time/isSimulating)
      (advance-cluster-time cluster-map 11))

    (let [storm-id (common/get-storm-id state storm-name)]
      ;;Give the topology time to come up without using it to wait for the spouts to complete
      (simulate-wait cluster-map)

      (while-timeout timeout-ms (not (every? exhausted? (spout-objects spouts)))
                     (simulate-wait cluster-map))

      (.killTopologyWithOpts (:nimbus cluster-map) storm-name (doto (KillOptions.) (.set_wait_secs 0)))
      (while-timeout timeout-ms (.assignment-info state storm-id nil)
                     (simulate-wait cluster-map))
      (when cleanup-state
        (doseq [spout (spout-objects spouts)]
          (cleanup spout))))

    (if cleanup-state
      (.getAndRemoveResults capturer)
      (.getAndClearResults capturer))))

(defn read-tuples
  ([results component-id stream-id]
   (let [fixed-tuples (get results component-id [])]
     (mapcat
       (fn [ft]
         (if (= stream-id (. ft stream))
           [(vec (. ft values))]))
       fixed-tuples)
     ))
  ([results component-id]
   (read-tuples results component-id Utils/DEFAULT_STREAM_ID)))

(defn ms=
  [& args]
  (apply = (map multi-set args)))

(def TRACKER-BOLT-ID "+++tracker-bolt")

;; TODO: should override system-topology! and wrap everything there
(defn mk-tracked-topology
  ([tracked-cluster topology]
   (let [track-id (::track-id tracked-cluster)
         ret (.deepCopy topology)]
     (dofor [[_ bolt] (.get_bolts ret)
             :let [obj (deserialized-component-object (.get_bolt_object bolt))]]
            (.set_bolt_object bolt (serialize-component-object
                                     (BoltTracker. obj track-id))))
     (dofor [[_ spout] (.get_spouts ret)
             :let [obj (deserialized-component-object (.get_spout_object spout))]]
            (.set_spout_object spout (serialize-component-object
                                       (SpoutTracker. obj track-id))))
     {:topology ret
      :last-spout-emit (atom 0)
      :cluster tracked-cluster})))

(defn assoc-track-id
  [cluster track-id]
  (assoc cluster ::track-id track-id))

(defn increment-global!
  [id key amt]
  (-> (RegisteredGlobalState/getState id)
      (get key)
      (.addAndGet amt)))

(defn global-amt
  [id key]
  (-> (RegisteredGlobalState/getState id)
      (get key)
      .get))

(defmacro with-tracked-cluster
  [[cluster-sym & cluster-args] & body]
  `(let [id# (uuid)]
     (RegisteredGlobalState/setState
       id#
       (doto (ConcurrentHashMap.)
         (.put "spout-emitted" (AtomicInteger. 0))
         (.put "transferred" (AtomicInteger. 0))
         (.put "processed" (AtomicInteger. 0))))
     (with-var-roots
       [acker/mk-acker-bolt
        (let [old# acker/mk-acker-bolt]
          (fn [& args#] (NonRichBoltTracker. (apply old# args#) id#)))
        ;; critical that this particular function is overridden here,
        ;; since the transferred stat needs to be incremented at the moment
        ;; of tuple emission (and not on a separate thread later) for
        ;; topologies to be tracked correctly. This is because "transferred" *must*
        ;; be incremented before "processing".
        executor/mk-executor-transfer-fn
        (let [old# executor/mk-executor-transfer-fn]
          (fn [& args#]
            (let [transferrer# (apply old# args#)]
              (fn [& args2#]
                ;; (log-message "Transferring: " transfer-args#)
                (increment-global! id# "transferred" 1)
                (apply transferrer# args2#)))))]
       (with-simulated-time-local-cluster [~cluster-sym ~@cluster-args]
                           (let [~cluster-sym (assoc-track-id ~cluster-sym id#)]
                             ~@body)))
     (RegisteredGlobalState/clearState id#)))

(defn tracked-wait
  "Waits until topology is idle and 'amt' more tuples have been emitted by spouts."
  ([tracked-topology]
     (tracked-wait tracked-topology 1 TEST-TIMEOUT-MS))
  ([tracked-topology amt]
     (tracked-wait tracked-topology amt TEST-TIMEOUT-MS))
  ([tracked-topology amt timeout-ms]
    (let [target (+ amt @(:last-spout-emit tracked-topology))
          track-id (-> tracked-topology :cluster ::track-id)
          waiting? (fn []
                     (or (not= target (global-amt track-id "spout-emitted"))
                         (not= (global-amt track-id "transferred")
                               (global-amt track-id "processed"))))]
      (while-timeout timeout-ms (waiting?)
                     ;; (println "Spout emitted: " (global-amt track-id "spout-emitted"))
                     ;; (println "Processed: " (global-amt track-id "processed"))
                     ;; (println "Transferred: " (global-amt track-id "transferred"))
                    (advance-time-secs! 1)
                    (Thread/sleep (rand-int 200)))
      (reset! (:last-spout-emit tracked-topology) target))))

(defnk test-tuple
  [values
   :stream Utils/DEFAULT_STREAM_ID
   :component "component"
   :fields nil]
  (let [fields (or fields
                   (->> (iterate inc 1)
                        (take (count values))
                        (map #(str "field" %))))
        spout-spec (mk-spout-spec* (TestWordSpout.)
                                   {stream fields})
        topology (StormTopology. {component spout-spec} {} {})
        context (TopologyContext.
                  topology
                  (read-storm-config)
                  {(int 1) component}
                  {component [(int 1)]}
                  {component {stream (Fields. fields)}}
                  "test-storm-id"
                  nil
                  nil
                  (int 1)
                  nil
                  [(int 1)]
                  {}
                  {}
                  (HashMap.)
                  (HashMap.)
                  (atom false))]
    (TupleImpl. context values 1 stream)))

(defmacro with-timeout
  [millis unit & body]
  `(let [f# (future ~@body)]
     (try
       (.get f# ~millis ~unit)
       (finally (future-cancel f#)))))

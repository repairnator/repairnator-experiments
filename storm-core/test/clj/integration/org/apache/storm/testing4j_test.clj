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
(ns integration.org.apache.storm.testing4j-test
  (:use [clojure.test])
  (:use [org.apache.storm config clojure testing util])
  (:require [integration.org.apache.storm.integration-test :as it])
  (:require [org.apache.storm.thrift :as thrift])
  (:import [org.apache.storm Testing Config ILocalCluster])
  (:import [org.apache.storm.tuple Values Tuple])
  (:import [org.apache.storm.utils Time Utils])
  (:import [org.apache.storm.testing MkClusterParam TestJob MockedSources TestWordSpout
            TestWordCounter TestGlobalCount TestAggregatesCounter CompleteTopologyParam
            AckFailMapTracker MkTupleParam]))

(deftest test-with-simulated-time
  (is (= false (Time/isSimulating)))
  (Testing/withSimulatedTime (fn []
                               (is (= true (Time/isSimulating)))))
  (is (= false (Time/isSimulating))))

(deftest test-with-local-cluster
  (let [mk-cluster-param (doto (MkClusterParam.)
                           (.setSupervisors (int 2))
                           (.setPortsPerSupervisor (int 5)))
        daemon-conf (doto (Config.)
                      (.put SUPERVISOR-ENABLE false)
                      (.put TOPOLOGY-ACKER-EXECUTORS 0))]
    (Testing/withLocalCluster mk-cluster-param (reify TestJob
                                                 (^void run [this ^ILocalCluster cluster]
                                                   (is (not (nil? cluster)))
                                                   (is (not (nil? (.getState cluster))))
                                                   (is (not (nil? (:nimbus (.getState cluster))))))))))

(deftest test-with-simulated-time-local-cluster
  (let [mk-cluster-param (doto (MkClusterParam.)
                           (.setSupervisors (int 2)))
        daemon-conf (doto (Config.)
                      (.put SUPERVISOR-ENABLE false)
                      (.put TOPOLOGY-ACKER-EXECUTORS 0))]
    (is (not (Time/isSimulating)))
    (Testing/withSimulatedTimeLocalCluster mk-cluster-param (reify TestJob
                                                              (^void run [this ^ILocalCluster cluster]
                                                                (is (not (nil? cluster)))
                                                                (is (not (nil? (.getState cluster))))
                                                                (is (not (nil? (:nimbus (.getState cluster)))))
                                                                (is (Time/isSimulating)))))
    (is (not (Time/isSimulating)))))

(def complete-topology-testjob
  (reify TestJob
       (^void run [this ^ILocalCluster cluster]
         (let [topology (thrift/mk-topology
                         {"1" (thrift/mk-spout-spec (TestWordSpout. true) :parallelism-hint 3)}
                         {"2" (thrift/mk-bolt-spec {"1" ["word"]} (TestWordCounter.) :parallelism-hint 4)
                          "3" (thrift/mk-bolt-spec {"1" :global} (TestGlobalCount.))
                          "4" (thrift/mk-bolt-spec {"2" :global} (TestAggregatesCounter.))
                          })
               mocked-sources (doto (MockedSources.)
                                (.addMockData "1" (into-array Values [(Values. (into-array ["nathan"]))
                                                                      (Values. (into-array ["bob"]))
                                                                      (Values. (into-array ["joey"]))
                                                                      (Values. (into-array ["nathan"]))])
                                              ))
               storm-conf (doto (Config.)
                            (.setNumWorkers 2))
               complete-topology-param (doto (CompleteTopologyParam.)
                                         (.setMockedSources mocked-sources)
                                         (.setStormConf storm-conf))
               results (Testing/completeTopology cluster
                                                 topology
                                                 complete-topology-param)]
           (is (Testing/multiseteq [["nathan"] ["bob"] ["joey"] ["nathan"]]
                           (Testing/readTuples results "1")))
           (is (Testing/multiseteq [["nathan" 1] ["nathan" 2] ["bob" 1] ["joey" 1]]
                           (read-tuples results "2")))
           (is (= [[1] [2] [3] [4]]
                  (Testing/readTuples results "3")))
           (is (= [[1] [2] [3] [4]]
                  (Testing/readTuples results "4")))
           ))))

(deftest test-complete-topology
  (doseq [zmq-on? [true false]
          :let [daemon-conf (doto (Config.)
                              (.put STORM-LOCAL-MODE-ZMQ zmq-on?))
                mk-cluster-param (doto (MkClusterParam.)
                                   (.setSupervisors (int 4))
                                   (.setDaemonConf daemon-conf))]]
    (Testing/withSimulatedTimeLocalCluster
      mk-cluster-param complete-topology-testjob )
    (Testing/withLocalCluster
      mk-cluster-param complete-topology-testjob)))

(deftest test-with-tracked-cluster
  (Testing/withTrackedCluster
   (reify TestJob
     (^void run [this ^ILocalCluster cluster]
       (let [[feeder checker] (it/ack-tracking-feeder ["num"])
             tracked (Testing/mkTrackedTopology
                      cluster
                      (topology
                       {"1" (spout-spec feeder)}
                       {"2" (bolt-spec {"1" :shuffle} it/identity-bolt)
                        "3" (bolt-spec {"1" :shuffle} it/identity-bolt)
                        "4" (bolt-spec
                             {"2" :shuffle
                              "3" :shuffle}
                             (it/agg-bolt 4))}))]
         (.submitTopology cluster
                          "test-acking2"
                          (doto (Config.)
                            (.put TOPOLOGY-DEBUG true))
                          (.getTopology tracked))
         (.feed feeder [1])
         (Testing/trackedWait tracked (int 1))
         (checker 0)
         (.feed feeder [1])
         (Testing/trackedWait tracked (int 1))
         (checker 2)
         )))))

(deftest test-advance-cluster-time
  (let [daemon-conf (doto (Config.)
                      (.put TOPOLOGY-ENABLE-MESSAGE-TIMEOUTS true))
        mk-cluster-param (doto (MkClusterParam.)
                           (.setDaemonConf daemon-conf))]
    (Testing/withSimulatedTimeLocalCluster
     mk-cluster-param
     (reify TestJob
       (^void run [this ^ILocalCluster cluster]
         (let [feeder (feeder-spout ["field1"])
               tracker (AckFailMapTracker.)
               _ (.setAckFailDelegate feeder tracker)
               topology (thrift/mk-topology
                         {"1" (thrift/mk-spout-spec feeder)}
                         {"2" (thrift/mk-bolt-spec {"1" :global} it/ack-every-other)})
               storm-conf (doto (Config.)
                            (.put TOPOLOGY-MESSAGE-TIMEOUT-SECS 10))]
           (.submitTopology cluster
                            "timeout-tester"
                            storm-conf
                            topology)
           ;;Wait for the topology to come up
           (.feed feeder ["a"] 1)
           (.feed feeder ["b"] 2)
           (.feed feeder ["c"] 3)
           (Testing/advanceClusterTime cluster (int 9))
           (it/assert-acked tracker 1 3)
           (is (not (.isFailed tracker 2)))
           (Testing/advanceClusterTime cluster (int 20))
           (it/assert-failed tracker 2)
           ))))))

(deftest test-disable-tuple-timeout
  (let [daemon-conf (doto (Config.)
                      (.put TOPOLOGY-ENABLE-MESSAGE-TIMEOUTS false))
        mk-cluster-param (doto (MkClusterParam.)
                           (.setDaemonConf daemon-conf))]
    (Testing/withSimulatedTimeLocalCluster
      mk-cluster-param
      (reify TestJob
        (^void run [this ^ILocalCluster cluster]
          (let [feeder (feeder-spout ["field1"])
                tracker (AckFailMapTracker.)
                _ (.setAckFailDelegate feeder tracker)
                topology (thrift/mk-topology
                           {"1" (thrift/mk-spout-spec feeder)}
                           {"2" (thrift/mk-bolt-spec {"1" :global} it/ack-every-other)})
                storm-conf (doto (Config.)
                             (.put TOPOLOGY-DEBUG true)
                             (.put TOPOLOGY-MESSAGE-TIMEOUT-SECS 10)
                             (.put TOPOLOGY-ENABLE-MESSAGE-TIMEOUTS false))]
            (.submitTopology cluster
              "disable-timeout-tester"
              storm-conf
              topology)
            ;;Wait for the topology to come up
            (.feed feeder ["a"] 1)
            (.feed feeder ["b"] 2)
            (.feed feeder ["c"] 3)
            (Testing/advanceClusterTime cluster (int 9))
            (it/assert-acked tracker 1 3)
            (is (not (.isFailed tracker 2)))
            (Testing/advanceClusterTime cluster (int 20))
            (is (not (.isFailed tracker 2)))
            ))))))

(deftest test-test-tuple
  (letlocals
   ;; test the one-param signature
   (bind ^Tuple tuple (Testing/testTuple ["james" "bond"]))
   (is (= ["james" "bond"] (.getValues tuple)))
   (is (= Utils/DEFAULT_STREAM_ID (.getSourceStreamId tuple)))
   (is (= ["field1" "field2"] (-> tuple .getFields .toList)))
   (is (= "component" (.getSourceComponent tuple)))

   ;; test the two-params signature
   (bind mk-tuple-param (MkTupleParam.))
   (doto mk-tuple-param
     (.setStream "test-stream")
     (.setComponent "test-component")
     (.setFields (into-array String ["fname" "lname"])))
   (bind ^Tuple tuple (Testing/testTuple ["james" "bond"] mk-tuple-param))
   (is (= ["james" "bond"] (.getValues tuple)))
   (is (= "test-stream" (.getSourceStreamId tuple)))
   (is (= ["fname" "lname"] (-> tuple .getFields .toList)))
   (is (= "test-component" (.getSourceComponent tuple)))))

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.storm.daemon.worker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.StormTimer;
import org.apache.storm.messaging.netty.BackPressureStatus;
import org.apache.storm.cluster.IStateStorage;
import org.apache.storm.cluster.IStormClusterState;
import org.apache.storm.cluster.VersionedData;
import org.apache.storm.daemon.StormCommon;
import org.apache.storm.daemon.supervisor.AdvancedFSOps;
import org.apache.storm.executor.IRunningExecutor;
import org.apache.storm.generated.Assignment;
import org.apache.storm.generated.DebugOptions;
import org.apache.storm.generated.Grouping;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.NodeInfo;
import org.apache.storm.generated.StormBase;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.generated.StreamInfo;
import org.apache.storm.generated.TopologyStatus;
import org.apache.storm.grouping.Load;
import org.apache.storm.grouping.LoadMapping;
import org.apache.storm.hooks.BaseWorkerHook;
import org.apache.storm.messaging.ConnectionWithStatus;
import org.apache.storm.messaging.DeserializingConnectionCallback;
import org.apache.storm.messaging.IConnection;
import org.apache.storm.messaging.IContext;
import org.apache.storm.messaging.TransportFactory;
import org.apache.storm.security.auth.IAutoCredentials;

import org.apache.storm.policy.IWaitStrategy;
import org.apache.storm.serialization.ITupleSerializer;
import org.apache.storm.serialization.KryoTupleSerializer;
import org.apache.storm.task.WorkerTopologyContext;
import org.apache.storm.tuple.AddressedTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.ConfigUtils;
import org.apache.storm.utils.JCQueue;
import org.apache.storm.utils.Utils;
import org.apache.storm.utils.ObjectReader;
import org.apache.storm.utils.SupervisorClient;
import org.apache.storm.utils.ThriftTopologyUtils;
import org.apache.storm.utils.Utils.SmartThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerState {

    private static final Logger LOG = LoggerFactory.getLogger(WorkerState.class);
    private static long dropCount = 0;

    final Map<String, Object> conf;
    final IContext mqContext;
    private final WorkerTransfer workerTransfer;
    private final BackPressureTracker bpTracker;

    public Map<String, Object> getConf() {
        return conf;
    }

    public IConnection getReceiver() {
        return receiver;
    }

    public String getTopologyId() {
        return topologyId;
    }

    public int getPort() {
        return port;
    }

    public String getWorkerId() {
        return workerId;
    }

    public IStateStorage getStateStorage() {
        return stateStorage;
    }

    public AtomicBoolean getIsTopologyActive() {
        return isTopologyActive;
    }

    public AtomicReference<Map<String, DebugOptions>> getStormComponentToDebug() {
        return stormComponentToDebug;
    }

    public Set<List<Long>> getLocalExecutors() {
        return localExecutors;
    }

    public List<Integer> getLocalTaskIds() {
        return localTaskIds;
    }

    public Map<Integer, JCQueue> getLocalReceiveQueues() {
        return localReceiveQueues;
    }

    public Map<String, Object> getTopologyConf() {
        return topologyConf;
    }

    public StormTopology getTopology() {
        return topology;
    }

    public StormTopology getSystemTopology() {
        return systemTopology;
    }

    public Map<Integer, String> getTaskToComponent() {
        return taskToComponent;
    }

    public Map<String, Map<String, Fields>> getComponentToStreamToFields() {
        return componentToStreamToFields;
    }

    public Map<String, List<Integer>> getComponentToSortedTasks() {
        return componentToSortedTasks;
    }

    public Map<String, Long> getBlobToLastKnownVersion() {
        return blobToLastKnownVersion;
    }

    public AtomicReference<Map<NodeInfo, IConnection>> getCachedNodeToPortSocket() {
        return cachedNodeToPortSocket;
    }

    public Map<List<Long>, JCQueue> getExecutorReceiveQueueMap() {
        return executorReceiveQueueMap;
    }

    public Map<Integer, JCQueue> getShortExecutorReceiveQueueMap() {
        return shortExecutorReceiveQueueMap;
    }

    public Runnable getSuicideCallback() {
        return suicideCallback;
    }

    public Utils.UptimeComputer getUptime() {
        return uptime;
    }

    public Map<String, Object> getDefaultSharedResources() {
        return defaultSharedResources;
    }

    public Map<String, Object> getUserSharedResources() {
        return userSharedResources;
    }

    final IConnection receiver;
    final String topologyId;
    final String assignmentId;
    final int supervisorPort;
    final int port;
    final String workerId;
    final IStateStorage stateStorage;
    final IStormClusterState stormClusterState;

    // when worker bootup, worker will start to setup initial connections to
    // other workers. When all connection is ready, we will enable this flag
    // and spout and bolt will be activated.
    // used in worker only, keep it as atomic
    final AtomicBoolean isWorkerActive;
    final AtomicBoolean isTopologyActive;
    final AtomicReference<Map<String, DebugOptions>> stormComponentToDebug;

    // local executors and localTaskIds running in this worker
    final Set<List<Long>> localExecutors;
    final ArrayList<Integer> localTaskIds;
    final Map<Integer, JCQueue> localReceiveQueues = new HashMap<>(); // [taskId]-> JCQueue :  initialized after local executors are initialized

    final Map<String, Object> topologyConf;
    final StormTopology topology;
    final StormTopology systemTopology;
    final Map<Integer, String> taskToComponent;
    final Map<String, Map<String, Fields>> componentToStreamToFields;
    final Map<String, List<Integer>> componentToSortedTasks;
    final ConcurrentMap<String, Long> blobToLastKnownVersion;
    final ReentrantReadWriteLock endpointSocketLock;
    final AtomicReference<Map<Integer, NodeInfo>> cachedTaskToNodePort;
    final AtomicReference<Map<NodeInfo, IConnection>> cachedNodeToPortSocket;
    final Map<List<Long>, JCQueue> executorReceiveQueueMap;
    // executor id is in form [start_task_id end_task_id]
    // short executor id is start_task_id
    final Map<Integer, JCQueue> shortExecutorReceiveQueueMap;
    final Map<Integer, Integer> taskToShortExecutor;
    final Runnable suicideCallback;
    final Utils.UptimeComputer uptime;
    final Map<String, Object> defaultSharedResources;
    final Map<String, Object> userSharedResources;
    final LoadMapping loadMapping;
    final AtomicReference<Map<String, VersionedData<Assignment>>> assignmentVersions;

    public LoadMapping getLoadMapping() {
        return loadMapping;
    }

    public AtomicReference<Map<String, VersionedData<Assignment>>> getAssignmentVersions() {
        return assignmentVersions;
    }

    public StormTimer getUserTimer() {
        return userTimer;
    }

    // Timers
    final StormTimer heartbeatTimer = mkHaltingTimer("heartbeat-timer");
    final StormTimer refreshLoadTimer = mkHaltingTimer("refresh-load-timer");
    final StormTimer refreshConnectionsTimer = mkHaltingTimer("refresh-connections-timer");
    final StormTimer refreshCredentialsTimer = mkHaltingTimer("refresh-credentials-timer");
    final StormTimer checkForUpdatedBlobsTimer = mkHaltingTimer("check-for-updated-blobs-timer");
    final StormTimer resetLogLevelsTimer = mkHaltingTimer("reset-log-levels-timer");
    final StormTimer refreshActiveTimer = mkHaltingTimer("refresh-active-timer");
    final StormTimer executorHeartbeatTimer = mkHaltingTimer("executor-heartbeat-timer");
    final StormTimer flushTupleTimer = mkHaltingTimer("flush-tuple-timer");
    final StormTimer userTimer = mkHaltingTimer("user-timer");
    final StormTimer backPressureCheckTimer = mkHaltingTimer("backpressure-check-timer");

    // global variables only used internally in class
    private final Set<Integer> outboundTasks;
    private final AtomicLong nextLoadUpdate = new AtomicLong(0);

    private final boolean trySerializeLocal;
    private final Collection<IAutoCredentials> autoCredentials;
    private static final long LOAD_REFRESH_INTERVAL_MS = 5000L;

    public WorkerState(Map<String, Object> conf, IContext mqContext, String topologyId, String assignmentId,
        int supervisorPort, int port, String workerId, Map<String, Object> topologyConf, IStateStorage stateStorage,
        IStormClusterState stormClusterState, Collection<IAutoCredentials> autoCredentials) throws IOException, InvalidTopologyException {
        this.autoCredentials = autoCredentials;
        this.conf = conf;
        this.localExecutors = new HashSet<>(readWorkerExecutors(stormClusterState, topologyId, assignmentId, port));
        this.mqContext = (null != mqContext) ? mqContext : TransportFactory.makeContext(topologyConf);
        this.receiver = this.mqContext.bind(topologyId, port);
        this.topologyId = topologyId;
        this.assignmentId = assignmentId;
        this.supervisorPort = supervisorPort;
        this.port = port;
        this.workerId = workerId;
        this.stateStorage = stateStorage;
        this.stormClusterState = stormClusterState;
        this.isWorkerActive = new AtomicBoolean(false);
        this.isTopologyActive = new AtomicBoolean(false);
        this.stormComponentToDebug = new AtomicReference<>();
        this.executorReceiveQueueMap = mkReceiveQueueMap(topologyConf, localExecutors);
        this.shortExecutorReceiveQueueMap = new HashMap<>();
        this.localTaskIds = new ArrayList<>();
        this.blobToLastKnownVersion = new ConcurrentHashMap<>();
        for (Map.Entry<List<Long>, JCQueue> entry : executorReceiveQueueMap.entrySet()) {
            this.shortExecutorReceiveQueueMap.put(entry.getKey().get(0).intValue(), entry.getValue());
            this.localTaskIds.addAll(StormCommon.executorIdToTasks(entry.getKey()));
        }
        Collections.sort(localTaskIds);
        this.topologyConf = topologyConf;
        this.topology = ConfigUtils.readSupervisorTopology(conf, topologyId, AdvancedFSOps.make(conf));
        this.systemTopology = StormCommon.systemTopology(topologyConf, topology);
        this.taskToComponent = StormCommon.stormTaskInfo(topology, topologyConf);
        this.componentToStreamToFields = new HashMap<>();
        for (String c : ThriftTopologyUtils.getComponentIds(systemTopology)) {
            Map<String, Fields> streamToFields = new HashMap<>();
            for (Map.Entry<String, StreamInfo> stream :
                    ThriftTopologyUtils.getComponentCommon(systemTopology, c).get_streams().entrySet()) {
                streamToFields.put(stream.getKey(), new Fields(stream.getValue().get_output_fields()));
            }
            componentToStreamToFields.put(c, streamToFields);
        }
        this.componentToSortedTasks = Utils.reverseMap(taskToComponent);
        this.componentToSortedTasks.values().forEach(Collections::sort);
        this.endpointSocketLock = new ReentrantReadWriteLock();
        this.cachedNodeToPortSocket = new AtomicReference<>(new HashMap<>());
        this.cachedTaskToNodePort = new AtomicReference<>(new HashMap<>());
        this.taskToShortExecutor = new HashMap<>();
        for (List<Long> executor : this.localExecutors) {
            for (Integer task : StormCommon.executorIdToTasks(executor)) {
                taskToShortExecutor.put(task, executor.get(0).intValue());
            }
        }
        this.suicideCallback = Utils.mkSuicideFn();
        this.uptime = Utils.makeUptimeComputer();
        this.defaultSharedResources = makeDefaultResources();
        this.userSharedResources = makeUserResources();
        this.loadMapping = new LoadMapping();
        this.assignmentVersions = new AtomicReference<>(new HashMap<>());
        this.outboundTasks = workerOutboundTasks();
        this.trySerializeLocal = topologyConf.containsKey(Config.TOPOLOGY_TESTING_ALWAYS_TRY_SERIALIZE)
            && (Boolean) topologyConf.get(Config.TOPOLOGY_TESTING_ALWAYS_TRY_SERIALIZE);
        if (trySerializeLocal) {
            LOG.warn("WILL TRY TO SERIALIZE ALL TUPLES (Turn off {} for production", Config.TOPOLOGY_TESTING_ALWAYS_TRY_SERIALIZE);
        }
        int maxTaskId = getMaxTaskId(componentToSortedTasks);
        this.workerTransfer = new WorkerTransfer(this, topologyConf, maxTaskId);
        this.bpTracker = new BackPressureTracker(workerId, localTaskIds);
    }

    public void refreshConnections() {
        try {
            refreshConnections(() -> refreshConnectionsTimer.schedule(0, this::refreshConnections));
        } catch (Exception e) {
            throw Utils.wrapInRuntime(e);
        }
    }

    public SmartThread makeTransferThread() {
        return workerTransfer.makeTransferThread();
    }

    public void refreshConnections(Runnable callback) throws Exception {
        Assignment assignment = getLocalAssignment(conf, stormClusterState, topologyId);

        Set<NodeInfo> neededConnections = new HashSet<>();
        Map<Integer, NodeInfo> newTaskToNodePort = new HashMap<>();
        if (null != assignment) {
            Map<Integer, NodeInfo> taskToNodePort = StormCommon.taskToNodeport(assignment.get_executor_node_port());
            for (Map.Entry<Integer, NodeInfo> taskToNodePortEntry : taskToNodePort.entrySet()) {
                Integer task = taskToNodePortEntry.getKey();
                if (outboundTasks.contains(task)) {
                    newTaskToNodePort.put(task, taskToNodePortEntry.getValue());
                    if (!localTaskIds.contains(task)) {
                        neededConnections.add(taskToNodePortEntry.getValue());
                    }
                }
            }
        }

        Set<NodeInfo> currentConnections = cachedNodeToPortSocket.get().keySet();
        Set<NodeInfo> newConnections = Sets.difference(neededConnections, currentConnections);
        Set<NodeInfo> removeConnections = Sets.difference(currentConnections, neededConnections);

        // Add new connections atomically
        cachedNodeToPortSocket.getAndUpdate(prev -> {
            Map<NodeInfo, IConnection> next = new HashMap<>(prev);
            for (NodeInfo nodeInfo : newConnections) {
                next.put(nodeInfo,
                    mqContext.connect(
                        topologyId,
                        assignment.get_node_host().get(nodeInfo.get_node()),    // Host
                        nodeInfo.get_port().iterator().next().intValue(),       // Port
                        workerTransfer.getRemoteBackPressureStatus()));
            }
            return next;
        });


        try {
            endpointSocketLock.writeLock().lock();
            cachedTaskToNodePort.set(newTaskToNodePort);
        } finally {
            endpointSocketLock.writeLock().unlock();
        }

        for (NodeInfo nodeInfo : removeConnections) {
            cachedNodeToPortSocket.get().get(nodeInfo).close();
        }

        // Remove old connections atomically
        cachedNodeToPortSocket.getAndUpdate(prev -> {
            Map<NodeInfo, IConnection> next = new HashMap<>(prev);
            removeConnections.forEach(next::remove);
            return next;
        });

    }

    public void refreshStormActive() {
        refreshStormActive(() -> refreshActiveTimer.schedule(0, this::refreshStormActive));
    }

    public void refreshStormActive(Runnable callback) {
        StormBase base = stormClusterState.stormBase(topologyId, callback);
        isTopologyActive.set(
            (null != base) &&
                (base.get_status() == TopologyStatus.ACTIVE) &&
                (isWorkerActive.get()));
        if (null != base) {
            Map<String, DebugOptions> debugOptionsMap = new HashMap<>(base.get_component_debug());
            for (DebugOptions debugOptions : debugOptionsMap.values()) {
                if (!debugOptions.is_set_samplingpct()) {
                    debugOptions.set_samplingpct(10);
                }
                if (!debugOptions.is_set_enable()) {
                    debugOptions.set_enable(false);
                }
            }
            stormComponentToDebug.set(debugOptionsMap);
            LOG.debug("Events debug options {}", stormComponentToDebug.get());
        }
    }

    private static double getQueueLoad(JCQueue q) {
        JCQueue.QueueMetrics qMetrics = q.getMetrics();
        return ((double) qMetrics.population()) / qMetrics.capacity();
    }

    public void refreshLoad(List<IRunningExecutor> execs) {
        Set<Integer> remoteTasks = Sets.difference(new HashSet<>(outboundTasks), new HashSet<>(localTaskIds));
        Long now = System.currentTimeMillis();
        Map<Integer, Double> localLoad = new HashMap<>();
        for (IRunningExecutor exec : execs) {
            double receiveLoad = getQueueLoad(exec.getReceiveQueue());
            localLoad.put(exec.getExecutorId().get(0).intValue(), receiveLoad);
        }

        Map<Integer, Load> remoteLoad = new HashMap<>();
        cachedNodeToPortSocket.get().values().stream().forEach(conn -> remoteLoad.putAll(conn.getLoad(remoteTasks)));
        loadMapping.setLocal(localLoad);
        loadMapping.setRemote(remoteLoad);

        if (now > nextLoadUpdate.get()) {
            receiver.sendLoadMetrics(localLoad);
            nextLoadUpdate.set(now + LOAD_REFRESH_INTERVAL_MS);
        }
    }

    // checks if the tasks which had back pressure are now free again. if so, sends an update to other workers
    public void refreshBackPressureStatus() {
        LOG.debug("Checking for change in Backpressure status on worker's tasks");
        boolean bpSituationChanged = bpTracker.refreshBpTaskList();
        if (bpSituationChanged) {
            BackPressureStatus bpStatus = bpTracker.getCurrStatus();
            receiver.sendBackPressureStatus(bpStatus);
        }
    }


    /**
     * we will wait all connections to be ready and then activate the spout/bolt
     * when the worker bootup.
     */
    public void activateWorkerWhenAllConnectionsReady() {
        int delaySecs = 0;
        int recurSecs = 1;
        refreshActiveTimer.schedule(delaySecs,
            () -> {
                if (areAllConnectionsReady()) {
                    LOG.info("All connections are ready for worker {}:{} with id {}", assignmentId, port, workerId);
                    isWorkerActive.set(Boolean.TRUE);
                } else {
                    refreshActiveTimer.schedule(recurSecs, () -> activateWorkerWhenAllConnectionsReady(), false, 0);
                }
            }
        );
    }

    public void registerCallbacks() {
        LOG.info("Registering IConnectionCallbacks for {}:{}", assignmentId, port);
        receiver.registerRecv(new DeserializingConnectionCallback(topologyConf,
            getWorkerTopologyContext(),
            this::transferLocalBatch));
        // Send curr BackPressure status to new clients
        receiver.registerNewConnectionResponse(
            () -> {
                BackPressureStatus bpStatus = bpTracker.getCurrStatus();
                LOG.info("Sending BackPressure status to new client. BPStatus: {}", bpStatus);
                return bpStatus;
            }
        );
    }

    /* Not a Blocking call. If cannot emit, will add 'tuple' to pendingEmits and return 'false'. 'pendingEmits' can be null */
    public boolean tryTransferRemote(AddressedTuple tuple, Queue<AddressedTuple> pendingEmits, ITupleSerializer serializer) {
        return workerTransfer.tryTransferRemote(tuple, pendingEmits, serializer);
    }

    public void flushRemotes() throws InterruptedException {
        workerTransfer.flushRemotes();
    }

    public boolean tryFlushRemotes() {
        return workerTransfer.tryFlushRemotes();
    }

    // Receives msgs from remote workers and feeds them to local executors. If any receiving local executor is under Back Pressure,
    // informs other workers about back pressure situation. Runs in the NettyWorker thread.
    private void transferLocalBatch(ArrayList<AddressedTuple> tupleBatch) {
        int lastOverflowCount = 0; // overflowQ size at the time the last BPStatus was sent

        for (int i = 0; i < tupleBatch.size(); i++) {
            AddressedTuple tuple = tupleBatch.get(i);
            JCQueue queue = shortExecutorReceiveQueueMap.get(tuple.dest);

            // 1- try adding to main queue if its overflow is not empty
            if (queue.isEmptyOverflow()) {
                if (queue.tryPublish(tuple)) {
                    continue;
                }
            }

            // 2- BP detected (i.e MainQ is full). So try adding to overflow
            int currOverflowCount = queue.getOverflowCount();
            if (bpTracker.recordBackPressure(tuple.dest, queue)) {
                receiver.sendBackPressureStatus(bpTracker.getCurrStatus());
                lastOverflowCount = currOverflowCount;
            } else {

                if (currOverflowCount - lastOverflowCount > 10000) {
                    // resend BP status, in case prev notification was missed or reordered
                    BackPressureStatus bpStatus = bpTracker.getCurrStatus();
                    receiver.sendBackPressureStatus(bpStatus);
                    lastOverflowCount = currOverflowCount;
                    LOG.debug("Re-sent BackPressure Status. OverflowCount = {}, BP Status ID = {}. ", currOverflowCount, bpStatus.id);
                }
            }
            if (!queue.tryPublishToOverflow(tuple)) {
                dropMessage(tuple, queue);
            }
        }
    }

    private void dropMessage(AddressedTuple tuple, JCQueue queue) {
        ++dropCount;
        queue.recordMsgDrop();
        LOG.warn("Dropping message as overflow threshold has reached for Q = {}. OverflowCount = {}. Total Drop Count= {}, Dropped Message : {}", queue.getName(), queue.getOverflowCount(), dropCount, tuple);
    }

    public void checkSerialize(KryoTupleSerializer serializer, AddressedTuple tuple) {
        if (trySerializeLocal) {
            serializer.serialize(tuple.getTuple());
        }
    }

    public WorkerTopologyContext getWorkerTopologyContext() {
        try {
            String codeDir = ConfigUtils.supervisorStormResourcesPath(ConfigUtils.supervisorStormDistRoot(conf, topologyId));
            String pidDir = ConfigUtils.workerPidsRoot(conf, topologyId);
            return new WorkerTopologyContext(systemTopology, topologyConf, taskToComponent, componentToSortedTasks,
                componentToStreamToFields, topologyId, codeDir, pidDir, port, localTaskIds,
                defaultSharedResources,
                userSharedResources, cachedTaskToNodePort, assignmentId);
        } catch (IOException e) {
            throw Utils.wrapInRuntime(e);
        }
    }

    public void runWorkerStartHooks() {
        WorkerTopologyContext workerContext = getWorkerTopologyContext();
        if (topology.is_set_worker_hooks()) {
            for (ByteBuffer hook : topology.get_worker_hooks()) {
                byte[] hookBytes = Utils.toByteArray(hook);
                BaseWorkerHook hookObject = Utils.javaDeserialize(hookBytes, BaseWorkerHook.class);
                hookObject.start(topologyConf, workerContext);
            }
        }
    }

    public void runWorkerShutdownHooks() {
        if (topology.is_set_worker_hooks()) {
            for (ByteBuffer hook : topology.get_worker_hooks()) {
                byte[] hookBytes = Utils.toByteArray(hook);
                BaseWorkerHook hookObject = Utils.javaDeserialize(hookBytes, BaseWorkerHook.class);
                hookObject.shutdown();
            }
        }
    }

    public void closeResources() {
        LOG.info("Shutting down default resources");
        ((ExecutorService) defaultSharedResources.get(WorkerTopologyContext.SHARED_EXECUTOR)).shutdownNow();
        LOG.info("Shut down default resources");
    }

    public boolean areAllConnectionsReady() {
        return cachedNodeToPortSocket.get().values()
            .stream()
            .map(WorkerState::isConnectionReady)
            .reduce((left, right) -> left && right)
            .orElse(true);
    }

    public static boolean isConnectionReady(IConnection connection) {
        return !(connection instanceof ConnectionWithStatus)
            || ((ConnectionWithStatus) connection).status() == ConnectionWithStatus.Status.Ready;
    }

    public Collection<IAutoCredentials> getAutoCredentials() {
        return this.autoCredentials;
    }

    private List<List<Long>> readWorkerExecutors(IStormClusterState stormClusterState, String topologyId, String assignmentId,
                                                 int port) {
        LOG.info("Reading assignments");
        List<List<Long>> executorsAssignedToThisWorker = new ArrayList<>();
        executorsAssignedToThisWorker.add(Constants.SYSTEM_EXECUTOR_ID);
        Map<List<Long>, NodeInfo> executorToNodePort = getLocalAssignment(conf, stormClusterState, topologyId).get_executor_node_port();
        for (Map.Entry<List<Long>, NodeInfo> entry : executorToNodePort.entrySet()) {
            NodeInfo nodeInfo = entry.getValue();
            if (nodeInfo.get_node().equals(assignmentId) && nodeInfo.get_port().iterator().next() == port) {
                executorsAssignedToThisWorker.add(entry.getKey());
            }
        }
        return executorsAssignedToThisWorker;
    }

    private Assignment getLocalAssignment(Map<String, Object> conf, IStormClusterState stormClusterState, String topologyId) {
        if (!ConfigUtils.isLocalMode(conf)) {
            try (SupervisorClient supervisorClient = SupervisorClient.getConfiguredClient(conf, Utils.hostname(),
                    supervisorPort)){
                Assignment assignment = supervisorClient.getClient().getLocalAssignmentForStorm(topologyId);
                return assignment;
            } catch (Throwable tr1) {
                //if any error/exception thrown, fetch it from zookeeper
                return stormClusterState.remoteAssignmentInfo(topologyId, null);
            }
        } else {
            return stormClusterState.remoteAssignmentInfo(topologyId, null);
        }
    }

    private Map<List<Long>, JCQueue> mkReceiveQueueMap(Map<String, Object> topologyConf, Set<List<Long>> executors) {
        Integer recvQueueSize = ObjectReader.getInt(topologyConf.get(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE));
        Integer recvBatchSize = ObjectReader.getInt(topologyConf.get(Config.TOPOLOGY_PRODUCER_BATCH_SIZE));
        Integer overflowLimit = ObjectReader.getInt(topologyConf.get(Config.TOPOLOGY_EXECUTOR_OVERFLOW_LIMIT));

        if (recvBatchSize > recvQueueSize / 2) {
            throw new IllegalArgumentException(Config.TOPOLOGY_PRODUCER_BATCH_SIZE + ":" + recvBatchSize +
                " is greater than half of " + Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE + ":" + recvQueueSize);
        }

        IWaitStrategy backPressureWaitStrategy = IWaitStrategy.createBackPressureWaitStrategy(topologyConf);
        Map<List<Long>, JCQueue> receiveQueueMap = new HashMap<>();
        for (List<Long> executor : executors) {
            receiveQueueMap.put(executor, new JCQueue("receive-queue" + executor.toString(),
                recvQueueSize, overflowLimit, recvBatchSize, backPressureWaitStrategy));

        }
        return receiveQueueMap;
    }

    private Map<String, Object> makeDefaultResources() {
        int threadPoolSize = ObjectReader.getInt(conf.get(Config.TOPOLOGY_WORKER_SHARED_THREAD_POOL_SIZE));
        return ImmutableMap.of(WorkerTopologyContext.SHARED_EXECUTOR, Executors.newFixedThreadPool(threadPoolSize));
    }

    private Map<String, Object> makeUserResources() {
        /* TODO: need to invoke a hook provided by the topology, giving it a chance to create user resources.
        * this would be part of the initialization hook
        * need to separate workertopologycontext into WorkerContext and WorkerUserContext.
        * actually just do it via interfaces. just need to make sure to hide setResource from tasks
        */
        return new HashMap<>();
    }

    private StormTimer mkHaltingTimer(String name) {
        return new StormTimer(name, (thread, exception) -> {
            LOG.error("Error when processing event", exception);
            Utils.exitProcess(20, "Error when processing an event");
        });
    }

    /**
     * @return seq of task ids that receive messages from this worker
     */
    private Set<Integer> workerOutboundTasks() {
        WorkerTopologyContext context = getWorkerTopologyContext();
        Set<String> components = new HashSet<>();
        for (Integer taskId : localTaskIds) {
            for (Map<String, Grouping> value : context.getTargets(context.getComponentId(taskId)).values()) {
                components.addAll(value.keySet());
            }
        }

        Set<Integer> outboundTasks = new HashSet<>();

        for (Map.Entry<String, List<Integer>> entry : Utils.reverseMap(taskToComponent).entrySet()) {
            if (components.contains(entry.getKey())) {
                outboundTasks.addAll(entry.getValue());
            }
        }
        return outboundTasks;
    }

    public void haltWorkerTransfer() {
        workerTransfer.haltTransferThd();
    }

    private static int getMaxTaskId(Map<String, List<Integer>> componentToSortedTasks) {
        int maxTaskId = -1;
        for (List<Integer> integers : componentToSortedTasks.values()) {
            if (!integers.isEmpty()) {
                int tempMax = integers.stream().max(Integer::compareTo).get();
                if (tempMax > maxTaskId) {
                    maxTaskId = tempMax;
                }
            }
        }
        return maxTaskId;
    }

    public JCQueue getTransferQueue() {
        return workerTransfer.getTransferQueue();
    }

    public interface ILocalTransferCallback {
        void transfer(ArrayList<AddressedTuple> tupleBatch);
    }
}

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.execution.scheduler;

import com.facebook.presto.execution.DriverGroupId;
import com.facebook.presto.execution.RemoteTask;
import com.facebook.presto.execution.SqlStageExecution;
import com.facebook.presto.execution.scheduler.FixedSourcePartitionedScheduler.FixedSplitPlacementPolicy;
import com.facebook.presto.metadata.Split;
import com.facebook.presto.spi.Node;
import com.facebook.presto.split.EmptySplit;
import com.facebook.presto.split.SplitSource;
import com.facebook.presto.split.SplitSource.SplitBatch;
import com.facebook.presto.sql.planner.plan.PlanNodeId;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.facebook.presto.execution.scheduler.ScheduleResult.BlockedReason.MIXED_SPLIT_QUEUES_FULL_AND_WAITING_FOR_SOURCE;
import static com.facebook.presto.execution.scheduler.ScheduleResult.BlockedReason.NO_ACTIVE_DRIVER_GROUP;
import static com.facebook.presto.execution.scheduler.ScheduleResult.BlockedReason.SPLIT_QUEUES_FULL;
import static com.facebook.presto.execution.scheduler.ScheduleResult.BlockedReason.WAITING_FOR_SOURCE;
import static com.facebook.presto.spi.StandardErrorCode.NO_NODES_AVAILABLE;
import static com.facebook.presto.util.Failures.checkCondition;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Verify.verify;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.util.concurrent.Futures.nonCancellationPropagating;
import static io.airlift.concurrent.MoreFutures.getFutureValue;
import static io.airlift.concurrent.MoreFutures.whenAnyComplete;
import static java.util.Objects.requireNonNull;

public class SourcePartitionedScheduler
        implements StageScheduler
{
    private enum State
    {
        INITIALIZED,
        // At least one split has been added to pendingSplits set.
        SPLITS_ADDED,
        // All splits from underlying SplitSource has been discovered.
        // No more splits will be added to the pendingSplits set.
        NO_MORE_SPLITS,
        // All splits has been provided to caller of this scheduler.
        // Cleanup operations are done (e.g. drainCompletedDriverGroups has drained all driver groups).
        FINISHED
    }

    private final SqlStageExecution stage;
    private final SplitSource splitSource;
    private final SplitPlacementPolicy splitPlacementPolicy;
    private final int splitBatchSize;
    private final PlanNodeId partitionedNode;
    private final boolean autoDropCompletedDriverGroups;

    private final Map<DriverGroupId, ScheduleGroup> scheduleGroups = new HashMap<>();
    private State state = State.INITIALIZED;

    private SettableFuture<?> whenFinishedOrNewDriverGroupAdded = SettableFuture.create();

    private SourcePartitionedScheduler(
            SqlStageExecution stage,
            PlanNodeId partitionedNode,
            SplitSource splitSource,
            SplitPlacementPolicy splitPlacementPolicy,
            int splitBatchSize,
            boolean autoDropCompletedDriverGroups)
    {
        this.stage = requireNonNull(stage, "stage is null");
        this.partitionedNode = requireNonNull(partitionedNode, "partitionedNode is null");
        this.splitSource = requireNonNull(splitSource, "splitSource is null");
        this.splitPlacementPolicy = requireNonNull(splitPlacementPolicy, "splitPlacementPolicy is null");

        checkArgument(splitBatchSize > 0, "splitBatchSize must be at least one");
        this.splitBatchSize = splitBatchSize;

        this.autoDropCompletedDriverGroups = autoDropCompletedDriverGroups;
    }

    /**
     * Obtains an instance of {@code SourcePartitionedScheduler} suitable for use as a
     * stage scheduler.
     * <p>
     * This returns an all-at-once {@code SourcePartitionedScheduler} that requires
     * minimal management from the caller, which is ideal for use as a stage scheduler.
     */
    public static SourcePartitionedScheduler simpleSourcePartitionedScheduler(
            SqlStageExecution stage,
            PlanNodeId partitionedNode,
            SplitSource splitSource,
            SplitPlacementPolicy splitPlacementPolicy,
            int splitBatchSize)
    {
        SourcePartitionedScheduler result = new SourcePartitionedScheduler(stage, partitionedNode, splitSource, splitPlacementPolicy, splitBatchSize, true);
        result.startDriverGroups(ImmutableList.of(DriverGroupId.notGrouped()));
        return result;
    }

    /**
     * Obtains an instance of {@code SourcePartitionedScheduler} suitable for use
     * in FixedSourcePartitionedScheduler.
     * <p>
     * This returns a {@code SourcePartitionedScheduler} that can be used for a pipeline
     * that is either all-at-once or grouped. However, the caller is responsible initializing
     * the driver groups in this scheduler accordingly.
     * <p>
     * In addition, the caller is required to poll {@link #drainCompletedDriverGroups()}
     * in addition to {@link #schedule()} on the returned object. Otherwise, lifecycle
     * transitioning of the object will not work properly.
     */
    public static SourcePartitionedScheduler managedSourcePartitionedScheduler(
            SqlStageExecution stage,
            PlanNodeId partitionedNode,
            SplitSource splitSource,
            SplitPlacementPolicy splitPlacementPolicy,
            int splitBatchSize)
    {
        return new SourcePartitionedScheduler(stage, partitionedNode, splitSource, splitPlacementPolicy, splitBatchSize, false);
    }

    public synchronized void startDriverGroups(List<DriverGroupId> driverGroupIds)
    {
        if (driverGroupIds.isEmpty()) {
            return;
        }
        checkState(state == State.INITIALIZED || state == State.SPLITS_ADDED);
        for (DriverGroupId driverGroupId : driverGroupIds) {
            scheduleGroups.put(driverGroupId, new ScheduleGroup());
        }
        whenFinishedOrNewDriverGroupAdded.set(null);
        whenFinishedOrNewDriverGroupAdded = SettableFuture.create();
    }

    /**
     * Schedules as much work as possible without blocking.
     * <p>
     * Before setting the finished flag, at least one split is guaranteed to be scheduled.
     * In the event that no ordinary split is available from the underlying SplitSource,
     * a synthesized EmptySplit will be scheduled.
     * This at-least-one-split guarantee is provided on a per-SplitSource basis,
     * not per-DriverGroup basis.
     */
    @Override
    public synchronized ScheduleResult schedule()
    {
        int overallSplitAssignmentCount = 0;
        ImmutableSet.Builder<RemoteTask> overallNewTasks = ImmutableSet.builder();
        List<ListenableFuture<?>> overallBlockedFutures = new ArrayList<>();
        boolean anyBlockedOnPlacements = false;
        boolean anyBlockedOnNextSplitBatch = false;
        boolean anyNotBlocked = false;

        for (Entry<DriverGroupId, ScheduleGroup> entry : scheduleGroups.entrySet()) {
            DriverGroupId driverGroupId = entry.getKey();
            ScheduleGroup scheduleGroup = entry.getValue();
            Set<Split> pendingSplits = scheduleGroup.pendingSplits;

            if (scheduleGroup.state != ScheduleGroupState.DISCOVERING_SPLITS) {
                verify(scheduleGroup.nextSplitBatchFuture == null);
            }
            else if (pendingSplits.isEmpty()) {
                // try to get the next batch
                if (scheduleGroup.nextSplitBatchFuture == null) {
                    scheduleGroup.nextSplitBatchFuture = splitSource.getNextBatch(driverGroupId, splitBatchSize - pendingSplits.size());

                    long start = System.nanoTime();
                    Futures.addCallback(scheduleGroup.nextSplitBatchFuture, new FutureCallback<SplitBatch>()
                    {
                        @Override
                        public void onSuccess(SplitBatch result)
                        {
                            stage.recordGetSplitTime(start);
                        }

                        @Override
                        public void onFailure(Throwable t)
                        {
                        }
                    });
                }

                if (scheduleGroup.nextSplitBatchFuture.isDone()) {
                    SplitBatch nextSplits = getFutureValue(scheduleGroup.nextSplitBatchFuture);
                    scheduleGroup.nextSplitBatchFuture = null;

                    pendingSplits.addAll(nextSplits.getSplits());
                    if (nextSplits.isNoMoreSplits() && scheduleGroup.state == ScheduleGroupState.DISCOVERING_SPLITS) {
                        scheduleGroup.state = ScheduleGroupState.NO_MORE_SPLITS;
                    }
                }
                else {
                    overallBlockedFutures.add(scheduleGroup.nextSplitBatchFuture);
                    anyBlockedOnNextSplitBatch = true;
                    continue;
                }
            }

            Multimap<Node, Split> splitAssignment = ImmutableMultimap.of();
            if (!pendingSplits.isEmpty()) {
                if (!scheduleGroup.placementFuture.isDone()) {
                    continue;
                }

                if (state == State.INITIALIZED) {
                    state = State.SPLITS_ADDED;
                }

                // calculate placements for splits
                SplitPlacementResult splitPlacementResult = splitPlacementPolicy.computeAssignments(pendingSplits);
                splitAssignment = splitPlacementResult.getAssignments();

                // remove splits with successful placements
                pendingSplits.removeAll(splitAssignment.values());
                overallSplitAssignmentCount += splitAssignment.size();

                // if not completed placed, mark scheduleGroup as blocked on placement
                if (!pendingSplits.isEmpty()) {
                    scheduleGroup.placementFuture = splitPlacementResult.getBlocked();
                    overallBlockedFutures.add(scheduleGroup.placementFuture);
                    anyBlockedOnPlacements = true;
                }
            }

            // if no new splits will be assigned, update state and attach completion event
            Map<Node, DriverGroupId> noMoreSplitsNotification = ImmutableMap.of();
            if (pendingSplits.isEmpty() && scheduleGroup.state == ScheduleGroupState.NO_MORE_SPLITS) {
                scheduleGroup.state = ScheduleGroupState.DONE;
                if (driverGroupId.isGrouped()) {
                    Node node = ((FixedSplitPlacementPolicy) splitPlacementPolicy).getNodeForBucket(driverGroupId.getId());
                    noMoreSplitsNotification = ImmutableMap.of(node, driverGroupId);
                }
            }

            // assign the splits with successful placements
            overallNewTasks.addAll(assignSplits(splitAssignment, noMoreSplitsNotification));

            // Assert that "placement future is not done" implies "pendingSplits is not empty".
            // The other way around is not true. One obvious reason is (un)lucky timing, where the placement is unblocked between `computeAssignments` and this line.
            // However, there are other reasons that could lead to this.
            // Note that `computeAssignments` is quite broken:
            // 1. It always returns a completed future when there are no tasks, regardless of whether all nodes are blocked.
            // 2. The returned future will only be completed when a node with an assigned task becomes unblocked. Other nodes don't trigger future completion.
            // As a result, to avoid busy loops caused by 1, we check pendingSplits.isEmpty() instead of placementFuture.isDone() here.
            if (scheduleGroup.nextSplitBatchFuture == null && scheduleGroup.pendingSplits.isEmpty() && scheduleGroup.state != ScheduleGroupState.DONE) {
                anyNotBlocked = true;
            }
        }

        if (autoDropCompletedDriverGroups) {
            drainCompletedDriverGroups();
        }

        // If state is NO_MORE_SPLITS/FINISHED, splitSource.isFinished has previously returned true.
        // Don't check again because splitSource is closed now.
        // If anyBlockedOnNextSplitBatch is true, it means we have not checked out the recently completed nextSplitBatch futures,
        // which may contain recently published splits. Even if splitSource.isFinished returns true, acting on it now would be racy.
        if ((state == State.NO_MORE_SPLITS || state == State.FINISHED) || (!anyBlockedOnNextSplitBatch && splitSource.isFinished())) {
            switch (state) {
                case INITIALIZED:
                    // One may find it more natural to write `if (splitSource.isFinished)` around this switch.
                    // However, that doesn't work because `splitSource.isFinished` invocation may fail after
                    // `splitSource.close` has been invoked.
                    // we have not scheduled a single split so far
                    state = State.SPLITS_ADDED;
                    ScheduleResult emptySplitScheduleResult = scheduleEmptySplit();
                    overallNewTasks.addAll(emptySplitScheduleResult.getNewTasks());
                    overallSplitAssignmentCount++;
                    // fall through
                case SPLITS_ADDED:
                    state = State.NO_MORE_SPLITS;
                    splitSource.close();
                    // fall through
                case NO_MORE_SPLITS:
                    if (!scheduleGroups.isEmpty()) {
                        // we are blocked on split assignment
                        break;
                    }
                    state = State.FINISHED;
                    // fall through
                case FINISHED:
                    return new ScheduleResult(
                            true,
                            overallNewTasks.build(),
                            overallSplitAssignmentCount);
                default:
                    throw new IllegalStateException("SourcePartitionedScheduler expected to be in INITIALIZED or SPLITS_ADDED state but is in " + state);
            }
        }

        if (anyNotBlocked) {
            return new ScheduleResult(false, overallNewTasks.build(), overallSplitAssignmentCount);
        }

        // Only try to finalize task creation when scheduling would block
        overallNewTasks.addAll(finalizeTaskCreationIfNecessary());

        ScheduleResult.BlockedReason blockedReason;
        if (anyBlockedOnNextSplitBatch) {
            blockedReason = anyBlockedOnPlacements ? MIXED_SPLIT_QUEUES_FULL_AND_WAITING_FOR_SOURCE : WAITING_FOR_SOURCE;
        }
        else {
            blockedReason = anyBlockedOnPlacements ? SPLIT_QUEUES_FULL : NO_ACTIVE_DRIVER_GROUP;
        }

        overallBlockedFutures.add(whenFinishedOrNewDriverGroupAdded);
        return new ScheduleResult(
                false,
                overallNewTasks.build(),
                nonCancellationPropagating(whenAnyComplete(overallBlockedFutures)),
                blockedReason,
                overallSplitAssignmentCount);
    }

    @Override
    public void close()
    {
        splitSource.close();
    }

    public synchronized List<DriverGroupId> drainCompletedDriverGroups()
    {
        ImmutableList.Builder<DriverGroupId> result = ImmutableList.builder();
        Iterator<Entry<DriverGroupId, ScheduleGroup>> entryIterator = scheduleGroups.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Entry<DriverGroupId, ScheduleGroup> entry = entryIterator.next();
            if (entry.getValue().state == ScheduleGroupState.DONE) {
                result.add(entry.getKey());
                entryIterator.remove();
            }
        }

        if (scheduleGroups.isEmpty()) {
            if (state == State.NO_MORE_SPLITS) {
                state = State.FINISHED;
                whenFinishedOrNewDriverGroupAdded.set(null);
            }
        }

        return result.build();
    }

    private ScheduleResult scheduleEmptySplit()
    {
        state = State.SPLITS_ADDED;

        List<Node> nodes = splitPlacementPolicy.allNodes();
        checkCondition(!nodes.isEmpty(), NO_NODES_AVAILABLE, "No nodes available to run query");
        Node node = nodes.iterator().next();

        Split emptySplit = new Split(
                splitSource.getConnectorId(),
                splitSource.getTransactionHandle(),
                new EmptySplit(splitSource.getConnectorId()));
        Set<RemoteTask> emptyTask = assignSplits(ImmutableMultimap.of(node, emptySplit), ImmutableMap.of());
        return new ScheduleResult(false, emptyTask, 1);
    }

    private Set<RemoteTask> assignSplits(Multimap<Node, Split> splitAssignment, Map<Node, DriverGroupId> noMoreSplitsNotification)
    {
        ImmutableSet.Builder<RemoteTask> newTasks = ImmutableSet.builder();

        ImmutableSet<Node> nodes = ImmutableSet.<Node>builder()
                .addAll(splitAssignment.keySet())
                .addAll(noMoreSplitsNotification.keySet())
                .build();
        for (Node node : nodes) {
            // source partitioned tasks can only receive broadcast data; otherwise it would have a different distribution
            ImmutableMultimap<PlanNodeId, Split> splits = ImmutableMultimap.<PlanNodeId, Split>builder()
                    .putAll(partitionedNode, splitAssignment.get(node))
                    .build();
            ImmutableMap.Builder<PlanNodeId, DriverGroupId> noMoreSplits = ImmutableMap.builder();
            if (noMoreSplitsNotification.containsKey(node)) {
                noMoreSplits.put(partitionedNode, noMoreSplitsNotification.get(node));
            }
            newTasks.addAll(stage.scheduleSplits(
                    node,
                    splits,
                    noMoreSplits.build()));
        }
        return newTasks.build();
    }

    private Set<RemoteTask> finalizeTaskCreationIfNecessary()
    {
        // only lock down tasks if there is a sub stage that could block waiting for this stage to create all tasks
        if (stage.getFragment().isLeaf()) {
            return ImmutableSet.of();
        }

        splitPlacementPolicy.lockDownNodes();

        Set<Node> scheduledNodes = stage.getScheduledNodes();
        Set<RemoteTask> newTasks = splitPlacementPolicy.allNodes().stream()
                .filter(node -> !scheduledNodes.contains(node))
                .flatMap(node -> stage.scheduleSplits(node, ImmutableMultimap.of(), ImmutableMap.of()).stream())
                .collect(toImmutableSet());

        // notify listeners that we have scheduled all tasks so they can set no more buffers or exchange splits
        stage.transitionToSchedulingSplits();

        return newTasks;
    }

    private static class ScheduleGroup
    {
        public ListenableFuture<SplitBatch> nextSplitBatchFuture = null;
        public ListenableFuture<?> placementFuture = Futures.immediateFuture(null);
        public final Set<Split> pendingSplits = new HashSet<>();
        public ScheduleGroupState state = ScheduleGroupState.DISCOVERING_SPLITS;
    }

    private enum ScheduleGroupState {
        DISCOVERING_SPLITS,
        // All splits from underlying SplitSource has been discovered.
        // No more splits will be added to the pendingSplits set.
        NO_MORE_SPLITS,
        // All splits has been provided to caller of this scheduler.
        // Cleanup operations (e.g. inform caller of noMoreSplits) are done.
        DONE
    }
}

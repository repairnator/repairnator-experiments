package com.hubspot.singularity.scheduler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.mesos.v1.Protos.TaskState;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.hubspot.mesos.json.MesosTaskMonitorObject;
import com.hubspot.mesos.json.MesosTaskStatisticsObject;
import com.hubspot.singularity.MachineState;
import com.hubspot.singularity.SingularityClusterUtilization;
import com.hubspot.singularity.SingularitySlaveUsage;
import com.hubspot.singularity.SingularityTask;
import com.hubspot.singularity.SingularityTaskCurrentUsageWithId;
import com.hubspot.singularity.SingularityTaskId;
import com.hubspot.singularity.SingularityTaskUsage;
import com.hubspot.singularity.data.UsageManager;

public class SingularityUsageTest extends SingularitySchedulerTestBase {

  @Inject
  protected SingularityUsagePoller usagePoller;

  @Inject
  protected SingularityUsageCleanerPoller cleaner;

  @Inject
  protected UsageManager usageManager;

  @Inject
  protected TestingMesosClient mesosClient;

  public SingularityUsageTest() {
    super(false);
  }

  @Test
  public void testUsagePollerSimple() {
    // works with no slaves
    usagePoller.runActionOnPoll();
    cleaner.runActionOnPoll();

    initRequest();
    initFirstDeploy();
    saveAndSchedule(request.toBuilder().setInstances(Optional.of(1)));
    resourceOffers(1);

    SingularityTask firstTask = taskManager.getActiveTasks().get(0);
    String hostname = firstTask.getHostname();

    MesosTaskMonitorObject usage = new MesosTaskMonitorObject(null, null, null, firstTask.getTaskId().getId(), getStatistics(2, 5, 100));
    mesosClient.setSlaveResourceUsage(hostname, Collections.singletonList(usage));
    usagePoller.runActionOnPoll();

    String slaveId = firstTask.getAgentId().getValue();

    List<String> slaves = usageManager.getSlavesWithUsage();

    Assert.assertEquals(1, slaves.size());
    Assert.assertEquals(slaves.get(0), slaveId);

    Assert.assertEquals(0, usageManager.getSlaveUsage(slaveId).get(0).getCpusUsed(), 0);
    Assert.assertEquals(100, usageManager.getSlaveUsage(slaveId).get(0).getMemoryBytesUsed());

    SingularityTaskUsage first = usageManager.getTaskUsage(firstTask.getTaskId().getId()).get(0);

    Assert.assertEquals(2, first.getCpuSeconds(), 0);
    Assert.assertEquals(100, first.getMemoryTotalBytes(), 0);
    Assert.assertEquals(5, first.getTimestamp(), 0);
  }

  @Test
  public void testUsageCleaner() {
    initRequest();
    initFirstDeploy();
    saveAndSchedule(request.toBuilder().setInstances(Optional.of(2)));
    resourceOffers(1);

    List<SingularityTaskId> taskIds = taskManager.getActiveTaskIds();

    String t1 = taskIds.get(0).getId();
    String t2 = taskIds.get(1).getId();

    String slaveId = slaveManager.getObjectIds().get(0);
    String host = slaveManager.getObjects().get(0).getHost();

    MesosTaskMonitorObject t1u1 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(2, 5, 100));
    MesosTaskMonitorObject t2u1 = new MesosTaskMonitorObject(null, null, null, t2, getStatistics(10, 5, 1000));

    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u1, t2u1));

    usagePoller.runActionOnPoll();
    cleaner.runActionOnPoll();

    Assert.assertEquals(2, usageManager.getTasksWithUsage().size());
    Assert.assertEquals(1, usageManager.getSlavesWithUsage().size());

    Assert.assertEquals(1100, usageManager.getAllCurrentSlaveUsage().get(0).getMemoryBytesUsed());

    // kill task one
    statusUpdate(taskManager.getActiveTasks().get(0), TaskState.TASK_KILLED);
    killKilledTasks();

    cleaner.runActionOnPoll();

    Assert.assertEquals(1, usageManager.getTasksWithUsage().size());
    Assert.assertEquals(1, usageManager.getSlavesWithUsage().size());

    slaveManager.changeState(slaveId, MachineState.DEAD, Optional.<String> absent(), Optional.<String> absent());

    cleaner.runActionOnPoll();

    Assert.assertEquals(1, usageManager.getTasksWithUsage().size());
    Assert.assertEquals(0, usageManager.getSlavesWithUsage().size());
  }

  @Test
  public void testUsagePollerComplex() throws InterruptedException {
    initRequest();
    initFirstDeploy();
    saveAndSchedule(request.toBuilder().setInstances(Optional.of(2)));
    resourceOffers(1);

    configuration.setNumUsageToKeep(2);
    configuration.setCheckUsageEveryMillis(1);

    List<SingularityTaskId> taskIds = taskManager.getActiveTaskIds();

    String t1 = taskIds.get(0).getId();
    String t2 = taskIds.get(1).getId();

    String slaveId = slaveManager.getObjectIds().get(0);
    String host = slaveManager.getObjects().get(0).getHost();

    MesosTaskMonitorObject t1u1 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(2, 5, 100));
    MesosTaskMonitorObject t2u1 = new MesosTaskMonitorObject(null, null, null, t2, getStatistics(10, 5, 1000));

    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u1, t2u1));

    usagePoller.runActionOnPoll();
    cleaner.runActionOnPoll();

    Thread.sleep(2);

    // 5 seconds have elapsed, t1 has used 1 CPU the whole time = 5 + 2
    // t2 has used 2.5 CPUs the whole time =
    MesosTaskMonitorObject t1u2 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(7, 10, 125));
    MesosTaskMonitorObject t2u2 = new MesosTaskMonitorObject(null, null, null, t2, getStatistics(22.5, 10, 750));

    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u2, t2u2));

    usagePoller.runActionOnPoll();
    cleaner.runActionOnPoll();

    // check usage now
    Assert.assertEquals(3.5, usageManager.getSlaveUsage(slaveId).get(1).getCpusUsed(), 0);
    Assert.assertEquals(875, usageManager.getSlaveUsage(slaveId).get(1).getMemoryBytesUsed(), 0);
    Assert.assertEquals(2, usageManager.getSlaveUsage(slaveId).get(1).getNumTasks(), 0);

    // check task usage
    Assert.assertEquals(22.5, usageManager.getTaskUsage(t2).get(1).getCpuSeconds(), 0);
    Assert.assertEquals(10, usageManager.getTaskUsage(t2).get(0).getCpuSeconds(), 0);

    Thread.sleep(2);

    MesosTaskMonitorObject t1u3 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(8, 11, 125));
    MesosTaskMonitorObject t2u3 = new MesosTaskMonitorObject(null, null, null, t2, getStatistics(23.5, 11, 1000));

    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u3, t2u3));

    usagePoller.runActionOnPoll();
    cleaner.runActionOnPoll();

    //check that there is only 2 usages

    Assert.assertEquals(2, usageManager.getSlaveUsage(slaveId).size());
    Assert.assertEquals(2, usageManager.getTaskUsage(t1).size());
    Assert.assertEquals(2, usageManager.getTaskUsage(t2).size());

    Assert.assertEquals(22.5, usageManager.getTaskUsage(t2).get(0).getCpuSeconds(), 0);
    Assert.assertEquals(23.5, usageManager.getTaskUsage(t2).get(1).getCpuSeconds(), 0);

    Assert.assertEquals(875, usageManager.getSlaveUsage(slaveId).get(0).getMemoryBytesUsed(), 0);
    Assert.assertEquals(1125, usageManager.getSlaveUsage(slaveId).get(1).getMemoryBytesUsed(), 0);

    Assert.assertEquals(slaveId, usageManager.getAllCurrentSlaveUsage().get(0).getSlaveId());
    Assert.assertEquals(1125, usageManager.getAllCurrentSlaveUsage().get(0).getMemoryBytesUsed());

    List<SingularityTaskCurrentUsageWithId> taskCurrentUsages = usageManager.getTaskCurrentUsages(taskManager.getActiveTaskIds());

    Assert.assertEquals(2, taskCurrentUsages.size());

    List<SingularityTaskId> activeTaskIds = taskManager.getActiveTaskIds();

    for (SingularityTaskCurrentUsageWithId taskCurrentUsage : taskCurrentUsages) {
      activeTaskIds.remove(taskCurrentUsage.getTaskId());
    }

    Assert.assertTrue(activeTaskIds.isEmpty());
  }

  @Test
  public void itTracksClusterUtilizationSimple() {
    initRequest();
    double cpuReserved = 10;
    double memMbReserved = .001;
    initFirstDeployWithResources(cpuReserved, memMbReserved);
    saveAndSchedule(request.toBuilder().setInstances(Optional.of(1)));
    resourceOffers(1);

    SingularityTaskId taskId = taskManager.getActiveTaskIds().get(0);
    String t1 = taskId.getId();
    String host = slaveManager.getObjects().get(0).getHost();

    // used 8 cpu
    MesosTaskMonitorObject t1u1 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(40, taskId.getStartedAt() + 5, 800));
    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u1));
    usagePoller.runActionOnPoll();

    // used 8 cpu
    MesosTaskMonitorObject t1u2 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(80, taskId.getStartedAt() + 10, 850));
    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u2));
    usagePoller.runActionOnPoll();

    Assert.assertTrue("Couldn't find cluster utilization", usageManager.getClusterUtilization().isPresent());

    SingularityClusterUtilization utilization = usageManager.getClusterUtilization().get();

    int taskUsages = usageManager.getTaskUsage(t1).size();
    Assert.assertEquals(2, taskUsages);

    Assert.assertEquals(1, utilization.getRequestUtilizations().size());
    Assert.assertEquals(cpuReserved * taskUsages, utilization.getRequestUtilizations().get(0).getCpuReserved(), 0);
    Assert.assertEquals(memMbReserved * SingularitySlaveUsage.BYTES_PER_MEGABYTE * taskUsages, utilization.getRequestUtilizations().get(0).getMemBytesReserved(), 0);

    Assert.assertEquals(0, utilization.getNumRequestsWithOverUtilizedCpu());
    Assert.assertEquals(1, utilization.getNumRequestsWithUnderUtilizedCpu());
    Assert.assertEquals(1, utilization.getNumRequestsWithUnderUtilizedMemBytes());

    Assert.assertEquals(0, utilization.getAvgOverUtilizedCpu(), 0);
    Assert.assertEquals(2, utilization.getAvgUnderUtilizedCpu(), 0);
    Assert.assertEquals(175, utilization.getAvgUnderUtilizedMemBytes(), 0);

    Assert.assertEquals(0, utilization.getMaxOverUtilizedCpu(), 0);
    Assert.assertEquals(2, utilization.getMaxUnderUtilizedCpu(), 0);
    Assert.assertEquals(requestId, utilization.getMaxUnderUtilizedCpuRequestId());
    Assert.assertEquals(175, utilization.getMaxUnderUtilizedMemBytes());
    Assert.assertEquals(requestId, utilization.getMaxUnderUtilizedMemBytesRequestId());

    Assert.assertEquals(0, utilization.getMinOverUtilizedCpu(), 0);
    Assert.assertEquals(2, utilization.getMinUnderUtilizedCpu(), 0);
    Assert.assertEquals(175, utilization.getMinUnderUtilizedMemBytes());
  }

  @Test
  public void itDoesntIncludePerfectlyUtilizedRequestsInClusterUtilization() {
    initRequest();
    double cpuReserved = 2;
    double memMbReserved = .001;
    initFirstDeployWithResources(cpuReserved, memMbReserved);
    saveAndSchedule(request.toBuilder().setInstances(Optional.of(1)));
    resourceOffers(1);

    SingularityTaskId taskId = taskManager.getActiveTaskIds().get(0);
    String t1 = taskId.getId();
    String host = slaveManager.getObjects().get(0).getHost();

    // 2 cpus used
    MesosTaskMonitorObject t1u1 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(10, taskId.getStartedAt() + 5, 1000));
    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u1));
    usagePoller.runActionOnPoll();

    // 2 cpus used
    MesosTaskMonitorObject t1u2 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(20, taskId.getStartedAt() + 10, 900));
    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u2));
    usagePoller.runActionOnPoll();

    Assert.assertTrue("Couldn't find cluster utilization", usageManager.getClusterUtilization().isPresent());

    SingularityClusterUtilization utilization = usageManager.getClusterUtilization().get();

    int taskUsages = usageManager.getTaskUsage(t1).size();
    Assert.assertEquals(2, taskUsages);

    Assert.assertEquals(1, utilization.getRequestUtilizations().size());
    Assert.assertEquals(cpuReserved * taskUsages, utilization.getRequestUtilizations().get(0).getCpuReserved(), 0);
    Assert.assertEquals(memMbReserved * SingularitySlaveUsage.BYTES_PER_MEGABYTE * taskUsages, utilization.getRequestUtilizations().get(0).getMemBytesReserved(), 0);

    Assert.assertEquals(0, utilization.getNumRequestsWithOverUtilizedCpu());
    Assert.assertEquals(0, utilization.getNumRequestsWithUnderUtilizedCpu());
    Assert.assertEquals(1, utilization.getNumRequestsWithUnderUtilizedMemBytes());

    Assert.assertEquals(0, utilization.getAvgOverUtilizedCpu(), 0);
    Assert.assertEquals(0, utilization.getAvgUnderUtilizedCpu(), 0);
    Assert.assertEquals(50, utilization.getAvgUnderUtilizedMemBytes(), 0);

    Assert.assertEquals(0, utilization.getMaxOverUtilizedCpu(), 0);
    Assert.assertEquals(0, utilization.getMaxUnderUtilizedCpu(), 0);
    Assert.assertEquals(50, utilization.getMaxUnderUtilizedMemBytes());
    Assert.assertEquals(requestId, utilization.getMaxUnderUtilizedMemBytesRequestId());

    Assert.assertEquals(0, utilization.getMinOverUtilizedCpu(), 0);
    Assert.assertEquals(0, utilization.getMinUnderUtilizedCpu(), 0);
    Assert.assertEquals(50, utilization.getMinUnderUtilizedMemBytes());
  }

  @Test
  public void itTracksOverusedCpuInClusterUtilization() {
    initRequest();
    double cpuReserved = 2;
    double memMbReserved = .001;
    initFirstDeployWithResources(cpuReserved, memMbReserved);
    saveAndSchedule(request.toBuilder().setInstances(Optional.of(1)));
    resourceOffers(1);

    SingularityTaskId taskId = taskManager.getActiveTaskIds().get(0);
    String t1 = taskId.getId();
    String host = slaveManager.getObjects().get(0).getHost();

    // 4 cpus used
    MesosTaskMonitorObject t1u1 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(20, taskId.getStartedAt() + 5, 1000));
    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u1));
    usagePoller.runActionOnPoll();

    // 4 cpus used
    MesosTaskMonitorObject t1u2 = new MesosTaskMonitorObject(null, null, null, t1, getStatistics(40, taskId.getStartedAt() + 10, 1000));
    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u2));
    usagePoller.runActionOnPoll();

    Assert.assertTrue("Couldn't find cluster utilization", usageManager.getClusterUtilization().isPresent());

    SingularityClusterUtilization utilization = usageManager.getClusterUtilization().get();

    int taskUsages = usageManager.getTaskUsage(t1).size();
    Assert.assertEquals(2, taskUsages);

    Assert.assertEquals(1, utilization.getRequestUtilizations().size());
    Assert.assertEquals(cpuReserved * taskUsages, utilization.getRequestUtilizations().get(0).getCpuReserved(), 0);
    Assert.assertEquals(memMbReserved * SingularitySlaveUsage.BYTES_PER_MEGABYTE * taskUsages, utilization.getRequestUtilizations().get(0).getMemBytesReserved(), 0);

    Assert.assertEquals(1, utilization.getNumRequestsWithOverUtilizedCpu());
    Assert.assertEquals(0, utilization.getNumRequestsWithUnderUtilizedCpu());
    Assert.assertEquals(0, utilization.getNumRequestsWithUnderUtilizedMemBytes());

    Assert.assertEquals(2, utilization.getAvgOverUtilizedCpu(), 0);
    Assert.assertEquals(0, utilization.getAvgUnderUtilizedCpu(), 0);
    Assert.assertEquals(0, utilization.getAvgUnderUtilizedMemBytes(), 0);

    Assert.assertEquals(2, utilization.getMaxOverUtilizedCpu(), 0);
    Assert.assertEquals(requestId, utilization.getMaxOverUtilizedCpuRequestId());
    Assert.assertEquals(0, utilization.getMaxUnderUtilizedCpu(), 0);
    Assert.assertEquals(0, utilization.getMaxUnderUtilizedMemBytes());

    Assert.assertEquals(2, utilization.getMinOverUtilizedCpu(), 0);
    Assert.assertEquals(0, utilization.getMinUnderUtilizedCpu(), 0);
    Assert.assertEquals(0, utilization.getMinUnderUtilizedMemBytes());
  }

  @Test
  public void itCorrectlyDeletesOldUsage() {
    configuration.setNumUsageToKeep(3);
    configuration.setUsageIntervalMultiplier(3);
    configuration.setCheckUsageEveryMillis(TimeUnit.MINUTES.toMillis(1));
    long now = System.currentTimeMillis();

    // no usages exist, none are deleted
    String taskId = "newTask";
    clearUsages(taskId);

    // 1 usage exists, none are deleted
    taskId = "singleUsage";
    saveTaskUsage(taskId, now);
    clearUsages(taskId);
    Assert.assertEquals(1, usageManager.getTaskUsage(taskId).size());

    // 2 usages exist 1 min apart, none are deleted
    taskId = "twoUsages";
    saveTaskUsage(taskId, now, now + TimeUnit.MINUTES.toMillis(1));
    clearUsages(taskId);
    Assert.assertEquals(2, usageManager.getTaskUsage(taskId).size());

    // x1 (3 min apart) x2 (1 min apart) x3
    // x3 is deleted
    taskId = "threeUsages";
    saveTaskUsage(taskId, now, now + TimeUnit.MINUTES.toMillis(3), now + TimeUnit.MINUTES.toMillis(4));
    clearUsages(taskId);
    Assert.assertEquals(2, usageManager.getTaskUsage(taskId).size());
    Assert.assertEquals(now, (long) usageManager.getTaskUsage(taskId).get(0).getTimestamp());
    Assert.assertEquals(now + TimeUnit.MINUTES.toMillis(3), (long) usageManager.getTaskUsage(taskId).get(1).getTimestamp());

    // x1 (1 min apart) x2 (1 min apart) x3
    // x2 is deleted
    taskId = "threeUsages2";
    saveTaskUsage(taskId, now, now + TimeUnit.MINUTES.toMillis(1), now + TimeUnit.MINUTES.toMillis(2));
    clearUsages(taskId);
    Assert.assertEquals(2, usageManager.getTaskUsage(taskId).size());
    Assert.assertEquals(now, (long) usageManager.getTaskUsage(taskId).get(0).getTimestamp());
    Assert.assertEquals(now + TimeUnit.MINUTES.toMillis(2), (long) usageManager.getTaskUsage(taskId).get(1).getTimestamp());

    // x1 (3 min apart) x2 (3 min apart) x3
    // x1 is deleted
    taskId = "threeUsages3";
    saveTaskUsage(taskId, now, now + TimeUnit.MINUTES.toMillis(3), now + TimeUnit.MINUTES.toMillis(6));
    clearUsages(taskId);
    Assert.assertEquals(2, usageManager.getTaskUsage(taskId).size());
    Assert.assertEquals(now + TimeUnit.MINUTES.toMillis(3), (long) usageManager.getTaskUsage(taskId).get(0).getTimestamp());
    Assert.assertEquals(now + TimeUnit.MINUTES.toMillis(6), (long) usageManager.getTaskUsage(taskId).get(1).getTimestamp());
  }

  @Test
  public void itCorrectlyDeterminesResourcesReservedForRequestsWithMultipleTasks() {
    initRequest();
    double cpuReserved = 10;
    double memMbReserved = .001;
    initFirstDeployWithResources(cpuReserved, memMbReserved);
    saveAndSchedule(request.toBuilder().setInstances(Optional.of(2)));
    resourceOffers(1);

    List<SingularityTaskId> taskIds = taskManager.getActiveTaskIds();
    SingularityTaskId t1 = taskIds.get(0);
    SingularityTaskId t2 = taskIds.get(1);
    String host = slaveManager.getObjects().get(0).getHost();

    // used 6 cpu
    MesosTaskMonitorObject t1u1 = new MesosTaskMonitorObject(null, null, null, t1.getId(), getStatistics(30, t1.getStartedAt() + 5, 800));
    // used 6 cpu
    MesosTaskMonitorObject t2u1 = new MesosTaskMonitorObject(null, null, null, t2.getId(), getStatistics(30, t2.getStartedAt() + 5, 800));

    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u1, t2u1));
    usagePoller.runActionOnPoll();

    // used 8 cpu
    MesosTaskMonitorObject t1u2 = new MesosTaskMonitorObject(null, null, null, t1.getId(), getStatistics(70, t1.getStartedAt() + 10, 850));
    // used 8 cpu
    MesosTaskMonitorObject t2u2 = new MesosTaskMonitorObject(null, null, null, t2.getId(), getStatistics(70, t2.getStartedAt() + 10, 850));

    mesosClient.setSlaveResourceUsage(host, Arrays.asList(t1u2, t2u2));
    usagePoller.runActionOnPoll();

    Assert.assertTrue("Couldn't find cluster utilization", usageManager.getClusterUtilization().isPresent());

    SingularityClusterUtilization utilization = usageManager.getClusterUtilization().get();

    int t1TaskUsages = usageManager.getTaskUsage(t1.getId()).size();
    int t2TaskUsages = usageManager.getTaskUsage(t2.getId()).size();
    Assert.assertEquals(2, t1TaskUsages);
    Assert.assertEquals(2, t2TaskUsages);

    Assert.assertEquals(1, utilization.getRequestUtilizations().size());
    Assert.assertEquals(cpuReserved * (t1TaskUsages + t2TaskUsages), utilization.getRequestUtilizations().get(0).getCpuReserved(), 0);
    Assert.assertEquals(memMbReserved * SingularitySlaveUsage.BYTES_PER_MEGABYTE * (t1TaskUsages + t2TaskUsages), utilization.getRequestUtilizations().get(0).getMemBytesReserved(), 0);
  }


  private MesosTaskStatisticsObject getStatistics(double cpuSecs, double timestamp, long memBytes) {
    return new MesosTaskStatisticsObject(1, 0L, 0L, 0, 0, cpuSecs, 0L, 0L, 0L, 0L, 0L, memBytes, timestamp);
  }

  private void saveTaskUsage(String taskId, long... times) {
    for (long time : times) {
      usageManager.saveSpecificTaskUsage(taskId, new SingularityTaskUsage(0, time, 0));
    }
  }

  private void clearUsages(String taskId) {
    usagePoller.clearOldUsage(usageManager.getTaskUsage(taskId), taskId);
  }
}

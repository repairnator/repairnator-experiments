/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.indexing.overlord;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import org.apache.druid.client.indexing.IndexingService;
import org.apache.druid.curator.discovery.ServiceAnnouncer;
import org.apache.druid.discovery.DruidLeaderSelector;
import org.apache.druid.guice.annotations.Self;
import org.apache.druid.indexing.common.actions.TaskActionClient;
import org.apache.druid.indexing.common.actions.TaskActionClientFactory;
import org.apache.druid.indexing.common.task.Task;
import org.apache.druid.indexing.overlord.autoscaling.ScalingStats;
import org.apache.druid.indexing.overlord.config.TaskQueueConfig;
import org.apache.druid.indexing.overlord.helpers.OverlordHelperManager;
import org.apache.druid.indexing.overlord.supervisor.SupervisorManager;
import org.apache.druid.java.util.common.lifecycle.Lifecycle;
import org.apache.druid.java.util.common.lifecycle.LifecycleStart;
import org.apache.druid.java.util.common.lifecycle.LifecycleStop;
import org.apache.druid.java.util.emitter.EmittingLogger;
import org.apache.druid.java.util.emitter.service.ServiceEmitter;
import org.apache.druid.server.DruidNode;
import org.apache.druid.server.coordinator.CoordinatorOverlordServiceConfig;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Encapsulates the indexer leadership lifecycle.
 */
public class TaskMaster
{
  private final DruidLeaderSelector overlordLeaderSelector;
  private final DruidLeaderSelector.Listener leadershipListener;

  private final ReentrantLock giant = new ReentrantLock(true);
  private final TaskActionClientFactory taskActionClientFactory;
  private final SupervisorManager supervisorManager;

  private final AtomicReference<Lifecycle> leaderLifecycleRef = new AtomicReference<>(null);

  private volatile TaskRunner taskRunner;
  private volatile TaskQueue taskQueue;

  private static final EmittingLogger log = new EmittingLogger(TaskMaster.class);

  @Inject
  public TaskMaster(
      final TaskQueueConfig taskQueueConfig,
      final TaskLockbox taskLockbox,
      final TaskStorage taskStorage,
      final TaskActionClientFactory taskActionClientFactory,
      @Self final DruidNode selfNode,
      final TaskRunnerFactory runnerFactory,
      final ServiceAnnouncer serviceAnnouncer,
      final CoordinatorOverlordServiceConfig coordinatorOverlordServiceConfig,
      final ServiceEmitter emitter,
      final SupervisorManager supervisorManager,
      final OverlordHelperManager overlordHelperManager,
      @IndexingService final DruidLeaderSelector overlordLeaderSelector
  )
  {
    this.supervisorManager = supervisorManager;
    this.taskActionClientFactory = taskActionClientFactory;

    this.overlordLeaderSelector = overlordLeaderSelector;

    final DruidNode node = coordinatorOverlordServiceConfig.getOverlordService() == null ? selfNode :
                           selfNode.withService(coordinatorOverlordServiceConfig.getOverlordService());

    this.leadershipListener = new DruidLeaderSelector.Listener()
    {
      @Override
      public void becomeLeader()
      {
        giant.lock();

        // I AM THE MASTER OF THE UNIVERSE.
        log.info("By the power of Grayskull, I have the power!");

        try {
          taskLockbox.syncFromStorage();
          taskRunner = runnerFactory.build();
          taskQueue = new TaskQueue(
              taskQueueConfig,
              taskStorage,
              taskRunner,
              taskActionClientFactory,
              taskLockbox,
              emitter
          );

          // Sensible order to start stuff:
          final Lifecycle leaderLifecycle = new Lifecycle();
          if (leaderLifecycleRef.getAndSet(leaderLifecycle) != null) {
            log.makeAlert("TaskMaster set a new Lifecycle without the old one being cleared!  Race condition")
               .emit();
          }

          leaderLifecycle.addManagedInstance(taskRunner);
          leaderLifecycle.addManagedInstance(taskQueue);
          leaderLifecycle.addManagedInstance(supervisorManager);
          leaderLifecycle.addManagedInstance(overlordHelperManager);

          leaderLifecycle.addHandler(
              new Lifecycle.Handler()
              {
                @Override
                public void start()
                {
                  serviceAnnouncer.announce(node);
                }

                @Override
                public void stop()
                {
                  serviceAnnouncer.unannounce(node);
                }
              }
          );

          leaderLifecycle.start();
        }
        catch (Exception e) {
          throw Throwables.propagate(e);
        }
        finally {
          giant.unlock();
        }
      }

      @Override
      public void stopBeingLeader()
      {
        giant.lock();
        try {
          final Lifecycle leaderLifecycle = leaderLifecycleRef.getAndSet(null);
          if (leaderLifecycle != null) {
            leaderLifecycle.stop();
          }
        }
        finally {
          giant.unlock();
        }
      }
    };
  }

  /**
   * Starts waiting for leadership. Should only be called once throughout the life of the program.
   */
  @LifecycleStart
  public void start()
  {
    giant.lock();

    try {
      overlordLeaderSelector.registerListener(leadershipListener);
    }
    finally {
      giant.unlock();
    }
  }

  /**
   * Stops forever (not just this particular leadership session). Should only be called once throughout the life of
   * the program.
   */
  @LifecycleStop
  public void stop()
  {
    giant.lock();

    try {
      overlordLeaderSelector.unregisterListener();
    }
    finally {
      giant.unlock();
    }
  }

  public boolean isLeader()
  {
    return overlordLeaderSelector.isLeader();
  }

  public String getCurrentLeader()
  {
    return overlordLeaderSelector.getCurrentLeader();
  }

  public Optional<TaskRunner> getTaskRunner()
  {
    if (overlordLeaderSelector.isLeader()) {
      return Optional.of(taskRunner);
    } else {
      return Optional.absent();
    }
  }

  public Optional<TaskQueue> getTaskQueue()
  {
    if (overlordLeaderSelector.isLeader()) {
      return Optional.of(taskQueue);
    } else {
      return Optional.absent();
    }
  }

  public Optional<TaskActionClient> getTaskActionClient(Task task)
  {
    if (overlordLeaderSelector.isLeader()) {
      return Optional.of(taskActionClientFactory.create(task));
    } else {
      return Optional.absent();
    }
  }

  public Optional<ScalingStats> getScalingStats()
  {
    if (overlordLeaderSelector.isLeader()) {
      return taskRunner.getScalingStats();
    } else {
      return Optional.absent();
    }
  }

  public Optional<SupervisorManager> getSupervisorManager()
  {
    if (overlordLeaderSelector.isLeader()) {
      return Optional.of(supervisorManager);
    } else {
      return Optional.absent();
    }
  }
}

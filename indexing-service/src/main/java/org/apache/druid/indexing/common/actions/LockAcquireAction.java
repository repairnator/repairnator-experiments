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

package org.apache.druid.indexing.common.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.apache.druid.indexing.common.TaskLock;
import org.apache.druid.indexing.common.TaskLockType;
import org.apache.druid.indexing.common.task.Task;
import org.apache.druid.indexing.overlord.LockResult;
import org.joda.time.Interval;

import javax.annotation.Nullable;

public class LockAcquireAction implements TaskAction<TaskLock>
{
  private final TaskLockType type;

  @JsonIgnore
  private final Interval interval;

  @JsonIgnore
  private final long timeoutMs;

  @JsonCreator
  public LockAcquireAction(
      @JsonProperty("lockType") @Nullable TaskLockType type, // nullable for backward compatibility
      @JsonProperty("interval") Interval interval,
      @JsonProperty("timeoutMs") long timeoutMs
  )
  {
    this.type = type == null ? TaskLockType.EXCLUSIVE : type;
    this.interval = Preconditions.checkNotNull(interval, "interval");
    this.timeoutMs = timeoutMs;
  }

  @JsonProperty("lockType")
  public TaskLockType getType()
  {
    return type;
  }

  @JsonProperty
  public Interval getInterval()
  {
    return interval;
  }

  @JsonProperty
  public long getTimeoutMs()
  {
    return timeoutMs;
  }

  @Override
  public TypeReference<TaskLock> getReturnTypeReference()
  {
    return new TypeReference<TaskLock>()
    {
    };
  }

  @Override
  public TaskLock perform(Task task, TaskActionToolbox toolbox)
  {
    try {
      final LockResult result = timeoutMs == 0 ?
                                toolbox.getTaskLockbox().lock(type, task, interval) :
                                toolbox.getTaskLockbox().lock(type, task, interval, timeoutMs);
      return result.isOk() ? result.getTaskLock() : null;
    }
    catch (InterruptedException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public boolean isAudited()
  {
    return false;
  }

  @Override
  public String toString()
  {
    return "LockAcquireAction{" +
           "lockType=" + type +
           ", interval=" + interval +
           ", timeoutMs=" + timeoutMs +
           '}';
  }
}

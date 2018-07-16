/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.core.query.scheduler;

/**
 * Maps query request to scheduler group based on table name.
 * This maintains per table query queues.
 */
public class TableBasedGroupMapper implements SchedulerGroupMapper {

  /**
   * Maps query to per-table {@link SchedulerGroup}
   * @param query
   * @return table name (per-table) SchedulerGroup
   */
  @Override
  public String getSchedulerGroupName(SchedulerQueryContext query) {
    return query.getQueryRequest().getTableName();
  }
}

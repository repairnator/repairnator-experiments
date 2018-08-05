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

package io.druid.client.selector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import io.druid.timeline.DataSegment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ConnectionCountServerSelectorStrategy implements ServerSelectorStrategy
{
  private static final Comparator<QueryableDruidServer> comparator =
      Comparator.comparingInt(s -> s.getClient().getNumOpenConnections());

  @Override
  public QueryableDruidServer pick(Set<QueryableDruidServer> servers, DataSegment segment)
  {
    return Collections.min(servers, comparator);
  }

  @Override
  public List<QueryableDruidServer> pick(
      Set<QueryableDruidServer> servers, DataSegment segment, int numServersToPick
  )
  {
    if (servers.size() <= numServersToPick) {
      return ImmutableList.copyOf(servers);
    }
    return Ordering.from(comparator).leastOf(servers, numServersToPick);
  }
}

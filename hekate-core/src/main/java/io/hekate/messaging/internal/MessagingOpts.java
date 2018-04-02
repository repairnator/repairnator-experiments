/*
 * Copyright 2018 The Hekate Project
 *
 * The Hekate Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.hekate.messaging.internal;

import io.hekate.cluster.ClusterNode;
import io.hekate.cluster.ClusterView;
import io.hekate.failover.FailoverPolicy;
import io.hekate.messaging.loadbalance.LoadBalancer;
import io.hekate.partition.PartitionMapper;

interface MessagingOpts<T> {
    LoadBalancer<T> balancer();

    ClusterView cluster();

    FailoverPolicy failover();

    PartitionMapper partitions();

    long timeout();

    MessagingOpts<T> forSingleNode(ClusterNode node);

    default boolean hasTimeout() {
        return timeout() > 0;
    }
}

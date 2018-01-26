/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.storm.metric.api;

import java.util.Collection;

public interface IClusterMetricsConsumer {
    class ClusterInfo {
        private long timestamp;

        public ClusterInfo(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    class SupervisorInfo {
        private String srcSupervisorHost;
        private String srcSupervisorId;
        private long timestamp;

        public SupervisorInfo(String srcSupervisorHost, String srcSupervisorId, long timestamp) {
            this.srcSupervisorHost = srcSupervisorHost;
            this.srcSupervisorId = srcSupervisorId;
            this.timestamp = timestamp;
        }

        public String getSrcSupervisorHost() {
            return srcSupervisorHost;
        }

        public String getSrcSupervisorId() {
            return srcSupervisorId;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    void prepare(Object registrationArgument);
    void handleDataPoints(ClusterInfo clusterInfo, Collection<DataPoint> dataPoints);
    void handleDataPoints(SupervisorInfo supervisorInfo, Collection<DataPoint> dataPoints);
    void cleanup();
}
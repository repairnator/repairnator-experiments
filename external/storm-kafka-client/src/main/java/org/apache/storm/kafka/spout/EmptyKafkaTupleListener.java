/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */


package org.apache.storm.kafka.spout;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.TopicPartition;
import org.apache.storm.task.TopologyContext;

public final class EmptyKafkaTupleListener implements KafkaTupleListener {

    @Override
    public void open(Map<String, Object> conf, TopologyContext context) { }

    @Override
    public void onEmit(List<Object> tuple, KafkaSpoutMessageId msgId) { }

    @Override
    public void onAck(KafkaSpoutMessageId msgId) { }

    @Override
    public void onPartitionsReassigned(Collection<TopicPartition> topicPartitions) { }

    @Override
    public void onRetry(KafkaSpoutMessageId msgId) { }

    @Override
    public void onMaxRetryReached(KafkaSpoutMessageId msgId) { }

    @Override
    public String toString() {
        return "EmptyKafkaTupleListener";
    }
}

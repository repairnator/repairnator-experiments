/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.streaming.connectors.kafka.internals;

import org.apache.flink.annotation.Internal;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 * A special version of the per-kafka-partition-state that additionally holds
 * a periodic watermark generator (and timestamp extractor) per partition.
 *
 * <p>This class is not thread safe, but it gives volatile access to the current
 * partition watermark ({@link #getCurrentPartitionWatermark()}).
 *
 * @param <T> The type of records handled by the watermark generator
 * @param <KPH> The type of the Kafka partition descriptor, which varies across Kafka versions
 */
@Internal
public final class KafkaTopicPartitionStateWithPunctuatedWatermarks<T, KPH> extends KafkaTopicPartitionState<KPH> {

	/** The timestamp assigner and watermark generator for the partition. */
	private final AssignerWithPunctuatedWatermarks<T> timestampsAndWatermarks;

	/** The last watermark timestamp generated by this partition. */
	private volatile long partitionWatermark;

	// ------------------------------------------------------------------------

	public KafkaTopicPartitionStateWithPunctuatedWatermarks(
			KafkaTopicPartition partition, KPH kafkaPartitionHandle,
			AssignerWithPunctuatedWatermarks<T> timestampsAndWatermarks) {
		super(partition, kafkaPartitionHandle);

		this.timestampsAndWatermarks = timestampsAndWatermarks;
		this.partitionWatermark = Long.MIN_VALUE;
	}

	// ------------------------------------------------------------------------

	public long getTimestampForRecord(T record, long kafkaEventTimestamp) {
		return timestampsAndWatermarks.extractTimestamp(record, kafkaEventTimestamp);
	}

	@Nullable
	public Watermark checkAndGetNewWatermark(T record, long timestamp) {
		Watermark mark = timestampsAndWatermarks.checkAndGetNextWatermark(record, timestamp);
		if (mark != null && mark.getTimestamp() > partitionWatermark) {
			partitionWatermark = mark.getTimestamp();
			return mark;
		}
		else {
			return null;
		}
	}

	public long getCurrentPartitionWatermark() {
		return partitionWatermark;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return "KafkaTopicPartitionStateWithPunctuatedWatermarks: partition=" + getKafkaTopicPartition()
				+ ", offset=" + getOffset() + ", watermark=" + partitionWatermark;
	}
}

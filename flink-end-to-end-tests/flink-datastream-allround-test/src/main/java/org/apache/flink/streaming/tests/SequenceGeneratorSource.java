/*
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

package org.apache.flink.streaming.tests;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.util.Preconditions;
import org.apache.flink.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This source function generates a sequence of long values per key.
 */
public class SequenceGeneratorSource extends RichParallelSourceFunction<Event> implements CheckpointedFunction {

	private static final long serialVersionUID = -3986989644799442178L;

	private static final Logger LOG = LoggerFactory.getLogger(SequenceGeneratorSource.class);

	/** Length of the artificial payload string generated for each event. */
	private final int payloadLength;

	/** The size of the total key space, i.e. the number of unique keys generated by all parallel sources. */
	private final int totalKeySpaceSize;

	/** This determines how much the event time progresses for each generated element. */
	private final long eventTimeClockProgressPerEvent;

	/** Maximum generated deviation in event time from the current event time clock. */
	private final long maxOutOfOrder;

	/** Time that a sleep takes in milliseconds. A value < 1 deactivates sleeping. */
	private final long sleepTime;

	/** This determines after how many generated events we sleep. A value < 1 deactivates sleeping. */
	private final long sleepAfterElements;

	/** This holds the key ranges for which this source generates events. */
	private transient List<KeyRangeStates> keyRanges;

	/** This is used to snapshot the state of this source, one entry per key range. */
	private transient ListState<KeyRangeStates> snapshotKeyRanges;

	/** Flag that determines if this source is running, i.e. generating events. */
	private volatile boolean running;

	SequenceGeneratorSource(
		int totalKeySpaceSize,
		int payloadLength,
		long maxOutOfOrder,
		long eventTimeClockProgressPerEvent,
		long sleepTime,
		long sleepAfterElements) {

		this.totalKeySpaceSize = totalKeySpaceSize;
		this.maxOutOfOrder = maxOutOfOrder;
		this.payloadLength = payloadLength;
		this.eventTimeClockProgressPerEvent = eventTimeClockProgressPerEvent;
		this.sleepTime = sleepTime;
		this.sleepAfterElements = sleepTime > 0 ? sleepAfterElements : 0;
		this.running = true;
	}

	@Override
	public void run(SourceContext<Event> ctx) throws Exception {

		Random random = new Random();

		// this holds the current event time, from which generated events can up to +/- (maxOutOfOrder).
		long monotonousEventTime = 0L;
		long elementsBeforeSleep = sleepAfterElements;

		while (running) {

			KeyRangeStates randomKeyRangeStates = keyRanges.get(random.nextInt(keyRanges.size()));
			int randomKey = randomKeyRangeStates.getRandomKey(random);

			long eventTime = Math.max(
				0,
				generateEventTimeWithOutOfOrderness(random, monotonousEventTime));

			// uptick the event time clock
			monotonousEventTime += eventTimeClockProgressPerEvent;

			synchronized (ctx.getCheckpointLock()) {
				long value = randomKeyRangeStates.incrementAndGet(randomKey);

				Event event = new Event(
					randomKey,
					eventTime,
					value,
					StringUtils.getRandomString(random, payloadLength, payloadLength, 'A', 'z'));

				ctx.collect(event);
			}

			if (sleepTime > 0) {
				if (elementsBeforeSleep == 1) {
					elementsBeforeSleep = sleepAfterElements;
					Thread.sleep(sleepTime);
				} else if (elementsBeforeSleep > 1) {
					--elementsBeforeSleep;
				}
			}
		}
	}

	private long generateEventTimeWithOutOfOrderness(Random random, long correctTime) {
		return correctTime - maxOutOfOrder + ((random.nextLong() & Long.MAX_VALUE) % (2 * maxOutOfOrder));
	}

	@Override
	public void cancel() {
		running = false;
	}

	@Override
	public void snapshotState(FunctionSnapshotContext context) throws Exception {
		snapshotKeyRanges.update(keyRanges);
	}

	@Override
	public void initializeState(FunctionInitializationContext context) throws Exception {
		final RuntimeContext runtimeContext = getRuntimeContext();
		final int subtaskIdx = runtimeContext.getIndexOfThisSubtask();
		final int parallelism = runtimeContext.getNumberOfParallelSubtasks();
		final int maxParallelism = runtimeContext.getMaxNumberOfParallelSubtasks();

		ListStateDescriptor<KeyRangeStates> stateDescriptor =
			new ListStateDescriptor<>("keyRanges", KeyRangeStates.class);

		snapshotKeyRanges = context.getOperatorStateStore().getListState(stateDescriptor);
		keyRanges = new ArrayList<>();

		if (context.isRestored()) {
			// restore key ranges from the snapshot
			for (KeyRangeStates keyRange : snapshotKeyRanges.get()) {
				keyRanges.add(keyRange);
			}
		} else {
			// determine the key ranges that belong to the subtask
			int rangeStartIdx = (subtaskIdx * maxParallelism) / parallelism;
			int rangeEndIdx = ((subtaskIdx + 1) * maxParallelism) / parallelism;

			for (int i = rangeStartIdx; i < rangeEndIdx; ++i) {

				int start = ((i * totalKeySpaceSize + maxParallelism - 1) / maxParallelism);
				int end = 1 + ((i + 1) * totalKeySpaceSize - 1) / maxParallelism;

				if (end - start > 0) {
					keyRanges.add(new KeyRangeStates(start, end));
				}
			}
		}
	}

	/**
	 * This defines the key-range and the current sequence numbers for all keys in the range.
	 */
	private static class KeyRangeStates {

		/** Start key of the range (inclusive). */
		final int startKey;

		/** Start key of the range (exclusive). */
		final int endKey;

		/** This array contains the current sequence number for each key in the range. */
		final long[] statesPerKey;

		KeyRangeStates(int startKey, int endKey) {
			this(startKey, endKey, new long[endKey - startKey]);
		}

		KeyRangeStates(int startKey, int endKey, long[] statesPerKey) {
			Preconditions.checkArgument(statesPerKey.length == endKey - startKey);
			this.startKey = startKey;
			this.endKey = endKey;
			this.statesPerKey = statesPerKey;
		}

		/**
		 * Increments and returns the current sequence number for the given key.
		 */
		long incrementAndGet(int key) {
			return ++statesPerKey[key - startKey];
		}

		/**
		 * Returns a random key that belongs to this key range.
		 */
		int getRandomKey(Random random) {
			return random.nextInt(endKey - startKey) + startKey;
		}

		@Override
		public String toString() {
			return "KeyRangeStates{" +
				"start=" + startKey +
				", end=" + endKey +
				", statesPerKey=" + Arrays.toString(statesPerKey) +
				'}';
		}
	}
}

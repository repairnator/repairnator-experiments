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

package org.apache.flink.streaming.api.functions.sink.filesystem.rolling.policies;

import org.apache.flink.streaming.api.functions.sink.filesystem.PartFileInfo;
import org.apache.flink.streaming.api.functions.sink.filesystem.RollingPolicy;

/**
 * A {@link RollingPolicy} which rolls on every checkpoint.
 */
public class OnCheckpointRollingPolicy<IN, BucketID> implements RollingPolicy<IN, BucketID> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean shouldRollOnCheckpoint(PartFileInfo<BucketID> partFileState) {
		return true;
	}

	@Override
	public boolean shouldRollOnEvent(PartFileInfo<BucketID> partFileState, IN element) {
		return false;
	}

	@Override
	public boolean shouldRollOnProcessingTime(PartFileInfo<BucketID> partFileState, long currentTime) {
		return false;
	}
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.runtime.state.ttl;

import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.core.memory.DataInputViewStreamWrapper;
import org.apache.flink.runtime.state.StateSnapshotTransformer;
import org.apache.flink.runtime.state.StateSnapshotTransformer.CollectionStateSnapshotTransformer;
import org.apache.flink.util.FlinkRuntimeException;
import org.apache.flink.util.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

/** State snapshot filter of expired values with TTL. */
abstract class TtlStateSnapshotTransformer<T> implements CollectionStateSnapshotTransformer<T> {
	private final TtlTimeProvider ttlTimeProvider;
	final long ttl;

	TtlStateSnapshotTransformer(@Nonnull TtlTimeProvider ttlTimeProvider, long ttl) {
		this.ttlTimeProvider = ttlTimeProvider;
		this.ttl = ttl;
	}

	<V> TtlValue<V> filterTtlValue(TtlValue<V> value) {
		return expired(value) ? null : value;
	}

	private boolean expired(TtlValue<?> ttlValue) {
		return expired(ttlValue.getLastAccessTimestamp());
	}

	boolean expired(long ts) {
		return TtlUtils.expired(ts, ttl, ttlTimeProvider);
	}

	private static long deserializeTs(
		byte[] value, int offest) throws IOException {
		return LongSerializer.INSTANCE.deserialize(
			new DataInputViewStreamWrapper(new ByteArrayInputStream(value, offest, 8)));
	}

	@Override
	public TransformStrategy getFilterStrategy() {
		return TransformStrategy.STOP_ON_FIRST_INCLUDED;
	}

	static class TtlDeserializedValueStateSnapshotTransformer<T> extends TtlStateSnapshotTransformer<TtlValue<T>> {
		TtlDeserializedValueStateSnapshotTransformer(TtlTimeProvider ttlTimeProvider, long ttl) {
			super(ttlTimeProvider, ttl);
		}

		@Override
		@Nullable
		public TtlValue<T> filterOrTransform(@Nonnull TtlValue<T> value) {
			return filterTtlValue(value);
		}
	}

	static class TtlSerializedValueStateSnapshotTransformer extends TtlStateSnapshotTransformer<byte[]> {
		TtlSerializedValueStateSnapshotTransformer(TtlTimeProvider ttlTimeProvider, long ttl) {
			super(ttlTimeProvider, ttl);
		}

		@Override
		@Nullable
		public byte[] filterOrTransform(@Nonnull byte[] value) {
			Preconditions.checkArgument(value.length >= 8);
			long ts;
			try {
				ts = deserializeTs(value, value.length - Long.BYTES);
			} catch (IOException e) {
				throw new FlinkRuntimeException("Unexpected timestamp deserialization failure");
			}
			return expired(ts) ? null : value;
		}
	}

	static class Factory<T> implements StateSnapshotTransformFactory<TtlValue<T>> {
		private final TtlTimeProvider ttlTimeProvider;
		private final long ttl;

		Factory(@Nonnull TtlTimeProvider ttlTimeProvider, long ttl) {
			this.ttlTimeProvider = ttlTimeProvider;
			this.ttl = ttl;
		}

		@Override
		public Optional<StateSnapshotTransformer<TtlValue<T>>> createForDeserializedState() {
			return Optional.of(new TtlDeserializedValueStateSnapshotTransformer<>(ttlTimeProvider, ttl));
		}

		@Override
		public Optional<StateSnapshotTransformer<byte[]>> createForSerializedState() {
			return Optional.of(new TtlSerializedValueStateSnapshotTransformer(ttlTimeProvider, ttl));
		}
	}
}

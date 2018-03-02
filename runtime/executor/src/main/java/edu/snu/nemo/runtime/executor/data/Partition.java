/*
 * Copyright (C) 2017 Seoul National University
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
package edu.snu.nemo.runtime.executor.data;

/**
 * A collection of data elements.
 * This is a unit of read / write towards {@link edu.snu.nemo.runtime.executor.data.block.Block}s.
 * @param <T> the type of the data stored in this {@link Partition}.
 * @param <K> the type of key used for {@link Partition}.
 */
public interface Partition<T, K> {

  /**
   * @return the key value.
   */
  K getKey();

  /**
   * @return whether the data in this {@link Partition} is serialized or not.
   */
  boolean isSerialized();

  /**
   * @return the data in this {@link Partition}.
   */
  T getData();
}

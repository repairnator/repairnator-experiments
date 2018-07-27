/*
 * Copyright 2018-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.primitive.service;

import io.atomix.storage.buffer.BufferOutput;

/**
 * Backup output.
 */
public interface BackupOutput extends BufferOutput<BackupOutput> {

  /**
   * Serializes an object to the output.
   *
   * @param object the object to serialize
   * @param <U> the object type
   * @return the backup output
   */
  <U> BackupOutput writeObject(U object);

}

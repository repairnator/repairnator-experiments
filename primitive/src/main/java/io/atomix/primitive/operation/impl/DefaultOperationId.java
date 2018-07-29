/*
 * Copyright 2017-present Open Networking Foundation
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
package io.atomix.primitive.operation.impl;

import io.atomix.primitive.operation.OperationId;
import io.atomix.primitive.operation.OperationType;
import io.atomix.utils.AbstractIdentifier;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Raft operation identifier.
 */
public class DefaultOperationId extends AbstractIdentifier<String> implements OperationId {
  private final OperationType type;

  protected DefaultOperationId() {
    this.type = null;
  }

  public DefaultOperationId(String id, OperationType type) {
    super(id);
    this.type = type;
  }

  /**
   * Returns the operation type.
   *
   * @return the operation type
   */
  public OperationType type() {
    return type;
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .add("id", id())
        .add("type", type())
        .toString();
  }
}

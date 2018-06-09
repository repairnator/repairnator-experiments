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
package io.atomix.protocols.backup.protocol;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Restore request.
 */
public class RestoreRequest extends PrimitiveRequest {

  public static RestoreRequest request(PrimitiveDescriptor primitive, long term) {
    return new RestoreRequest(primitive, term);
  }

  private final long term;

  public RestoreRequest(PrimitiveDescriptor primitive, long term) {
    super(primitive);
    this.term = term;
  }

  public long term() {
    return term;
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .add("primitive", primitive())
        .add("term", term())
        .toString();
  }
}

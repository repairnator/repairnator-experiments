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
package io.atomix.core.collection;

import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.PrimitiveType;
import io.atomix.primitive.service.PrimitiveService;
import io.atomix.primitive.service.ServiceConfig;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Distributed collection primitive type.
 */
public class DistributedCollectionType<E> implements PrimitiveType<DistributedCollectionBuilder, DistributedCollectionConfig, DistributedCollection<E>> {
  private static final String NAME = "collection";
  private static final DistributedCollectionType INSTANCE = new DistributedCollectionType();

  /**
   * Returns a new distributed collection type.
   *
   * @param <E> the collection element type
   * @return a new distributed collection type
   */
  @SuppressWarnings("unchecked")
  public static <E> DistributedCollectionType<E> instance() {
    return INSTANCE;
  }

  @Override
  public String name() {
    return NAME;
  }

  @Override
  public PrimitiveService newService(ServiceConfig config) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DistributedCollectionConfig newConfig() {
    throw new UnsupportedOperationException();
  }

  @Override
  public DistributedCollectionBuilder newBuilder(String primitiveName, DistributedCollectionConfig config, PrimitiveManagementService managementService) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .add("name", name())
        .toString();
  }
}
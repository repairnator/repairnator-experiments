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
package io.atomix.primitive;

import java.util.Collection;

/**
 * Primitive registry.
 */
public interface PrimitiveTypeRegistry {

  /**
   * Returns the collection of registered primitive types.
   *
   * @return the collection of registered primitive types
   */
  Collection<PrimitiveType> getPrimitiveTypes();

  /**
   * Returns the primitive type for the given name.
   *
   * @param typeName the primitive type name
   * @return the primitive type
   */
  PrimitiveType getPrimitiveType(String typeName);

}

/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.druid.segment;

import javax.annotation.Nullable;

/**
 * An abstract class that provides an implementation of {@link #getObject()}.
 * Don't extend this abstract class if you could provide a more efficient implementation for those methods, implement
 * {@link DimensionSelector} directly then.
 */
public abstract class AbstractDimensionSelector implements DimensionSelector
{

  @Nullable
  @Override
  public Object getObject()
  {
    return defaultGetObject();
  }
}

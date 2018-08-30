/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.client.cache;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HybridCacheConfig
{
  @JsonProperty
  private boolean useL2 = true;

  @JsonProperty
  private boolean populateL2 = true;

  public boolean getUseL2()
  {
    return useL2;
  }

  public boolean getPopulateL2()
  {
    return populateL2;
  }

  @Override
  public String toString()
  {
    return "HybridCacheConfig{" +
           "useL2=" + useL2 +
           ", populateL2=" + populateL2 +
           '}';
  }
}

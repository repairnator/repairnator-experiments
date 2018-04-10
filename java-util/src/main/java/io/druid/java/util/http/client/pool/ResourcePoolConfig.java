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

package io.druid.java.util.http.client.pool;

/**
 */
public class ResourcePoolConfig
{
  private final int maxPerKey;
  private final long unusedConnectionTimeoutMillis;

  public ResourcePoolConfig(
      int maxPerKey,
      long unusedConnectionTimeoutMillis
  )
  {
    this.maxPerKey = maxPerKey;
    this.unusedConnectionTimeoutMillis = unusedConnectionTimeoutMillis;
  }

  @Deprecated
  public ResourcePoolConfig(
      int maxPerKey,
      boolean cleanIdle,
      long unusedConnectionTimeoutMillis
  )
  {
    this(maxPerKey, unusedConnectionTimeoutMillis);

    if (cleanIdle) {
      throw new IllegalStateException(
          "Cleaning up idle connections is a bad idea.  "
          + "If your services can't handle the max number then lower the max number."
      );
    }
  }

  public int getMaxPerKey()
  {
    return maxPerKey;
  }

  public boolean isCleanIdle()
  {
    return false;
  }

  public long getUnusedConnectionTimeoutMillis()
  {
    return unusedConnectionTimeoutMillis;
  }
}

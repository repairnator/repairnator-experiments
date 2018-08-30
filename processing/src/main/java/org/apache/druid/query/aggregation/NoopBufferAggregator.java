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

package org.apache.druid.query.aggregation;

import org.apache.druid.query.monomorphicprocessing.RuntimeShapeInspector;

import java.nio.ByteBuffer;

public final class NoopBufferAggregator implements BufferAggregator
{
  private static final NoopBufferAggregator INSTANCE = new NoopBufferAggregator();

  public static NoopBufferAggregator instance()
  {
    return INSTANCE;
  }

  private NoopBufferAggregator()
  {
  }

  @Override
  public void init(ByteBuffer buf, int position)
  {
  }

  @Override
  public void aggregate(ByteBuffer buf, int position)
  {
  }

  @Override
  public Object get(ByteBuffer buf, int position)
  {
    return null;
  }

  @Override
  public float getFloat(ByteBuffer buf, int position)
  {
    return 0;
  }


  @Override
  public long getLong(ByteBuffer buf, int position)
  {
    return 0L;
  }

  @Override
  public double getDouble(ByteBuffer buf, int position)
  {
    return 0d;
  }

  @Override
  public void close()
  {
  }

  @Override
  public void inspectRuntimeShape(RuntimeShapeInspector inspector)
  {
    // nothing to inspect
  }
}

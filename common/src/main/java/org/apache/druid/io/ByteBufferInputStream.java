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

package org.apache.druid.io;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Streams bytes from the {@link ByteBuffer}'s position to the limit as {@link InputStream}.
 */
public final class ByteBufferInputStream extends InputStream
{
  private final ByteBuffer buffer;

  /**
   * Does *not* make a copy of the given buffer, so the position of the given buffer is incremented, as the created
   * InputStream is used.
   */
  public ByteBufferInputStream(ByteBuffer buffer)
  {
    this.buffer = buffer;
  }

  @Override
  public int read()
  {
    if (!buffer.hasRemaining()) {
      return -1;
    }
    return buffer.get() & 0xFF;
  }

  @Override
  public int read(byte[] bytes, int off, int len)
  {
    if (len == 0) {
      return 0;
    }
    if (!buffer.hasRemaining()) {
      return -1;
    }

    len = Math.min(len, buffer.remaining());
    buffer.get(bytes, off, len);
    return len;
  }

  @Override
  public int available()
  {
    return buffer.remaining();
  }
}

/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.core.segment.store;

import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* package-private */
class IndexEntry {
  private static Logger LOGGER = LoggerFactory.getLogger(IndexEntry.class);

  IndexKey key;
  long startOffset = -1;
  long size = -1;
  PinotDataBuffer buffer;

  public IndexEntry(IndexKey key) {
    this.key = key;
  }

  @Override
  public String toString() {
    return key.toString() + " : [" + startOffset + "," + (startOffset + size) + ")";
  }
}


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
package com.linkedin.pinot.core.data.manager.offline;

import com.linkedin.pinot.core.data.manager.SegmentDataManager;
import com.linkedin.pinot.core.indexsegment.immutable.ImmutableSegment;


/**
 * Segment data manager for immutable segment.
 */
public class ImmutableSegmentDataManager extends SegmentDataManager {

  private final ImmutableSegment _immutableSegment;

  public ImmutableSegmentDataManager(ImmutableSegment immutableSegment) {
    _immutableSegment = immutableSegment;
  }

  @Override
  public String getSegmentName() {
    return _immutableSegment.getSegmentName();
  }

  @Override
  public ImmutableSegment getSegment() {
    return _immutableSegment;
  }

  @Override
  public void destroy() {
    _immutableSegment.destroy();
  }

  @Override
  public String toString() {
    return "ImmutableSegmentDataManager(" + _immutableSegment.getSegmentName() + ")";
  }
}

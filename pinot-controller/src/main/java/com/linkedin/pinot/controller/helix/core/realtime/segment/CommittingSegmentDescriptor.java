/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
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

package com.linkedin.pinot.controller.helix.core.realtime.segment;

import com.linkedin.pinot.common.protocols.SegmentCompletionProtocol;


/**
 * Class to hold properties of the committing segment
 */
public class CommittingSegmentDescriptor {
  private String _segmentName;
  private long _segmentSizeBytes;
  private String _segmentLocation;
  private long _nextOffset;

  public static CommittingSegmentDescriptor fromSegmentCompletionReqParams(
      SegmentCompletionProtocol.Request.Params reqParams) {
    CommittingSegmentDescriptor committingSegmentDescriptor =
        new CommittingSegmentDescriptor(reqParams.getSegmentName(), reqParams.getOffset(),
            reqParams.getSegmentSizeBytes());
    committingSegmentDescriptor.setSegmentLocation(reqParams.getSegmentLocation());
    return committingSegmentDescriptor;
  }

  public CommittingSegmentDescriptor(String segmentName, long nextOffset, long segmentSizeBytes) {
    _segmentName = segmentName;
    _nextOffset = nextOffset;
    _segmentSizeBytes = segmentSizeBytes;
  }

  public CommittingSegmentDescriptor(String segmentName, long nextOffset, long segmentSizeBytes, String segmentLocation) {
    this(segmentName, nextOffset, segmentSizeBytes);
    _segmentLocation = segmentLocation;
  }

  public String getSegmentName() {
    return _segmentName;
  }

  public void setSegmentName(String segmentName) {
    _segmentName = segmentName;
  }

  public long getSegmentSizeBytes() {
    return _segmentSizeBytes;
  }

  public void setSegmentSizeBytes(long segmentSizeBytes) {
    _segmentSizeBytes = segmentSizeBytes;
  }

  public String getSegmentLocation() {
    return _segmentLocation;
  }

  public void setSegmentLocation(String segmentLocation) {
    _segmentLocation = segmentLocation;
  }

  public long getNextOffset() {
    return _nextOffset;
  }

  public void setNextOffset(long nextOffset) {
    _nextOffset = nextOffset;
  }
}

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

package org.apache.druid.indexer.hadoop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.druid.timeline.DataSegment;
import org.joda.time.Interval;

import java.util.Objects;

public class WindowedDataSegment
{
  private final DataSegment segment;
  private final Interval interval;

  @JsonCreator
  public WindowedDataSegment(
      @JsonProperty("segment") final DataSegment segment,
      @JsonProperty("interval") final Interval interval
  )
  {
    this.segment = segment;
    this.interval = interval;
  }

  @JsonProperty
  public DataSegment getSegment()
  {
    return segment;
  }

  @JsonProperty
  public Interval getInterval()
  {
    return interval;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WindowedDataSegment that = (WindowedDataSegment) o;
    return Objects.equals(segment, that.segment) &&
           Objects.equals(interval, that.interval);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(segment, interval);
  }

  @Override
  public String toString()
  {
    return "WindowedDataSegment{" +
           "segment=" + segment +
           ", interval=" + interval +
           '}';
  }

  public static WindowedDataSegment of(final DataSegment segment)
  {
    return new WindowedDataSegment(segment, segment.getInterval());
  }
}

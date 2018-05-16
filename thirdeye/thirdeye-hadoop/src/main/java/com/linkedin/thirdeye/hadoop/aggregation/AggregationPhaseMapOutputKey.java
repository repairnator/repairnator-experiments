/**
 * Copyright (C) 2014-2015 LinkedIn Corp. (pinot-core@linkedin.com)
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
package com.linkedin.thirdeye.hadoop.aggregation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.linkedin.thirdeye.hadoop.config.DimensionType;

/**
 * Wrapper for the key generated by mapper in Aggregation
 */
public class AggregationPhaseMapOutputKey {

  private long time;
  private List<Object> dimensionValues;
  private List<DimensionType> dimensionTypes;

  public AggregationPhaseMapOutputKey(long time, List<Object> dimensionValues, List<DimensionType> dimensionTypes) {
    this.time = time;
    this.dimensionValues = dimensionValues;
    this.dimensionTypes = dimensionTypes;
  }

  public long getTime() {
    return time;
  }

  public List<Object> getDimensionValues() {
    return dimensionValues;
  }

  public List<DimensionType> getDimensionTypes() {
    return dimensionTypes;
  }

  /**
   * Converts AggregationPhaseMapOutputKey to bytes buffer
   * @return
   * @throws IOException
   */
  public byte[] toBytes() throws IOException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    // time
    dos.writeLong(time);

    // dimensions size
    dos.writeInt(dimensionValues.size());
    // dimension values
    for (int i = 0; i < dimensionValues.size(); i++) {
      Object dimensionValue = dimensionValues.get(i);
      DimensionType dimensionType = dimensionTypes.get(i);
      DimensionType.writeDimensionValueToOutputStream(dos, dimensionValue, dimensionType);
    }

    baos.close();
    dos.close();
    return baos.toByteArray();
  }

  /**
   * Constructs AggregationPhaseMapOutputKey from bytes buffer
   * @param buffer
   * @param dimensionTypes
   * @return
   * @throws IOException
   */
  public static AggregationPhaseMapOutputKey fromBytes(byte[] buffer, List<DimensionType> dimensionTypes) throws IOException {
    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer));

    // time
    long time = dis.readLong();

    // dimensions size
    int size = dis.readInt();

    // dimension value
    List<Object> dimensionValues = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      DimensionType dimensionType = dimensionTypes.get(i);
      Object dimensionValue = DimensionType.readDimensionValueFromDataInputStream(dis, dimensionType);
      dimensionValues.add(dimensionValue);
    }

    AggregationPhaseMapOutputKey wrapper;
    wrapper = new AggregationPhaseMapOutputKey(time, dimensionValues, dimensionTypes);
    return wrapper;
  }

}

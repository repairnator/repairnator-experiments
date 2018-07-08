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

import io.druid.common.utils.SerializerUtils;
import io.druid.java.util.common.ISE;
import io.druid.segment.data.CompressedColumnarFloatsSupplier;
import io.druid.segment.data.GenericIndexed;
import io.druid.segment.serde.ComplexMetricSerde;
import io.druid.segment.serde.ComplexMetrics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 */
public class MetricHolder
{
  private static final byte[] version = new byte[]{0x0};
  private static final SerializerUtils serializerUtils = new SerializerUtils();

  public static MetricHolder fromByteBuffer(ByteBuffer buf)
  {
    final byte ver = buf.get();
    if (version[0] != ver) {
      throw new ISE("Unknown version[%s] of MetricHolder", ver);
    }

    final String metricName = serializerUtils.readString(buf);
    final String typeName = serializerUtils.readString(buf);
    MetricHolder holder = new MetricHolder(metricName, typeName);

    switch (holder.type) {
      case FLOAT:
        holder.floatType = CompressedColumnarFloatsSupplier.fromByteBuffer(buf, ByteOrder.nativeOrder());
        break;
      case COMPLEX:
        final ComplexMetricSerde serdeForType = ComplexMetrics.getSerdeForType(holder.getTypeName());

        if (serdeForType == null) {
          throw new ISE("Unknown type[%s], cannot load.", holder.getTypeName());
        }

        holder.complexType = read(buf, serdeForType);
        break;
      case LONG:
      case DOUBLE:
        throw new ISE("Unsupported type[%s]", holder.type);
    }

    return holder;
  }

  private static <T> GenericIndexed<T> read(ByteBuffer buf, ComplexMetricSerde serde)
  {
    return GenericIndexed.read(buf, serde.getObjectStrategy());
  }

  public enum MetricType
  {
    LONG,
    FLOAT,
    DOUBLE,
    COMPLEX;

    static MetricType determineType(String typeName)
    {
      if ("long".equalsIgnoreCase(typeName)) {
        return LONG;
      } else if ("float".equalsIgnoreCase(typeName)) {
        return FLOAT;
      } else if ("double".equalsIgnoreCase(typeName)) {
        return DOUBLE;
      }
      return COMPLEX;
    }
  }

  private final String name;
  private final String typeName;
  private final MetricType type;
  CompressedColumnarFloatsSupplier floatType = null;
  GenericIndexed<?> complexType = null;

  private MetricHolder(
      String name,
      String typeName
  )
  {
    this.name = name;
    this.typeName = typeName;
    this.type = MetricType.determineType(typeName);
  }

  public String getName()
  {
    return name;
  }

  public String getTypeName()
  {
    return typeName;
  }

  public MetricType getType()
  {
    return type;
  }

}

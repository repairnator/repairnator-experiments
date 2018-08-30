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

package org.apache.druid.segment.serde;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Supplier;
import org.apache.druid.collections.bitmap.ImmutableBitmap;
import org.apache.druid.java.util.common.io.smoosh.FileSmoosher;
import org.apache.druid.segment.column.ValueType;
import org.apache.druid.segment.data.BitmapSerde;
import org.apache.druid.segment.data.BitmapSerdeFactory;
import org.apache.druid.segment.data.ColumnarDoubles;
import org.apache.druid.segment.data.CompressedColumnarDoublesSuppliers;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.WritableByteChannel;

/**
 */
public class DoubleGenericColumnPartSerdeV2 implements ColumnPartSerde
{
  @JsonCreator
  public static DoubleGenericColumnPartSerdeV2 getDoubleGenericColumnPartSerde(
      @JsonProperty("byteOrder") ByteOrder byteOrder,
      @Nullable @JsonProperty("bitmapSerdeFactory") BitmapSerdeFactory bitmapSerdeFactory
  )
  {
    return new DoubleGenericColumnPartSerdeV2(
        byteOrder,
        bitmapSerdeFactory != null ? bitmapSerdeFactory : new BitmapSerde.LegacyBitmapSerdeFactory(),
        null
    );
  }

  private final ByteOrder byteOrder;
  private Serializer serialize;
  private final BitmapSerdeFactory bitmapSerdeFactory;

  public DoubleGenericColumnPartSerdeV2(
      ByteOrder byteOrder,
      BitmapSerdeFactory bitmapSerdeFactory,
      Serializer serialize
  )
  {
    this.byteOrder = byteOrder;
    this.bitmapSerdeFactory = bitmapSerdeFactory;
    this.serialize = serialize;
  }

  @JsonProperty
  public ByteOrder getByteOrder()
  {
    return byteOrder;
  }

  @JsonProperty
  public BitmapSerdeFactory getBitmapSerdeFactory()
  {
    return bitmapSerdeFactory;
  }

  public static SerializerBuilder serializerBuilder()
  {
    return new SerializerBuilder();
  }

  public static class SerializerBuilder
  {
    private ByteOrder byteOrder = null;
    private Serializer delegate = null;
    private BitmapSerdeFactory bitmapSerdeFactory = null;

    public SerializerBuilder withByteOrder(final ByteOrder byteOrder)
    {
      this.byteOrder = byteOrder;
      return this;
    }

    public SerializerBuilder withDelegate(final Serializer delegate)
    {
      this.delegate = delegate;
      return this;
    }

    public SerializerBuilder withBitmapSerdeFactory(BitmapSerdeFactory bitmapSerdeFactory)
    {
      this.bitmapSerdeFactory = bitmapSerdeFactory;
      return this;
    }

    public DoubleGenericColumnPartSerdeV2 build()
    {
      return new DoubleGenericColumnPartSerdeV2(
          byteOrder,
          bitmapSerdeFactory,
          new Serializer()
          {
            @Override
            public long getSerializedSize() throws IOException
            {
              return delegate.getSerializedSize();
            }

            @Override
            public void writeTo(WritableByteChannel channel, FileSmoosher fileSmoosher) throws IOException
            {
              delegate.writeTo(channel, fileSmoosher);
            }
          }
      );
    }
  }

  @Override
  public Serializer getSerializer()
  {
    return serialize;
  }

  @Override
  public Deserializer getDeserializer()
  {
    return (buffer, builder, columnConfig) -> {
      int offset = buffer.getInt();
      int initialPos = buffer.position();
      final Supplier<ColumnarDoubles> column = CompressedColumnarDoublesSuppliers.fromByteBuffer(
          buffer,
          byteOrder
      );

      buffer.position(initialPos + offset);
      final ImmutableBitmap bitmap;
      if (buffer.hasRemaining()) {
        bitmap = bitmapSerdeFactory.getObjectStrategy().fromByteBufferWithSize(buffer);
      } else {
        bitmap = bitmapSerdeFactory.getBitmapFactory().makeEmptyImmutableBitmap();
      }
      builder.setType(ValueType.DOUBLE)
             .setHasMultipleValues(false)
             .setGenericColumn(new DoubleGenericColumnSupplier(column, bitmap));
    };
  }
}

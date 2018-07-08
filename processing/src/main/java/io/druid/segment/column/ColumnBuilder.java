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

package io.druid.segment.column;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import io.druid.java.util.common.io.smoosh.SmooshedFileMapper;

/**
 */
public class ColumnBuilder
{
  private ValueType type = null;
  private boolean hasMultipleValues = false;

  private Supplier<? extends BaseColumn> columnSupplier = null;
  private boolean dictionaryEncoded = false;
  private Supplier<BitmapIndex> bitmapIndex = null;
  private Supplier<SpatialIndex> spatialIndex = null;
  private SmooshedFileMapper fileMapper = null;

  public ColumnBuilder setFileMapper(SmooshedFileMapper fileMapper)
  {
    this.fileMapper = fileMapper;
    return this;
  }

  public SmooshedFileMapper getFileMapper()
  {
    return this.fileMapper;
  }

  public ColumnBuilder setType(ValueType type)
  {
    this.type = type;
    return this;
  }

  public ColumnBuilder setHasMultipleValues(boolean hasMultipleValues)
  {
    this.hasMultipleValues = hasMultipleValues;
    return this;
  }

  public ColumnBuilder setDictionaryEncodedColumnSupplier(Supplier<? extends DictionaryEncodedColumn<?>> columnSupplier)
  {
    this.columnSupplier = columnSupplier;
    this.dictionaryEncoded = true;
    return this;
  }

  public ColumnBuilder setComplexColumnSupplier(Supplier<? extends ComplexColumn> columnSupplier)
  {
    this.columnSupplier = columnSupplier;
    return this;
  }

  public ColumnBuilder setNumericColumnSupplier(Supplier<? extends NumericColumn> columnSupplier)
  {
    this.columnSupplier = columnSupplier;
    return this;
  }

  public ColumnBuilder setBitmapIndex(Supplier<BitmapIndex> bitmapIndex)
  {
    this.bitmapIndex = bitmapIndex;
    return this;
  }

  public ColumnBuilder setSpatialIndex(Supplier<SpatialIndex> spatialIndex)
  {
    this.spatialIndex = spatialIndex;
    return this;
  }

  public ColumnHolder build()
  {
    Preconditions.checkState(type != null, "Type must be set.");

    return new SimpleColumnHolder(
        new ColumnCapabilitiesImpl()
            .setType(type)
            .setDictionaryEncoded(dictionaryEncoded)
            .setHasBitmapIndexes(bitmapIndex != null)
            .setHasSpatialIndexes(spatialIndex != null)
            .setHasMultipleValues(hasMultipleValues),
        columnSupplier,
        bitmapIndex,
        spatialIndex
    );
  }
}

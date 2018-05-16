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
package com.linkedin.pinot.core.query.aggregation;

/**
 * AggregationResultHolder interface implementation for result type 'object'.
 */
public class ObjectAggregationResultHolder implements AggregationResultHolder {
  Object _value;

  /**
   * {@inheritDoc}
   * @param value
   */
  @Override
  public void setValue(double value) {
    throw new RuntimeException("Method 'setValue' (with double value) not supported for class " + getClass().getName());
  }

  /**
   * {@inheritDoc}
   * @param value
   */
  @Override
  public void setValue(Object value) {
    _value = value;
  }

  /**
   * {@inheritDoc}
   * @return
   */
  @Override
  public double getDoubleResult() {
    throw new RuntimeException("Method 'getDoubleResult' not supported for class " + getClass().getName());
  }

  /**
   * {@inheritDoc}
   * @return
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getResult() {
    return (T) _value;
  }
}

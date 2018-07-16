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
package com.linkedin.pinot.core.operator.transform.transformer.timeunit;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;


/**
 * Implementation of {@link TimeUnitTransformer} to handle time units defined in {@link TimeUnit}.
 */
public class JavaTimeUnitTransformer implements TimeUnitTransformer {
  private final TimeUnit _inputTimeUnit;
  private final TimeUnit _outputTimeUnit;

  public JavaTimeUnitTransformer(@Nonnull TimeUnit inputTimeUnit, @Nonnull TimeUnit outputTimeUnit) {
    _inputTimeUnit = inputTimeUnit;
    _outputTimeUnit = outputTimeUnit;
  }

  @Override
  public void transform(@Nonnull long[] input, @Nonnull long[] output, int length) {
    for (int i = 0; i < length; i++) {
      output[i] = _outputTimeUnit.convert(input[i], _inputTimeUnit);
    }
  }
}

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
package com.linkedin.pinot.core.operator.transform.function;

import com.linkedin.pinot.common.request.transform.TransformExpressionTree;
import com.linkedin.pinot.core.query.exception.BadQueryRequestException;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class DateTimeConversionTransformFunctionTest extends BaseTransformFunctionTest {

  @Test
  public void testDateTimeConversionTransformFunction() {
    // NOTE: functionality of DateTimeConverter is covered in DateTimeConverterTest
    TransformExpressionTree expression = TransformExpressionTree.compileToExpressionTree(
        String.format("dateTimeConvert(%s,'1:MILLISECONDS:EPOCH','1:MINUTES:EPOCH','1:MINUTES')", TIME_COLUMN));
    TransformFunction transformFunction = TransformFunctionFactory.get(expression, _dataSourceMap);
    Assert.assertTrue(transformFunction instanceof DateTimeConversionTransformFunction);
    Assert.assertEquals(transformFunction.getName(), DateTimeConversionTransformFunction.FUNCTION_NAME);
    int[] intValues = transformFunction.transformToIntValuesSV(_projectionBlock);
    long[] longValues = transformFunction.transformToLongValuesSV(_projectionBlock);
    float[] floatValues = transformFunction.transformToFloatValuesSV(_projectionBlock);
    double[] doubleValues = transformFunction.transformToDoubleValuesSV(_projectionBlock);
    String[] stringValues = transformFunction.transformToStringValuesSV(_projectionBlock);
    for (int i = 0; i < NUM_ROWS; i++) {
      long expected = TimeUnit.MILLISECONDS.toMinutes(_timeValues[i]);
      Assert.assertEquals(intValues[i], (int) expected);
      Assert.assertEquals(longValues[i], expected);
      Assert.assertEquals(floatValues[i], (float) expected);
      Assert.assertEquals(doubleValues[i], (double) expected);
      Assert.assertEquals(stringValues[i], Long.toString(expected));
    }
  }

  @Test(dataProvider = "testIllegalArguments", expectedExceptions = {BadQueryRequestException.class})
  public void testIllegalArguments(String expressionStr) {
    TransformExpressionTree expression = TransformExpressionTree.compileToExpressionTree(expressionStr);
    TransformFunctionFactory.get(expression, _dataSourceMap);
  }

  @DataProvider(name = "testIllegalArguments")
  public Object[][] testIllegalArguments() {
    return new Object[][]{
        new Object[]{String.format("dateTimeConvert(%s,'1:MILLISECONDS:EPOCH','1:MINUTES:EPOCH')", TIME_COLUMN)},
        new Object[]{"dateTimeConvert(5,'1:MILLISECONDS:EPOCH','1:MINUTES:EPOCH','1:MINUTES')"},
        new Object[]{String.format("dateTimeConvert(%s,'1:MILLISECONDS:EPOCH','1:MINUTES:EPOCH','1:MINUTES')", INT_MV_COLUMN)},
        new Object[]{String.format("dateTimeConvert(%s,'1:MILLISECONDS:EPOCH','1:MINUTES:EPOCH','MINUTES')", TIME_COLUMN)},
        new Object[]{String.format("dateTimeConvert(%s,%s,'1:MINUTES:EPOCH','1:MINUTES')", TIME_COLUMN, INT_SV_COLUMN)}
    };
  }
}

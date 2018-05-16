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
package com.linkedin.pinot.core.operator.transform.function;

import com.linkedin.pinot.common.request.transform.TransformExpressionTree;
import com.linkedin.pinot.core.query.exception.BadQueryRequestException;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class TimeConversionTransformFunctionTest extends BaseTransformFunctionTest {

  @Test(dataProvider = "testTimeConversionTransformFunction")
  public void testTimeConversionTransformFunction(String expressionStr) {
    TransformExpressionTree expression = TransformExpressionTree.compileToExpressionTree(expressionStr);
    TransformFunction transformFunction = TransformFunctionFactory.get(expression, _dataSourceMap);
    Assert.assertTrue(transformFunction instanceof TimeConversionTransformFunction);
    Assert.assertEquals(transformFunction.getName(), TimeConversionTransformFunction.FUNCTION_NAME);
    int[] intValues = transformFunction.transformToIntValuesSV(_projectionBlock);
    long[] longValues = transformFunction.transformToLongValuesSV(_projectionBlock);
    float[] floatValues = transformFunction.transformToFloatValuesSV(_projectionBlock);
    double[] doubleValues = transformFunction.transformToDoubleValuesSV(_projectionBlock);
    String[] stringValues = transformFunction.transformToStringValuesSV(_projectionBlock);
    for (int i = 0; i < NUM_ROWS; i++) {
      long expected = TimeUnit.MILLISECONDS.toDays(_timeValues[i]);
      Assert.assertEquals(intValues[i], (int) expected);
      Assert.assertEquals(longValues[i], expected);
      Assert.assertEquals(floatValues[i], (float) expected);
      Assert.assertEquals(doubleValues[i], (double) expected);
      Assert.assertEquals(stringValues[i], Long.toString(expected));
    }
  }

  @DataProvider(name = "testTimeConversionTransformFunction")
  public Object[][] testTimeConversionTransformFunction() {
    return new Object[][]{
        new Object[]{String.format("timeConvert(%s,'MILLISECONDS','DAYS')", TIME_COLUMN)},
        new Object[]{String.format("timeConvert(timeConvert(timeConvert(%s,'MILLISECONDS','SECONDS'),'SECONDS','HOURS'),'HOURS','DAYS')", TIME_COLUMN)}
    };
  }

  @Test(dataProvider = "testIllegalArguments", expectedExceptions = {BadQueryRequestException.class})
  public void testIllegalArguments(String expressionStr) {
    TransformExpressionTree expression = TransformExpressionTree.compileToExpressionTree(expressionStr);
    TransformFunctionFactory.get(expression, _dataSourceMap);
  }

  @DataProvider(name = "testIllegalArguments")
  public Object[][] testIllegalArguments() {
    return new Object[][]{
        new Object[]{String.format("timeConvert(%s,'MILLISECONDS')", TIME_COLUMN)},
        new Object[]{"timeConvert(5,'MILLISECONDS','DAYS')"},
        new Object[]{String.format("timeConvert(%s,'MILLISECONDS','DAYS')", INT_MV_COLUMN)},
        new Object[]{String.format("timeConvert(%s,'MILLISECONDS','1:DAYS')", TIME_COLUMN)},
        new Object[]{String.format("timeConvert(%s,%s,'DAYS')", TIME_COLUMN, INT_SV_COLUMN)}
    };
  }
}

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
package com.linkedin.pinot.core.operator.docvalsets;

import com.linkedin.pinot.core.common.BaseBlockValSet;
import com.linkedin.pinot.core.operator.blocks.ProjectionBlock;
import com.linkedin.pinot.core.operator.transform.TransformResultMetadata;
import com.linkedin.pinot.core.operator.transform.function.TransformFunction;
import com.linkedin.pinot.core.plan.DocIdSetPlanNode;


/**
 * The <code>TransformBlockValSet</code> class represents the block value set for a transform function in the transform
 * block.
 * <p>Caller is responsible for calling the correct method based on the data source metadata for the block value set.
 */
public class TransformBlockValSet extends BaseBlockValSet {
  private final ProjectionBlock _projectionBlock;
  private final TransformFunction _transformFunction;

  private int[] _numMVEntries;

  public TransformBlockValSet(ProjectionBlock projectionBlock, TransformFunction transformFunction) {
    _projectionBlock = projectionBlock;
    _transformFunction = transformFunction;
  }

  @Override
  public int[] getDictionaryIdsSV() {
    return _transformFunction.transformToDictIdsSV(_projectionBlock);
  }

  @Override
  public int[] getIntValuesSV() {
    return _transformFunction.transformToIntValuesSV(_projectionBlock);
  }

  @Override
  public long[] getLongValuesSV() {
    return _transformFunction.transformToLongValuesSV(_projectionBlock);
  }

  @Override
  public float[] getFloatValuesSV() {
    return _transformFunction.transformToFloatValuesSV(_projectionBlock);
  }

  @Override
  public double[] getDoubleValuesSV() {
    return _transformFunction.transformToDoubleValuesSV(_projectionBlock);
  }

  @Override
  public String[] getStringValuesSV() {
    return _transformFunction.transformToStringValuesSV(_projectionBlock);
  }

  @Override
  public int[][] getDictionaryIdsMV() {
    return _transformFunction.transformToDictIdsMV(_projectionBlock);
  }

  @Override
  public int[][] getIntValuesMV() {
    return _transformFunction.transformToIntValuesMV(_projectionBlock);
  }

  @Override
  public long[][] getLongValuesMV() {
    return _transformFunction.transformToLongValuesMV(_projectionBlock);
  }

  @Override
  public float[][] getFloatValuesMV() {
    return _transformFunction.transformToFloatValuesMV(_projectionBlock);
  }

  @Override
  public double[][] getDoubleValuesMV() {
    return _transformFunction.transformToDoubleValuesMV(_projectionBlock);
  }

  @Override
  public String[][] getStringValuesMV() {
    return _transformFunction.transformToStringValuesMV(_projectionBlock);
  }

  @Override
  public int[] getNumMVEntries() {
    if (_numMVEntries == null) {
      _numMVEntries = new int[DocIdSetPlanNode.MAX_DOC_PER_CALL];
    }
    int numDocs = _projectionBlock.getNumDocs();
    TransformResultMetadata resultMetadata = _transformFunction.getResultMetadata();
    if (resultMetadata.hasDictionary()) {
      int[][] dictionaryIds = getDictionaryIdsMV();
      for (int i = 0; i < numDocs; i++) {
        _numMVEntries[i] = dictionaryIds[i].length;
      }
      return _numMVEntries;
    } else {
      switch (resultMetadata.getDataType()) {
        case INT:
          int[][] intValues = getIntValuesMV();
          for (int i = 0; i < numDocs; i++) {
            _numMVEntries[i] = intValues[i].length;
          }
          return _numMVEntries;
        case LONG:
          long[][] longValues = getLongValuesMV();
          for (int i = 0; i < numDocs; i++) {
            _numMVEntries[i] = longValues[i].length;
          }
          return _numMVEntries;
        case FLOAT:
          float[][] floatValues = getFloatValuesMV();
          for (int i = 0; i < numDocs; i++) {
            _numMVEntries[i] = floatValues[i].length;
          }
          return _numMVEntries;
        case DOUBLE:
          double[][] doubleValues = getDoubleValuesMV();
          for (int i = 0; i < numDocs; i++) {
            _numMVEntries[i] = doubleValues[i].length;
          }
          return _numMVEntries;
        case STRING:
          String[][] stringValues = getStringValuesMV();
          for (int i = 0; i < numDocs; i++) {
            _numMVEntries[i] = stringValues[i].length;
          }
          return _numMVEntries;
        default:
          throw new IllegalStateException();
      }
    }
  }
}

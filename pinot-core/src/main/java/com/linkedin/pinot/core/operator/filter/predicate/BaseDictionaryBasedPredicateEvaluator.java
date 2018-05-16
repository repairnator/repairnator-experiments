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
package com.linkedin.pinot.core.operator.filter.predicate;

public abstract class BaseDictionaryBasedPredicateEvaluator extends BasePredicateEvaluator {

  @Override
  public final boolean isDictionaryBased() {
    return true;
  }

  @Override
  public final boolean applySV(long value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean applyMV(long[] values, int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean applySV(float value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean applyMV(float[] values, int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean applySV(double value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean applyMV(double[] values, int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean applySV(String value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean applyMV(String[] values, int length) {
    throw new UnsupportedOperationException();
  }

  // NOTE: override it for exclusive predicate
  @Override
  public int[] getNonMatchingDictIds() {
    throw new UnsupportedOperationException();
  }

  /**
   * Apply a single-value entry to the predicate.
   *
   * @param dictId Dictionary id
   * @return Whether the entry matches the predicate
   */
  @Override
  public abstract boolean applySV(int dictId);

  /**
   * Apply a multi-value entry to the predicate.
   *
   * @param dictIds Array of dictionary ids
   * @param length Number of dictionary ids in the entry
   * @return Whether the entry matches the predicate
   */
  @SuppressWarnings("Duplicates")
  @Override
  public boolean applyMV(int[] dictIds, int length) {
    if (isExclusive()) {
      for (int i = 0; i < length; i++) {
        if (!applySV(dictIds[i])) {
          return false;
        }
      }
      return true;
    } else {
      for (int i = 0; i < length; i++) {
        if (applySV(dictIds[i])) {
          return true;
        }
      }
      return false;
    }
  }
}

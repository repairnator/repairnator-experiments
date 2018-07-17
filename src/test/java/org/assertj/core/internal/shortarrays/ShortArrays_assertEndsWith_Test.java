/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.shortarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ShortArrays.arrayOf;
import static org.assertj.core.test.ShortArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ShortArrays;
import org.assertj.core.internal.ShortArraysBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link ShortArrays#assertEndsWith(AssertionInfo, short[], short[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Florent Biville
 */
public class ShortArrays_assertEndsWith_Test extends ShortArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf(6, 8, 10, 12);
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertEndsWith(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
    arrays.assertEndsWith(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_pass_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    arrays.assertEndsWith(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertEndsWith(someInfo(), null, arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    short[] sequence = { 6, 8, 10, 12, 20, 22 };
    try {
      arrays.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_end_with_sequence() {
    AssertionInfo info = someInfo();
    short[] sequence = { 20, 22 };
    try {
      arrays.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    short[] sequence = { 6, 20, 22 };
    try {
      arrays.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_ends_with_sequence() {
    arrays.assertEndsWith(someInfo(), actual, arrayOf(8, 10, 12));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertEndsWith(someInfo(), actual, arrayOf(6, 8, 10, 12));
  }

  @Test
  public void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(),
                                                                                                        actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_pass_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), null, arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    short[] sequence = { 6, -8, 10, 12, 20, 22 };
    try {
      arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_end_with_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    short[] sequence = { 20, 22 };
    try {
      arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_ends_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    short[] sequence = { 6, 20, 22 };
    try {
      arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_ends_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, arrayOf(-8, 10, 12));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, arrayOf(6, -8, 10, 12));
  }
}

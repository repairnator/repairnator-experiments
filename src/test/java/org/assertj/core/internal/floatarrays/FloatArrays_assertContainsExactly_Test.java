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
package org.assertj.core.internal.floatarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.FloatArrays.arrayOf;
import static org.assertj.core.test.FloatArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FloatArrays;
import org.assertj.core.internal.FloatArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link FloatArrays#assertContainsExactly(AssertionInfo, float[], float[])}</code>.
 */
public class FloatArrays_assertContainsExactly_Test extends FloatArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_exactly() {
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6f, 8f, 10f));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactly(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  public void should_fail_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    try {
      arrays.assertContainsExactly(info, actual, arrayOf(6f, 10f, 8f));
    } catch (AssertionError e) {
      verify(failures).failure(info, elementsDifferAtIndex(8f, 10f, 1));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, arrayOf(6f, 8f)));
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, emptyArray()));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), null, arrayOf(8f)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    float[] expected = { 6f, 8f, 20f };
    try {
      arrays.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                          newArrayList(20f), newArrayList(10f)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    float[] expected = { 6f, 8f, 10f, 10f };
    try {
      arrays.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                          newArrayList(10f), newArrayList()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, arrayOf(6f, -8f, 10f));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    float[] expected = { -6f, 10f, 8f };
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, elementsDifferAtIndex(8f, 10f, 1, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, emptyArray()));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                               actual,
                                                                                                               null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), null, arrayOf(-8f)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    float[] expected = { 6f, -8f, 20f };
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                          newArrayList(20f), newArrayList(10f),
                                                          absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    float[] expected = { 6f, -8f, -10f, 10f };
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                          newArrayList(10f), newArrayList(),
                                                          absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}

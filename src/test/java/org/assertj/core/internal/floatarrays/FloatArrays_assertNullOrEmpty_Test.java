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

import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.test.FloatArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FloatArrays;
import org.assertj.core.internal.FloatArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link FloatArrays#assertNullOrEmpty(AssertionInfo, float[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class FloatArrays_assertNullOrEmpty_Test extends FloatArraysBaseTest {

  @Test
  public void should_fail_if_array_is_not_null_and_is_not_empty() {
    AssertionInfo info = someInfo();
    float[] actual = { 6f, 8f };
    try {
      arrays.assertNullOrEmpty(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeNullOrEmpty(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_array_is_null() {
    arrays.assertNullOrEmpty(someInfo(), null);
  }

  @Test
  public void should_pass_if_array_is_empty() {
    arrays.assertNullOrEmpty(someInfo(), emptyArray());
  }
}

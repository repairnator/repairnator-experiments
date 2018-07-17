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
package org.assertj.core.api.offsettime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.fail;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

public class OffsetTimeAssert_isEqualTo_Test extends OffsetTimeAssertBaseTest {

  @Test
  public void test_isEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isEqualTo(REFERENCE);
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
    // THEN
    verify_that_isEqualTo_assertion_fails_and_throws_AssertionError(REFERENCE);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(OffsetTime.of(3, 0, 5, 0, ZoneOffset.UTC)).isEqualTo("03:03:03Z"))
                                                   .withMessage("expected:<03:0[3:03]Z> but was:<03:0[0:05]Z>");
  }

  @Test
  public void should_fail_if_offsetTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(OffsetTime.now()).isEqualTo((String) null))
                                        .withMessage("The String representing the OffsetTime to compare actual with should not be null");
  }

  private static void verify_that_isEqualTo_assertion_fails_and_throws_AssertionError(OffsetTime reference) {
    try {
      assertThat(reference).isEqualTo(reference.plusHours(1).toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}

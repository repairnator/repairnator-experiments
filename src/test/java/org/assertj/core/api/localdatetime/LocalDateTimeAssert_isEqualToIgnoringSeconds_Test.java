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
package org.assertj.core.api.localdatetime;

import static java.lang.String.format;
import static org.assertj.core.api.AbstractLocalDateTimeAssert.NULL_LOCAL_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;

import org.assertj.core.api.BaseTest;
import org.junit.jupiter.api.Test;


public class LocalDateTimeAssert_isEqualToIgnoringSeconds_Test extends BaseTest {

  private final LocalDateTime refLocalDateTime = LocalDateTime.of(2000, 1, 1, 23, 51, 0, 0);

  @Test
  public void should_pass_if_actual_is_equal_to_other_ignoring_second_fields() {
    assertThat(refLocalDateTime).isEqualToIgnoringSeconds(refLocalDateTime.plusSeconds(1));
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_given_localdatetime_with_second_ignored() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(refLocalDateTime).isEqualToIgnoringSeconds(refLocalDateTime.plusMinutes(1)))
                                                   .withMessage(format("%nExpecting:%n  <2000-01-01T23:51>%nto have same year, month, day, hour and minute as:%n  <2000-01-01T23:52>%nbut had not."));
  }

  @Test
  public void should_fail_as_seconds_fields_are_different_even_if_time_difference_is_less_than_a_second() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(refLocalDateTime).isEqualToIgnoringSeconds(refLocalDateTime.minusNanos(1)))
                                                   .withMessage(format("%nExpecting:%n  <2000-01-01T23:51>%nto have same year, month, day, hour and minute as:%n  <2000-01-01T23:50:59.999999999>%nbut had not."));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      LocalDateTime actual = null;
      assertThat(actual).isEqualToIgnoringSeconds(LocalDateTime.now());
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_throw_error_if_given_localdatetime_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(refLocalDateTime).isEqualToIgnoringSeconds(null))
                                        .withMessage(NULL_LOCAL_DATE_TIME_PARAMETER_MESSAGE);
  }

}

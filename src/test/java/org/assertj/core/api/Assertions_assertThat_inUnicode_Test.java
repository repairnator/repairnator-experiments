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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.core.presentation.UnicodeRepresentation#toStringOf(Object)}.
 *
 * @author Mariusz Smykula
 */
public class Assertions_assertThat_inUnicode_Test {

  @Test
  public void should_assert_String_in_unicode() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat("a6c").inUnicode().isEqualTo("abó"))
                                                   .withMessage("expected:<a[b\\u00f3]> but was:<a[6c]>");
  }

  @Test
  public void should_assert_Character_in_unicode() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat('o').inUnicode().isEqualTo('ó'))
                                                   .withMessage("expected:<[\\u00f3]> but was:<[o]>");
  }

  @Test
  public void should_assert_char_array_in_unicode_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat("a6c".toCharArray()).inUnicode()
                                                                                                    .isEqualTo("abó".toCharArray()))
                                                   .withMessage("expected:<[a, [b, \\u00f3]]> but was:<[a, [6, c]]>");
  }
}

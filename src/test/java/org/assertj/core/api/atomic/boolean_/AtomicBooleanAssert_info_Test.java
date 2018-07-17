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
package org.assertj.core.api.atomic.boolean_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

public class AtomicBooleanAssert_info_Test {

  @Test
  public void should_honor_info_update() {
    String assertionDescription = "ssss";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new AtomicBoolean(true)).as(assertionDescription)
                                                                                                        .isFalse())
                                                   .withMessageContaining(assertionDescription);
  }

}

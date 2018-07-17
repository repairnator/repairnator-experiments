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
package org.assertj.core.error;

import static java.lang.String.format;
import static java.time.ZoneOffset.MIN;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqualIgnoringTimezone.shouldBeEqualIgnoringTimezone;

import java.time.OffsetDateTime;
import java.time.OffsetTime;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.error.ShouldBeEqualIgnoringTimezone#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Alexander Bischof
 */
public class ShouldBeEqualIgnoringTimezone_create_Test {

  private ErrorMessageFactory factory;

  @Test
  public void should_create_error_message_for_OffsetTime() {

    factory = shouldBeEqualIgnoringTimezone(OffsetTime.of(12, 0, 0, 0, UTC),
                                            OffsetTime.of(12, 0, 0, 0, MIN));

    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <12:00Z>%n" +
                                         "to have same time fields except timezone as:%n" +
                                         "  <12:00-18:00>%n" +
                                         "but had not."));
  }

  @Test
  public void should_create_error_message_for_OffsetDateTime() {

    factory = shouldBeEqualIgnoringTimezone(OffsetDateTime.of(2000, 5, 13, 12, 0, 0, 0, UTC),
                                            OffsetDateTime.of(2000, 5, 13, 12, 0, 0, 0, MIN));

    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <2000-05-13T12:00Z>%n" +
                                         "to have same time fields except timezone as:%n" +
                                         "  <2000-05-13T12:00-18:00>%n" +
                                         "but had not."));
  }
}

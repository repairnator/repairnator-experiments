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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.stream.Stream;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.ComparisonFailure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link ShouldBeEqual#newAssertionError(Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Dan Corder
 */
public class ShouldBeEqual_newAssertionError_Test {

  private Description description;
  private ShouldBeEqual factory;
  private DescriptionFormatter formatter;

  @BeforeEach
  public void setUp() {
    description = new TestDescription("Jedi");
    factory = (ShouldBeEqual) shouldBeEqual("Luke", "Yoda", new StandardRepresentation());
    factory.descriptionFormatter = mock(DescriptionFormatter.class);
    formatter = factory.descriptionFormatter;
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void should_create_ComparisonFailure_if_JUnit4_is_present_and_trim_spaces_in_formatted_description(String formattedDescription) {
    // GIVEN
    given(formatter.format(description)).willReturn(formattedDescription);
    // WHEN
    AssertionError error = factory.newAssertionError(description, new StandardRepresentation());
    // THEN
    assertThat(error).isInstanceOf(ComparisonFailure.class)
                     .hasMessage("[Jedi] expected:<\"[Yoda]\"> but was:<\"[Luke]\">");
  }

  public static Stream<Arguments> parameters() {
    return Stream.of(Arguments.of("[Jedi]"), Arguments.of("[Jedi]  "));
  }
}

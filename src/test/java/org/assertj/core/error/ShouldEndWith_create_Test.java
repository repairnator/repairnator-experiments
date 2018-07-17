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

import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link ShouldEndWith#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldEndWith_create_Test {

  private ErrorMessageFactory factory;

  @Test
  public void should_create_error_message() {
    factory = shouldEndWith(newArrayList("Yoda", "Luke"), newArrayList("Han", "Leia"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n <[\"Yoda\", \"Luke\"]>%nto end with:%n <[\"Han\", \"Leia\"]>%n"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    factory = shouldEndWith(newArrayList("Yoda", "Luke"), newArrayList("Han", "Leia"), new ComparatorBasedComparisonStrategy(
        CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpecting:%n <[\"Yoda\", \"Luke\"]>%nto end with:%n <[\"Han\", \"Leia\"]>%n"
        + "when comparing values using CaseInsensitiveStringComparator"));
  }
}

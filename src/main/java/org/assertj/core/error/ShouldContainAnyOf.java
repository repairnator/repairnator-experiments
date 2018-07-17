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

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

public class ShouldContainAnyOf extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldContainAnyOf(Object actual, Object expected,
                                                       ComparisonStrategy comparisonStrategy) {
    return new ShouldContainAnyOf(actual, expected, comparisonStrategy);
  }

  public static ErrorMessageFactory shouldContainAnyOf(Object actual, Object expected) {
    return shouldContainAnyOf(actual, expected, StandardComparisonStrategy.instance());
  }

  private ShouldContainAnyOf(Object actual, Object expected, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n" +
          "  <%s>%n" +
          "to contain at least one of the following elements:%n" +
          "  <%s>%n" +
          "but none were found %s",
          actual, expected, comparisonStrategy);
  }

}

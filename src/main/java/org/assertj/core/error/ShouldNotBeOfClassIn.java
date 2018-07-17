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

/**
 * Creates an error message indicating that an assertion that verifies that an object is not of type in group of types failed.
 * 
 * @author Nicolas François
 */
public class ShouldNotBeOfClassIn extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotBeOfClassIn}</code>.
   * @param actual the actual value in the failed assertion.
   * @param types contains the types {@code actual} is not expected to be in.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeOfClassIn(Object actual, Object types) {
    return new ShouldNotBeOfClassIn(actual, types);
  }

  private ShouldNotBeOfClassIn(Object actual, Object types) {
    super("%nExpecting:%n <%s>%nnot to be of any type in:%n <%s>%nbut was of type:<%s>", actual, types, actual.getClass());
  }
}

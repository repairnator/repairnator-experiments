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
 * Creates an error message indicating that a group of elements should have been an array.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeAnArray extends BasicErrorMessageFactory {

  /**
   * Creates a new instance of <code>{@link org.assertj.core.error.ShouldBeAnArray}</code>.
   * @param object the object value in the failed assertion.
   * @return the created of {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeAnArray(Object object) {
    return new ShouldBeAnArray(object);
  }

  private ShouldBeAnArray(Object object) {
    super("%nExpecting an array but was:<%s>", object);
  }
}

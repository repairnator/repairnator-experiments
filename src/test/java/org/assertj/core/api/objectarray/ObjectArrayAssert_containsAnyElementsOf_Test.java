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
package org.assertj.core.api.objectarray;

import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;

public class ObjectArrayAssert_containsAnyElementsOf_Test extends ObjectArrayAssertBaseTest {

  private final List<Object> iterable = Arrays.asList(new Object(), "bar");

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.containsAnyElementsOf(iterable);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsAnyOf(info(), getActual(assertions), iterable.toArray());
  }

}

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
package org.assertj.core.api.booleanarray;

import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.BooleanArrayAssert;
import org.assertj.core.api.BooleanArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.BooleanArrayAssert#containsExactly(boolean...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class BooleanArrayAssert_containsExactly_Test extends BooleanArrayAssertBaseTest {

  @Override
  protected BooleanArrayAssert invoke_api_method() {
    return assertions.containsExactly(true, false);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsExactly(getInfo(assertions), getActual(assertions), arrayOf(true, false));
  }

}

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
package org.assertj.core.api.date;

import org.assertj.core.api.DateAssert;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link DateAssert#hasMinute(int)}</code>.
 * 
 * @author Joel Costigliola
 */
public class DateAssert_hasMinute_Test extends AbstractDateAssertWithOneIntArg_Test {

  @Override
  protected DateAssert assertionInvocationWithOneIntArg() {
    return assertions.hasMinute(intArg);
  }

  @Override
  protected void verifyAssertionInvocation() {
    verify(dates).assertHasMinute(getInfo(assertions), getActual(assertions), intArg);
  }

}

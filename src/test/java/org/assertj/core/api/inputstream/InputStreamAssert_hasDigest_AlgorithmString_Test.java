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
package org.assertj.core.api.inputstream;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.InputStreamAssert;
import org.assertj.core.api.InputStreamAssertBaseTest;

/**
 * Tests for <code>{@link InputStreamAssert#hasDigest(String, String)}</code>
 *
 * @author Valeriy Vyrva
 */
public class InputStreamAssert_hasDigest_AlgorithmString_Test extends InputStreamAssertBaseTest {

  private final String algorithm = "MD5";
  private final String expected = "";

  @Override
  protected InputStreamAssert invoke_api_method() {
    return assertions.hasDigest(algorithm, expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(inputStreams).assertHasDigest(getInfo(assertions), getActual(assertions), algorithm, expected);
  }
}

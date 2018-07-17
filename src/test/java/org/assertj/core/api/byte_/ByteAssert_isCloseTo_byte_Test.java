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
package org.assertj.core.api.byte_;

import static org.assertj.core.data.Offset.offset;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ByteAssert;
import org.assertj.core.api.ByteAssertBaseTest;
import org.assertj.core.data.Offset;


/**
 * Tests for <code>{@link org.assertj.core.api.ByteAssert#isCloseTo(Byte, org.assertj.core.data.Offset)}</code>.
 *
 * @author Joel Costigliola
 */
public class ByteAssert_isCloseTo_byte_Test extends ByteAssertBaseTest {

  private final Offset<Byte> offset = offset((byte)5);
  private final Byte value = (byte)8;

  @Override
  protected ByteAssert invoke_api_method() {
    return assertions.isCloseTo(value, offset);
  }

  @Override
  protected void verify_internal_effects() {
    verify(bytes).assertIsCloseTo(getInfo(assertions), getActual(assertions), value, offset);
  }
}

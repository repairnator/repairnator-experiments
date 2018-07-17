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
package org.assertj.core.api.floatarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Comparator;


import org.assertj.core.api.FloatArrayAssert;
import org.assertj.core.api.FloatArrayAssertBaseTest;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

/**
 * Tests for <code>{@link FloatArrayAssert#usingElementComparator(Comparator)}</code>.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class FloatArrayAssert_usingElementComparator_Test extends FloatArrayAssertBaseTest {

  @Mock
  private Comparator<Float> comparator;

  private Objects objectsBefore;

  @BeforeEach
  public void before() {
    initMocks(this);
    objectsBefore = getObjects(assertions);
  }

  @Override
  protected FloatArrayAssert invoke_api_method() {
    // in that test, the comparator type is not important, we only check that we correctly switch of comparator
    return assertions.usingElementComparator(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(objectsBefore).isSameAs(getObjects(assertions));
    assertThat(comparator).isSameAs(getArrays(assertions).getComparator());
  }
}

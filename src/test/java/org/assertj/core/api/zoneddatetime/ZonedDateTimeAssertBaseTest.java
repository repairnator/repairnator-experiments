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
package org.assertj.core.api.zoneddatetime;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.assertj.core.api.BaseTest;


/**
 * Base test class for {@link org.assertj.core.api.ZonedDateTimeAssert} tests.
 * 
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssertBaseTest extends BaseTest {

  public static final ZonedDateTime REFERENCE = ZonedDateTime.of(2000, 12, 14, 0, 0, 0, 1, ZoneOffset.UTC);
  public static final ZonedDateTime BEFORE = ZonedDateTime.of(2000, 12, 14, 0, 0, 0, 0, ZoneOffset.UTC);
  public static final ZonedDateTime AFTER = ZonedDateTime.of(2000, 12, 14, 0, 0, 0, 2, ZoneOffset.UTC);

}
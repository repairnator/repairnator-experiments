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
package org.assertj.core.internal.urls;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.uri.ShouldHaveProtocol.shouldHaveProtocol;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;

public class Urls_assertHasProtocol_Test extends UrlsBaseTest {

  @Test
  public void should_pass_if_actual_uri_has_the_given_protocol() throws MalformedURLException {
    urls.assertHasProtocol(info, new URL("http://example.com/pages/"), "http");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> urls.assertHasProtocol(info, null, "http"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_protocol_is_not_the_expected_protocol() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL url = new URL("http://example.com/pages/");
    String expectedProtocol = "ftp";
    try {
      urls.assertHasProtocol(info, url, expectedProtocol);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveProtocol(url, expectedProtocol));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}

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
package org.assertj.core.api.future;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.error.future.ShouldBeCompleted.shouldBeCompleted;
import static org.assertj.core.error.future.Warning.WARNING;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.BaseTest;
import org.junit.jupiter.api.Test;

public class CompletableFutureAssert_isCompleted_Test extends BaseTest {

  @Test
  public void should_pass_if_completable_future_is_completed() {
    assertThat(CompletableFuture.completedFuture("done")).isCompleted();
  }

  @Test
  public void should_fail_when_completable_future_is_null() {
    assertThatThrownBy(() -> assertThat((CompletableFuture<String>) null).isCompleted()).isInstanceOf(AssertionError.class)
                                                                                        .hasMessage(format(actualIsNull()));
  }

  @Test
  public void should_fail_if_completable_future_is_incomplete() {
    CompletableFuture<String> future = new CompletableFuture<>();

    assertThatThrownBy(() -> assertThat(future).isCompleted()).isInstanceOf(AssertionError.class)
                                                              .hasMessage(shouldBeCompleted(future).create());
  }

  @Test
  public void should_fail_if_completable_future_has_failed() {
    CompletableFuture<String> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException());

    assertThatThrownBy(() -> assertThat(future).isCompleted()).isInstanceOf(AssertionError.class)
                                                              .hasMessageStartingWith(format("%nExpecting%n  <CompletableFuture[Failed: java.lang.RuntimeException]%n"))
                                                              .hasMessageContaining("Caused by: java.lang.RuntimeException")
                                                              .hasMessageEndingWith(format("to be completed.%n%s", WARNING));
  }

  @Test
  public void should_fail_if_completable_future_was_cancelled() {
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(true);

    assertThatThrownBy(() -> assertThat(future).isCompleted()).isInstanceOf(AssertionError.class)
                                                              .hasMessage(shouldBeCompleted(future).create());
  }
}

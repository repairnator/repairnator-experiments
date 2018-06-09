/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.utils.concurrent;

import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import static io.atomix.utils.concurrent.Threads.namedThreads;

/**
 * Thread pool context factory.
 */
public class ThreadPoolContextFactory implements ThreadContextFactory {
  private final ScheduledExecutorService executor;

  public ThreadPoolContextFactory(String name, int threadPoolSize, Logger logger) {
    this(threadPoolSize, namedThreads(name, logger));
  }

  public ThreadPoolContextFactory(int threadPoolSize, ThreadFactory threadFactory) {
    this(Executors.newScheduledThreadPool(threadPoolSize, threadFactory));
  }

  public ThreadPoolContextFactory(ScheduledExecutorService executor) {
    this.executor = executor;
  }

  @Override
  public ThreadContext createContext() {
    return new ThreadPoolContext(executor);
  }

  @Override
  public void close() {
    executor.shutdownNow();
  }
}

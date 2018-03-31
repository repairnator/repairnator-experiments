/*
 * Copyright (c) 2001-2017, Zoltan Farkas All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Additionally licensed with:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spf4j.stackmonitor;

import com.google.common.collect.ImmutableMap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gnu.trove.set.hash.THashSet;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.spf4j.base.Threads;

/**
 * This is a high performance sampling collector. The goal is for the sampling overhead to be minimal. This is better
 * than the SimpleStackCollector in 2 ways: 1) No HashMap is created during sampling. Resulting in less garbage
 * generated by sampling. 2) Stack trace for the sampling Thread is not created at all, saving some time and creating
 * less garbage.
 *
 * @author zoly
 */
@ParametersAreNonnullByDefault
public final class FastStackCollector implements ISampler {

  private static final String[] IGNORED_THREADS = {
    "Finalizer",
    "Signal Dispatcher",
    "Reference Handler",
    "Attach Listener",
    "VM JFR Buffer Thread"
  };

  private final Predicate<Thread> threadFilter;

  private final StackCollector collector;

  private Thread[] requestFor = new Thread[]{};

  public FastStackCollector(final boolean collectForMain, final String... xtraIgnoredThreads) {
    this(false, collectForMain, xtraIgnoredThreads);
  }

  public FastStackCollector(final boolean collectRunnableThreadsOnly,
                            final boolean collectForMain,
                            final String... xtraIgnoredThreads) {
    this(collectRunnableThreadsOnly, collectForMain, Threads.EMPTY_ARRAY, xtraIgnoredThreads);
  }

  public FastStackCollector(final boolean collectRunnableThreadsOnly,
                            final boolean collectForMain,
                            final Thread[] ignored,
                            final String... xtraIgnoredThreads) {
    this(createNameBasedFilter(collectRunnableThreadsOnly, collectForMain, ignored, xtraIgnoredThreads));
  }

  /**
   * @param threadFilter when returns true the thread is being ignored
   */
  public FastStackCollector(final Predicate<Thread> threadFilter) {
    this.threadFilter = threadFilter;
    this.collector = new StackCollectorImpl();
  }

  public static Predicate<Thread> createNameBasedFilter(final boolean collectRunnableThreadsOnly,
                                                        final boolean collectForMain,
                                                        final Thread[] ignored,
                                                        final String[] xtraIgnoredThreads) {
    final Set<String> ignoredThreadNames = new THashSet<>(Arrays.asList(IGNORED_THREADS));
    ignoredThreadNames.addAll(Arrays.asList(xtraIgnoredThreads));
    Predicate<Thread> result = new ThreadNamesPredicate(ignoredThreadNames);
    if (collectRunnableThreadsOnly) {
      result = result.or((Thread t) -> Thread.State.RUNNABLE != t.getState());
    }
    for (Thread th : ignored) {
      result = result.or((t) -> t == th);
    }
    if (!collectForMain) {
      Thread mainThread = org.spf4j.base.Runtime.getMainThread();
      result = result.or((t) -> t == mainThread);
    }
    return result;
  }

  /**
   * @deprecated use Threads.getThreads
   */
  @Deprecated
  public static Thread[] getThreads() {
    return Threads.getThreads();
  }

  /**
   * @deprecated use Threads.getStackTraces
   */
  @Deprecated
  public static StackTraceElement[][] getStackTraces(final Thread... threads) {
    return Threads.getStackTraces(threads);
  }

  /**
   * @deprecated use Threads.dumpToPrintStream
   */
  @Deprecated
  public static void dumpToPrintStream(final PrintStream stream) {
    Threads.dumpToPrintStream(stream);
  }

  @Override
  @SuppressFBWarnings("EXS_EXCEPTION_SOFTENING_NO_CHECKED")
  public void sample() {
    Thread[] threads = Threads.getThreads();
    final int nrThreads = threads.length;
    if (requestFor.length < nrThreads) {
      requestFor = new Thread[nrThreads - 1];
    }
    int j = 0;
    for (int i = 0; i < nrThreads; i++) {
      Thread th = threads[i];
      if (!threadFilter.test(th)) { // not interested in these traces
        requestFor[j++] = th;
      }
    }
    Arrays.fill(requestFor, j, requestFor.length, null);
    StackTraceElement[][] stackDump = Threads.getStackTraces(requestFor);
    for (int i = 0; i < j; i++) {
      StackTraceElement[] stackTrace = stackDump[i];
      if (stackTrace != null && stackTrace.length > 0) {
        collector.collect(stackTrace);
      } else {
        collector.collect(new StackTraceElement[]{
          new StackTraceElement("Thread", requestFor[i].getName(), "", 0)
        });
      }
    }
  }

  @Override
  public Map<String, SampleNode> getCollectionsAndReset() {
    SampleNode nodes = collector.getAndReset();
    return nodes == null ? Collections.EMPTY_MAP : ImmutableMap.of("ALL", nodes);
  }

  @Override
  public Map<String, SampleNode> getCollections() {
    SampleNode nodes = collector.get();
    return nodes == null ? Collections.EMPTY_MAP : ImmutableMap.of("ALL", nodes);
  }

  public static final class ThreadNamesPredicate implements Predicate<Thread> {

    private final Set<String> ignoredThreadNames;

    public ThreadNamesPredicate(final Set<String> ignoredThreadNames) {
      this.ignoredThreadNames = ignoredThreadNames;
    }

    @Override
    public boolean test(@Nonnull final Thread input) {
      return ignoredThreadNames.contains(input.getName());
    }
  }

  @Override
  public String toString() {
    return "FastStackCollector{" + "threadFilter=" + threadFilter + ", collector=" + collector + '}';
  }

}
/*
 * Copyright 2017 SPF4J.
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
package org.spf4j.failsafe;

import org.spf4j.base.TimeSource;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Zoltan Farkas
 */
public class TimeSourceTest {


  @Test
  public void testTimeout() {
    long nano1 = TimeSource.nanoTime();
    long nano2 = TimeSource.nanoTime();
    long deadline = nano1 + TimeSource.MAX_TIMEOUT;
    Assert.assertTrue(nano2 - nano1 >= 0);
    Assert.assertEquals(TimeSource.MAX_TIMEOUT, deadline - nano1);

    long deadlineNanos = TimeSource.getDeadlineNanos(Long.MAX_VALUE, TimeUnit.MINUTES);
    System.out.println(deadlineNanos - TimeSource.nanoTime());
    Assert.assertTrue(deadlineNanos - TimeSource.nanoTime() > 0);
  }

}

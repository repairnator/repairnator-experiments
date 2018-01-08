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
package org.spf4j.base;

import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Zoltan Farkas
 */
public class TimingTest {


  @Test
  public void testTiming() {
    Timing currentTiming = Timing.getCurrentTiming();
    long currNanos = System.nanoTime();
    long currMillis = System.currentTimeMillis();
    long millisInTheFutureCalculated = currentTiming.fromNanoTimeToEpochMillis(currNanos
            + TimeUnit.MILLISECONDS.toNanos(10));
    Assert.assertTrue(currMillis + 10 - millisInTheFutureCalculated <= 1);
    try {
      currentTiming.fromEpochMillisToNanoTime(Long.MAX_VALUE);
      Assert.fail();
    } catch (IllegalArgumentException ex) {
    }
    currNanos = System.nanoTime();
    long nanoTimeIn100ms = currentTiming.fromEpochMillisToNanoTime(System.currentTimeMillis() + 100);
    long expectNanoTimeIn100ms = currNanos + TimeUnit.MILLISECONDS.toNanos(100);
    Assert.assertTrue("expected = " + expectNanoTimeIn100ms + ", actual =" + nanoTimeIn100ms,
            Math.abs( expectNanoTimeIn100ms - nanoTimeIn100ms) < TimeUnit.MILLISECONDS.toNanos(1));

  }

}

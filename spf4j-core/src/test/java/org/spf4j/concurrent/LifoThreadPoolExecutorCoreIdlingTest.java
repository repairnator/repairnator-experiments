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
package org.spf4j.concurrent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spf4j.perf.cpu.CpuUsageSampler;
import org.spf4j.stackmonitor.Sampler;

/**
 *
 * @author zoly
 */
@SuppressFBWarnings({"MDM_THREAD_YIELD", "PRMC_POSSIBLY_REDUNDANT_METHOD_CALLS"})
public class LifoThreadPoolExecutorCoreIdlingTest {

  private static final Logger LOG = LoggerFactory.getLogger(LifoThreadPoolExecutorCoreIdlingTest.class);

  @Test
  public void testLifoExecSQ() throws InterruptedException, IOException {
    LifoThreadPoolExecutorSQP executor
            = new LifoThreadPoolExecutorSQP("test", 2, 8, 20, 1024, 0);
    Sampler s = Sampler.getSampler(20, 10000,
            new File(org.spf4j.base.Runtime.TMP_FOLDER),
            "lifeTest1");
    s.start();
    org.spf4j.base.Runtime.gc(5000);
    Thread.sleep(100);
    long time = CpuUsageSampler.getProcessCpuTimeNanos();
    Thread.sleep(3000);
    long cpuTime = CpuUsageSampler.getProcessCpuTimeNanos() - time;
    LOG.info("Cpu profile saved to {}", s.dumpToFile());
    LOG.debug("CPU time = {} ns", cpuTime);
    s.stop();
    Assert.assertTrue("CPU Time = " + cpuTime, cpuTime < 1500000000);
   // 6069497000 with bug  53945000/8035000/6000000 without bug without profiler, 119628000 with profiler
    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.SECONDS);
  }

  @Test
  public void testLifoExecSQMutable() throws InterruptedException, IOException {
    MutableLifoThreadPoolExecutorSQP executor
            = new MutableLifoThreadPoolExecutorSQP("test", 2, 8, 20, 1024, 0);
    Sampler s = Sampler.getSampler(20, 10000,
            new File(org.spf4j.base.Runtime.TMP_FOLDER),
            "lifeTest1");
    s.start();
    org.spf4j.base.Runtime.gc(5000);
    Thread.sleep(100);
    long time = CpuUsageSampler.getProcessCpuTimeNanos();
    Thread.sleep(3000);
    long cpuTime = CpuUsageSampler.getProcessCpuTimeNanos() - time;
    LOG.info("Cpu profile saved to {}", s.dumpToFile());
    LOG.debug("CPU time = {} ns", cpuTime);
    s.stop();
    Assert.assertTrue("CPU Time = " + cpuTime, cpuTime < 1500000000); // 6069497000 with bug  53945000 without bug
    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.SECONDS);
  }

}

/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.core.segment.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


public class PinotLByteBufferTest {
  private static Logger LOGGER = LoggerFactory.getLogger(PinotLByteBufferTest.class);

  @Test(enabled = false)
  public void testLoadGet() {
    PinotLByteBuffer buffer = PinotLByteBuffer.allocateDirect(3L * PinotDataBufferTest.ONE_GB);
    PinotDataBufferTest.loadVerifyAllTypes(buffer);
    buffer.close();
  }


}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.core.io.aio;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.activemq.artemis.jlibaio.LibaioFile;

public class ActiveMQFileLock extends FileLock {

   private final LibaioFile file;

   public ActiveMQFileLock(final LibaioFile handle) {
      super((FileChannel) null, 0, 0, false);
      this.file = handle;
   }

   @Override
   public boolean isValid() {
      return true;
   }

   @Override
   public void release() throws IOException {
      file.close();
   }
}

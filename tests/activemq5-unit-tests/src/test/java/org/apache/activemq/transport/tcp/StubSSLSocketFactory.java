/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.activemq.transport.tcp;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class StubSSLSocketFactory extends SSLServerSocketFactory {

   private final ServerSocket retServerSocket;

   public StubSSLSocketFactory(ServerSocket returnServerSocket) {
      retServerSocket = returnServerSocket;
   }

   @Override
   public ServerSocket createServerSocket(int arg0) throws IOException {
      return retServerSocket;
   }

   @Override
   public ServerSocket createServerSocket(int arg0, int arg1) throws IOException {
      return retServerSocket;
   }

   @Override
   public ServerSocket createServerSocket(int arg0, int arg1, InetAddress arg2) throws IOException {
      return retServerSocket;
   }

   // --- Stubbed Methods ---

   @Override
   public String[] getDefaultCipherSuites() {
      return null;
   }

   @Override
   public String[] getSupportedCipherSuites() {
      return null;
   }
}

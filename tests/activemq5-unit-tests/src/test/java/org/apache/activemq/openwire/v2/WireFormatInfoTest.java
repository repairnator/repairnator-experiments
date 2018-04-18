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
package org.apache.activemq.openwire.v2;

import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.openwire.DataFileGeneratorTestSupport;

/**
 * Test case for the OpenWire marshalling for WireFormatInfo NOTE!: This file is
 * auto generated - do not modify! if you need to make a change, please see the
 * modify the groovy scripts in the under src/gram/script and then use maven
 * openwire:generate to regenerate this file.
 */
public class WireFormatInfoTest extends DataFileGeneratorTestSupport {

   public static final WireFormatInfoTest SINGLETON = new WireFormatInfoTest();

   @Override
   public Object createObject() throws Exception {
      WireFormatInfo info = new WireFormatInfo();
      populateObject(info);
      return info;
   }

   @Override
   protected void populateObject(Object object) throws Exception {
      super.populateObject(object);
      WireFormatInfo info = (WireFormatInfo) object;
      info.setVersion(1);

      {
         byte data[] = "MarshalledProperties:1".getBytes();
         info.setMarshalledProperties(new org.apache.activemq.util.ByteSequence(data, 0, data.length));
      }

   }
}

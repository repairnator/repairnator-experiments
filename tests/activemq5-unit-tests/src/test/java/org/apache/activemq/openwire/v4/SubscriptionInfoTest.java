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
package org.apache.activemq.openwire.v4;

import org.apache.activemq.command.SubscriptionInfo;
import org.apache.activemq.openwire.DataFileGeneratorTestSupport;

/**
 * Test case for the OpenWire marshalling for SubscriptionInfo
 *
 *
 * NOTE!: This file is auto generated - do not modify!
 * if you need to make a change, please see the modify the groovy scripts in the
 * under src/gram/script and then use maven openwire:generate to regenerate
 * this file.
 */
public class SubscriptionInfoTest extends DataFileGeneratorTestSupport {

   public static SubscriptionInfoTest SINGLETON = new SubscriptionInfoTest();

   @Override
   public Object createObject() throws Exception {
      SubscriptionInfo info = new SubscriptionInfo();
      populateObject(info);
      return info;
   }

   @Override
   protected void populateObject(Object object) throws Exception {
      super.populateObject(object);
      SubscriptionInfo info = (SubscriptionInfo) object;

      info.setClientId("ClientId:1");
      info.setDestination(createActiveMQDestination("Destination:2"));
      info.setSelector("Selector:3");
      info.setSubscriptionName("SubcriptionName:4");
      info.setSubscribedDestination(createActiveMQDestination("SubscribedDestination:5"));
   }
}

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
package org.apache.activemq.transport.udp;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.test.JmsTopicSendReceiveWithTwoConnectionsTest;

/**
 * @version
 */
public class UdpSendReceiveWithTwoConnectionsTest extends JmsTopicSendReceiveWithTwoConnectionsTest {

   protected String brokerURI = "udp://localhost:8891";
   protected BrokerService broker;

   @Override
   protected void setUp() throws Exception {
      broker = createBroker();
      broker.start();

      super.setUp();
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
      if (broker != null) {
         broker.stop();
      }
   }

   protected BrokerService createBroker() throws Exception {
      BrokerService answer = new BrokerService();
      answer.setPersistent(false);
      answer.setUseJmx(false);
      answer.addConnector(brokerURI);
      return answer;
   }

   @Override
   protected ActiveMQConnectionFactory createConnectionFactory() throws Exception {
      return new ActiveMQConnectionFactory(brokerURI);
   }
}

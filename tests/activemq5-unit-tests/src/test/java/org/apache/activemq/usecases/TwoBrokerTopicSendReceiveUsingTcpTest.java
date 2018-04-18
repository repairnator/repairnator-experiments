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
package org.apache.activemq.usecases;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.xbean.BrokerFactoryBean;
import org.springframework.core.io.ClassPathResource;

/**
 *
 */
public class TwoBrokerTopicSendReceiveUsingTcpTest extends TwoBrokerTopicSendReceiveTest {

   private BrokerService receiverBroker;
   private BrokerService senderBroker;

   @Override
   protected void setUp() throws Exception {
      BrokerFactoryBean brokerFactory;

      brokerFactory = new BrokerFactoryBean(new ClassPathResource("org/apache/activemq/usecases/receiver.xml"));
      brokerFactory.afterPropertiesSet();
      receiverBroker = brokerFactory.getBroker();

      brokerFactory = new BrokerFactoryBean(new ClassPathResource("org/apache/activemq/usecases/sender.xml"));
      brokerFactory.afterPropertiesSet();
      senderBroker = brokerFactory.getBroker();

      super.setUp();
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();

      if (receiverBroker != null) {
         receiverBroker.stop();
      }
      if (senderBroker != null) {
         senderBroker.stop();
      }
   }

   @Override
   protected ActiveMQConnectionFactory createReceiverConnectionFactory() throws JMSException {
      try {
         ActiveMQConnectionFactory fac = new ActiveMQConnectionFactory(receiverBroker.getTransportConnectors().get(0).getConnectUri());
         return fac;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   @Override
   protected ActiveMQConnectionFactory createSenderConnectionFactory() throws JMSException {
      try {
         ActiveMQConnectionFactory fac = new ActiveMQConnectionFactory(senderBroker.getTransportConnectors().get(0).getConnectUri());
         return fac;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }

   }
}

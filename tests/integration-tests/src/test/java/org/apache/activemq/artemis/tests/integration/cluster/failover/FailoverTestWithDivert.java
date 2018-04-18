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
package org.apache.activemq.artemis.tests.integration.cluster.failover;

import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.artemis.api.core.ActiveMQBuffer;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
import org.apache.activemq.artemis.api.core.client.ClientConsumer;
import org.apache.activemq.artemis.api.core.client.ClientMessage;
import org.apache.activemq.artemis.api.core.client.ClientProducer;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.ServerLocator;
import org.apache.activemq.artemis.core.client.impl.ClientSessionFactoryInternal;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.junit.Before;
import org.junit.Test;

public class FailoverTestWithDivert extends FailoverTestBase {

   private static final String DIVERT_ADDRESS = "jms.queue.testQueue";
   private static final String DIVERT_FORWARD_ADDRESS = "jms.queue.divertedQueue";
   private ClientSessionFactoryInternal sf;

   @Before
   @Override
   public void setUp() throws Exception {
      super.setUp();
   }

   @Override
   protected TransportConfiguration getAcceptorTransportConfiguration(final boolean live) {
      return getNettyAcceptorTransportConfiguration(live);
   }

   @Override
   protected TransportConfiguration getConnectorTransportConfiguration(final boolean live) {
      return getNettyConnectorTransportConfiguration(live);
   }

   @Test
   public void testUniqueIDsWithDivert() throws Exception {
      Map<String, Object> params = new HashMap<>();
      params.put(TransportConstants.HOST_PROP_NAME, "localhost");
      TransportConfiguration tc = createTransportConfiguration(true, false, params);
      ServerLocator locator = addServerLocator(ActiveMQClient.createServerLocatorWithHA(tc)).setBlockOnNonDurableSend(true).setBlockOnDurableSend(true).setBlockOnAcknowledge(true).setReconnectAttempts(-1);
      sf = createSessionFactoryAndWaitForTopology(locator, 2);
      int minLarge = locator.getMinLargeMessageSize();

      ClientSession session = sf.createSession(false, false);
      addClientSession(session);
      session.start();

      final int num = 100;
      ClientProducer producer = session.createProducer(DIVERT_ADDRESS);
      for (int i = 0; i < num; i++) {
         ClientMessage message = createLargeMessage(session, 2 * minLarge);
         producer.send(message);
      }
      session.commit();

      ClientConsumer consumer = session.createConsumer(DIVERT_ADDRESS);
      for (int i = 0;  i < num; i++) {
         ClientMessage receivedFromSourceQueue = consumer.receive(5000);
         assertNotNull(receivedFromSourceQueue);
         receivedFromSourceQueue.acknowledge();
      }
      session.commit();

      crash(session);

      ClientConsumer consumer1 = session.createConsumer(DIVERT_FORWARD_ADDRESS);
      for (int i = 0; i < num; i++) {
         ClientMessage receivedFromTargetQueue = consumer1.receive(5000);
         assertNotNull(receivedFromTargetQueue);
         receivedFromTargetQueue.acknowledge();
      }
      session.commit();
   }

   private ClientMessage createLargeMessage(ClientSession session, final int largeSize) {
      ClientMessage message = session.createMessage(true);
      ActiveMQBuffer bodyBuffer = message.getBodyBuffer();
      final int propSize = 10240;
      while (bodyBuffer.writerIndex() < largeSize) {
         byte[] prop = new byte[propSize];
         bodyBuffer.writeBytes(prop);
      }
      return message;
   }
}

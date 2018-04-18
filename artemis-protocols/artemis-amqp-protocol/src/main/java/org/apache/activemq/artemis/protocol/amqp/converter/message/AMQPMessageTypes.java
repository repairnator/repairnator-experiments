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
package org.apache.activemq.artemis.protocol.amqp.converter.message;

@Deprecated
public class AMQPMessageTypes {

   // TODO - Remove in future release as these are no longer used by the
   //        inbound JMS Transformer.

   public static final String AMQP_TYPE_KEY = "amqp:type";

   public static final String AMQP_SEQUENCE = "amqp:sequence";

   public static final String AMQP_LIST = "amqp:list";
}

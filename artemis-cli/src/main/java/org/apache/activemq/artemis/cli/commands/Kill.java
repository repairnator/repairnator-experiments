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
package org.apache.activemq.artemis.cli.commands;

import java.io.File;

import io.airlift.airline.Command;
import org.apache.activemq.artemis.dto.BrokerDTO;

@Command(name = "kill", description = "Kills a broker instance started with --allow-kill")
public class Kill extends Configurable {

   @Override
   public Object execute(ActionContext context) throws Exception {
      super.execute(context);

      BrokerDTO broker = getBrokerDTO();

      File file = broker.server.getConfigurationFile().getParentFile();

      File killFile = new File(file, "KILL_ME");

      killFile.createNewFile();

      return null;
   }
}

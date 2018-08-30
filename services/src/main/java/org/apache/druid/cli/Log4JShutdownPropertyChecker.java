/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.cli;

import java.util.Properties;

public class Log4JShutdownPropertyChecker implements PropertyChecker
{
  @Override
  public void checkProperties(Properties properties)
  {
    if (!properties.containsKey("log4j.shutdownCallbackRegistry")) {
      properties.setProperty("log4j.shutdownCallbackRegistry", "org.apache.druid.common.config.Log4jShutdown");
    }
    if (!properties.containsKey("log4j.shutdownHookEnabled")) {
      properties.setProperty("log4j.shutdownHookEnabled", "true");
    }
  }
}

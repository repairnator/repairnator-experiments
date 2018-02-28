/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.twill.internal.state;

import org.apache.twill.api.Command;

/**
 * Factory class for creating instances of {@link Message}.
 */
public final class Messages {

  /**
   * Creates a {@link Message.Type#USER} type {@link Message} that sends the giving {@link Command} to a
   * particular runnable.
   *
   * @param runnableName Name of the runnable.
   * @param command The user command to send.
   * @return A new instance of {@link Message}.
   */
  public static Message createForRunnable(String runnableName, Command command) {
    return new SimpleMessage(Message.Type.USER, Message.Scope.RUNNABLE, runnableName, command);
  }

  /**
   * Creates a {@link Message.Type#USER} type {@link Message} that sends the giving {@link Command} to all
   * runnables.
   *
   * @param command The user command to send.
   * @return A new instance of {@link Message}.
   */
  public static Message createForAll(Command command) {
    return new SimpleMessage(Message.Type.USER, Message.Scope.ALL_RUNNABLE, null, command);
  }

  private Messages() {
  }
}

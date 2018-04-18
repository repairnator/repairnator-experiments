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
package org.apache.activemq.artemis.core.security;

public class User {

   final String user;

   final String password;

   public User(final String user, final String password) {
      this.user = user;
      this.password = password;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      User user1 = (User) o;

      if (!user.equals(user1.user)) {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode() {
      return user.hashCode();
   }

   public boolean isValid(final String user, final String password) {
      if (user == null) {
         return false;
      }
      return this.user.equals(user) && this.password.equals(password);
   }

   public String getUser() {
      return user;
   }

   public String getPassword() {
      return password;
   }
}

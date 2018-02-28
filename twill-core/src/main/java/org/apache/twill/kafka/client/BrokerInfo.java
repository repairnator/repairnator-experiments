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
package org.apache.twill.kafka.client;

import com.google.common.base.Objects;

/**
 * Represents broker information. This class should be serializable with Gson.
 */
public final class BrokerInfo {

  private String host;
  private int port;

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BrokerInfo that = (BrokerInfo) o;
    return host.equals(that.getHost()) && port == that.getPort();
  }

  @Override
  public int hashCode() {
    int result = host.hashCode();
    result = 31 * result + port;
    return result;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(BrokerInfo.class)
                  .add("host", host)
                  .add("port", port)
                  .toString();
  }
}

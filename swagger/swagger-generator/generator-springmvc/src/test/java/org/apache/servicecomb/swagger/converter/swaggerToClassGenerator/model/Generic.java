/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicecomb.swagger.converter.swaggerToClassGenerator.model;

public class Generic<T1, T2> {
  private T1 v1;

  private T2 v2;

  public T1 getV1() {
    return v1;
  }

  public void setV1(T1 v1) {
    this.v1 = v1;
  }

  public T2 getV2() {
    return v2;
  }

  public void setV2(T2 v2) {
    this.v2 = v2;
  }
}

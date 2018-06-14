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
package org.apache.servicecomb.swagger.invocation.response.producer;

import javax.ws.rs.core.Response.StatusType;

import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.converter.Converter;

public class DefaultProducerResponseMapper implements ProducerResponseMapper {
  private Converter converter;

  public DefaultProducerResponseMapper(Converter converter) {
    this.converter = converter;
  }

  @Override
  public Response mapResponse(StatusType status, Object response) {
    Object swaggerResult = converter.convert(response);
    return Response.create(status, swaggerResult);
  }
}

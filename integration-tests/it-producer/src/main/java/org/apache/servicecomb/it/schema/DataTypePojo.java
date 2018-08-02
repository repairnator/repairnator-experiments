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
package org.apache.servicecomb.it.schema;

import javax.servlet.http.HttpServletRequest;

import org.apache.servicecomb.core.Const;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.apache.servicecomb.provider.rest.common.InvocationToHttpServletRequest;
import org.apache.servicecomb.transport.highway.HighwayTransport;

import io.swagger.annotations.SwaggerDefinition;

@RpcSchema(schemaId = "dataTypePojo")
@SwaggerDefinition(basePath = "/v1/dataTypePojo")
public class DataTypePojo {
  public String checkTransport(HttpServletRequest request) {
    if (InvocationToHttpServletRequest.class.isInstance(request)) {
      return HighwayTransport.NAME;
    }

    return Const.RESTFUL;
  }

  public int intBody(int input) {
    return input;
  }
}

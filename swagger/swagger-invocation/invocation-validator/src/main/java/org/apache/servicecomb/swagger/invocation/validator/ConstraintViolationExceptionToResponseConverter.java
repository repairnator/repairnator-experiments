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
package org.apache.servicecomb.swagger.invocation.validator;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.exception.ExceptionToResponseConverter;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

public class ConstraintViolationExceptionToResponseConverter
    implements ExceptionToResponseConverter<ConstraintViolationException> {
  @Override
  public Class<ConstraintViolationException> getExceptionClass() {
    return ConstraintViolationException.class;
  }

  @Override
  public Response convert(SwaggerInvocation swaggerInvocation, ConstraintViolationException e) {
    return Response.createFail(new InvocationException(Status.BAD_REQUEST, e.getConstraintViolations().toString()));
  }

  @Override
  public int getOrder() {
    return -100;
  }
}

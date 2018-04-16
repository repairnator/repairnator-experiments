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
package org.apache.nifi.web.api.config;

import org.apache.nifi.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps not found exceptions into client responses.
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    private static final Logger logger = LoggerFactory.getLogger(NotFoundExceptionMapper.class);

    @Override
    public Response toResponse(NotFoundException exception) {
        // log the error
        logger.info(String.format("%s. Returning %s response.", exception, Status.NOT_FOUND));

        if (logger.isDebugEnabled()) {
            logger.debug(StringUtils.EMPTY, exception);
        }

        return Response.status(Status.NOT_FOUND).entity("The specified resource could not be found.").type("text/plain").build();
    }

}

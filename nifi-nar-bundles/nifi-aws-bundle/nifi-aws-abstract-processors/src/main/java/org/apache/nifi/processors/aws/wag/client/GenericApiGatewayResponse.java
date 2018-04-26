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
package org.apache.nifi.processors.aws.wag.client;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.util.IOUtils;
import java.io.IOException;

public class GenericApiGatewayResponse {
    private final HttpResponse httpResponse;
    private final String body;

    public GenericApiGatewayResponse(HttpResponse httpResponse) throws IOException {
        this.httpResponse = httpResponse;
        if(httpResponse.getContent() != null) {
            this.body = IOUtils.toString(httpResponse.getContent());
        }else {
            this.body = null;
        }
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public String getBody() {
        return body;
    }
}
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jackrabbit.oak.segment.standby.codec;

import java.io.InputStream;

public class GetBlobResponse {

    private final String clientId;

    private final String blobId;

    private final InputStream in;
    
    private final long length;

    public GetBlobResponse(String clientId, String blobId, InputStream in, long length) {
        this.clientId = clientId;
        this.blobId = blobId;
        this.in = in;
        this.length = length;
    }

    public String getClientId() {
        return clientId;
    }

    public String getBlobId() {
        return blobId;
    }

    public InputStream getInputStream() {
        return in;
    }

    public long getLength() {
        return length;
    }
}

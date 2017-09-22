/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.connector.thrift.api;

import com.facebook.swift.codec.ThriftConstructor;
import com.facebook.swift.codec.ThriftField;
import com.facebook.swift.codec.ThriftStruct;

@ThriftStruct
public final class PrestoThriftServiceException
        extends RuntimeException
{
    private final boolean retryable;

    @ThriftConstructor
    public PrestoThriftServiceException(String message, boolean retryable)
    {
        super(message);
        this.retryable = retryable;
    }

    @Override
    @ThriftField(1)
    public String getMessage()
    {
        return super.getMessage();
    }

    @ThriftField(2)
    public boolean isRetryable()
    {
        return retryable;
    }
}

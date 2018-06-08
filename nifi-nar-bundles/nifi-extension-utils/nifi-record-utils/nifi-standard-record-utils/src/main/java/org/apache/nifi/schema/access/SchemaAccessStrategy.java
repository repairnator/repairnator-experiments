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

package org.apache.nifi.schema.access;

import org.apache.nifi.serialization.record.RecordSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public interface SchemaAccessStrategy {

    /**
     * Returns the schema for the given FlowFile using the supplied stream of content and configuration.
     *
     * @param variables Variables which is used to resolve Record Schema via Expression Language.
     *                 This can be null or empty.
     * @param contentStream The stream which is used to read the serialized content.
     * @param readSchema The schema that was read from the input content, or <code>null</code> if there was none.
     * @return The RecordSchema for the content.
     */
    RecordSchema getSchema(Map<String, String> variables, InputStream contentStream, RecordSchema readSchema) throws SchemaNotFoundException, IOException;

    /**
     * @return the set of all Schema Fields that are supplied by the RecordSchema that is returned from {@link #getSchema(Map, InputStream, RecordSchema)}.
     */
    Set<SchemaField> getSuppliedSchemaFields();
}

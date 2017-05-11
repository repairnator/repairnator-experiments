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
package org.apache.nifi.hl7.query.result;

import java.util.Collections;
import java.util.Map;

import org.apache.nifi.hl7.query.ResultHit;

public class StandardResultHit implements ResultHit {

    private final Map<String, Object> values;

    public StandardResultHit(final Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public Object getValue(final String label) {
        return values.get(label);
    }

    @Override
    public Map<String, Object> getSelectedValues() {
        return Collections.unmodifiableMap(values);
    }

}

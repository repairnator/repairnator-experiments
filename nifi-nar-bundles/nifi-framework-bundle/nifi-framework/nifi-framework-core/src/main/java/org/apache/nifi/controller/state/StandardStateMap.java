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

package org.apache.nifi.controller.state;

import java.util.Collections;
import java.util.Map;

import org.apache.nifi.components.state.StateMap;

public class StandardStateMap implements StateMap {
    private final Map<String, String> stateValues;
    private final long version;

    public StandardStateMap(final Map<String, String> stateValues, final long version) {
        this.stateValues = Collections.unmodifiableMap(stateValues == null ? Collections.<String, String> emptyMap() : stateValues);
        this.version = version;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public String get(final String key) {
        return stateValues.get(key);
    }

    @Override
    public Map<String, String> toMap() {
        return stateValues;
    }

    @Override
    public String toString() {
        return "StandardStateMap[version=" + version + ", values=" + stateValues + "]";
    }
}

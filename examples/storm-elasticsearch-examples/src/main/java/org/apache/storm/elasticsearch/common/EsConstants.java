/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.storm.elasticsearch.common;

/**
 * Constants used in ElasticSearch examples.
 */
public final class EsConstants {

    /**
     * The cluster name.
     */
    public static final String CLUSTER_NAME = "test-cluster";
    /**
     * The default wait value in seconds. Necessary in order to avoid magic
     * number anti-pattern.
     */
    public static final int WAIT_DEFAULT = 5;

    /**
     * Utility constructor.
     */
    private EsConstants() {
    }
}

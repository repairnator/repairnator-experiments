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

package org.apache.storm.security;

import java.util.Map;
import org.apache.storm.daemon.Shutdownable;

/**
 * Nimbus auto credential plugin that will be called on nimbus host
 * during submit topology option. User can specify a list of implementation using config key
 * nimbus.autocredential.plugins.classes.
 */
public interface INimbusCredentialPlugin extends Shutdownable {

    /**
     * This method will be called when nimbus initializes.
     * @param conf the cluster config
     */
    void prepare(Map<String, Object> conf);

    /**
     * Method that will be called on nimbus as part of submit topology. This plugin will be called
     * at least once during the submit Topology action. It will be not be called during activate instead
     * the credentials return by this method will be merged with the other credentials in the topology
     * and stored in zookeeper.
     * @param credentials credentials map where more credentials will be added.
     * @param topologyConf topology configuration
     */
    @Deprecated
    default void populateCredentials(Map<String, String> credentials, Map<String, Object> topologyConf) {
        throw new IllegalStateException("One of the populateCredentials methods must be overridden by " + this);
    }

    /**
     * Method that will be called on nimbus as part of submit topology. This plugin will be called
     * at least once during the submit Topology action. It will be not be called during activate instead
     * the credentials return by this method will be merged with the other credentials in the topology
     * and stored in zookeeper.
     * @param credentials credentials map where more credentials will be added.
     * @param topoConf topology configuration
     * @param topologyOwnerPrincipal the full principal name of the owner of the topology
     */
    @SuppressWarnings("deprecation")
    default void populateCredentials(Map<String, String> credentials, Map<String, Object> topoConf, final String topologyOwnerPrincipal) {
        populateCredentials(credentials, topoConf);
    }
}

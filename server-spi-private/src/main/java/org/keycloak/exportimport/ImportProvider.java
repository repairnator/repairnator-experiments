/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.exportimport;

import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.Provider;

import java.io.IOException;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public interface ImportProvider extends Provider {

    void importModel(KeycloakSessionFactory factory, Strategy strategy) throws IOException;

    void importRealm(KeycloakSessionFactory factory, String realmName, Strategy strategy) throws IOException;

    /**
     * @return true if master realm was previously exported and is available in the data to be imported
     * @throws IOException
     */
    boolean isMasterRealmExported() throws IOException;
}

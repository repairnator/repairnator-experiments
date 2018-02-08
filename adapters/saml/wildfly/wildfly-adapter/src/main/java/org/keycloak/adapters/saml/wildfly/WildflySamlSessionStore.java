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

package org.keycloak.adapters.saml.wildfly;

import io.undertow.security.api.SecurityContext;
import io.undertow.server.HttpServerExchange;
import org.keycloak.adapters.saml.SamlDeployment;
import org.keycloak.adapters.saml.SamlSession;
import org.keycloak.adapters.saml.undertow.ServletSamlSessionStore;
import org.keycloak.adapters.spi.SessionIdMapper;
import org.keycloak.adapters.spi.SessionIdMapperUpdater;
import org.keycloak.adapters.undertow.UndertowUserSessionManagement;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class WildflySamlSessionStore extends ServletSamlSessionStore {
    public WildflySamlSessionStore(HttpServerExchange exchange, UndertowUserSessionManagement sessionManagement,
                                   SecurityContext securityContext,
                                   SessionIdMapper idMapper, SessionIdMapperUpdater idMapperUpdater,
                                   SamlDeployment resolvedDeployment) {
        super(exchange, sessionManagement, securityContext, idMapper, idMapperUpdater, resolvedDeployment);
    }

    @Override
    public boolean isLoggedIn() {
        if (super.isLoggedIn()) {
            SecurityInfoHelper.propagateSessionInfo(getAccount());
            return true;
        }
        return false;
    }

    @Override
    public void saveAccount(SamlSession account) {
        super.saveAccount(account);
        SecurityInfoHelper.propagateSessionInfo(account);
    }


}

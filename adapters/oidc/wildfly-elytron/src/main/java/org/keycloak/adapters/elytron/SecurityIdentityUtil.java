/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
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

package org.keycloak.adapters.elytron;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.wildfly.security.auth.callback.AuthenticationCompleteCallback;
import org.wildfly.security.auth.callback.EvidenceVerifyCallback;
import org.wildfly.security.auth.callback.IdentityCredentialCallback;
import org.wildfly.security.auth.callback.SecurityIdentityCallback;
import org.wildfly.security.auth.server.SecurityIdentity;
import org.wildfly.security.credential.BearerTokenCredential;
import org.wildfly.security.evidence.Evidence;
import org.wildfly.security.http.HttpAuthenticationException;

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Igor</a>
 */
final class SecurityIdentityUtil {

    static final SecurityIdentity authorize(CallbackHandler callbackHandler, Principal principal) {
        try {
            EvidenceVerifyCallback evidenceVerifyCallback = new EvidenceVerifyCallback(new Evidence() {
                @Override
                public Principal getPrincipal() {
                    return principal;
                }
            });

            callbackHandler.handle(new Callback[]{evidenceVerifyCallback});

            if (evidenceVerifyCallback.isVerified()) {
                AuthorizeCallback authorizeCallback = new AuthorizeCallback(null, null);

                try {
                    callbackHandler.handle(new Callback[] {authorizeCallback});

                    authorizeCallback.isAuthorized();
                } catch (Exception e) {
                    throw new HttpAuthenticationException(e);
                }

                SecurityIdentityCallback securityIdentityCallback = new SecurityIdentityCallback();
                IdentityCredentialCallback credentialCallback = new IdentityCredentialCallback(new BearerTokenCredential(KeycloakPrincipal.class.cast(principal).getKeycloakSecurityContext().getTokenString()), true);

                callbackHandler.handle(new Callback[]{credentialCallback, AuthenticationCompleteCallback.SUCCEEDED, securityIdentityCallback});

                SecurityIdentity securityIdentity = securityIdentityCallback.getSecurityIdentity();

                return securityIdentity;
            }
        } catch (UnsupportedCallbackException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}

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

package org.keycloak.subsystem.as7;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Add a credential to a deployment.
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2014 Red Hat Inc.
 */
public class CredentialAddHandler extends AbstractAddStepHandler {

    protected Collection<AttributeDefinition> attributes = new LinkedList<>();

    public CredentialAddHandler(AttributeDefinition... attrs) {
        for (AttributeDefinition attr : attrs) {
            attributes.add(attr);
        }
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        if (attributes != null) {
            for (AttributeDefinition attr : attributes) {
                attr.validateAndSet(operation, model);
            }
        }
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        KeycloakAdapterConfigService ckService = KeycloakAdapterConfigService.getInstance();
        ckService.addCredential(operation, context.resolveExpressions(model));
    }

}

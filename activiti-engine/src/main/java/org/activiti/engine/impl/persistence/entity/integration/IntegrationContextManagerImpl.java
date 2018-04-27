/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.persistence.entity.integration;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.AbstractEntityManager;
import org.activiti.engine.impl.persistence.entity.data.DataManager;
import org.activiti.engine.impl.persistence.entity.data.integration.IntegrationContextDataManager;

import java.util.List;

public class IntegrationContextManagerImpl extends AbstractEntityManager<IntegrationContextEntity> implements IntegrationContextManager {

    private final IntegrationContextDataManager dataManager;

    public IntegrationContextManagerImpl(ProcessEngineConfigurationImpl processEngineConfiguration, IntegrationContextDataManager dataManager) {
        super(processEngineConfiguration);
        this.dataManager = dataManager;
    }

    @Override
    protected DataManager<IntegrationContextEntity> getDataManager() {
        return dataManager;
    }

    @Override
    public List<IntegrationContextEntity> findIntegrationContextByExecutionId(String executionId) {
        return dataManager.findIntegrationContextByExecutionId(executionId);
    }

}

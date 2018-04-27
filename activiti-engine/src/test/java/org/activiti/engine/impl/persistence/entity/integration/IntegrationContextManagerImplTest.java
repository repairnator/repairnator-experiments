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

import org.activiti.engine.impl.persistence.entity.data.DataManager;
import org.activiti.engine.impl.persistence.entity.data.integration.IntegrationContextDataManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class IntegrationContextManagerImplTest {

    @InjectMocks
    private IntegrationContextManagerImpl manager;

    @Mock
    private IntegrationContextDataManager dataManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void getDataManagerShouldReturnIntegrationContextDataManager() throws Exception {
        //when
        DataManager<IntegrationContextEntity> retrievedDataManager = manager.getDataManager();

        //then
        assertThat(retrievedDataManager).isEqualTo(dataManager);
    }

    @Test
    public void findIntegrationContextByExecutionIdShouldReturnResultFromDataManager() throws Exception {
        //given
        IntegrationContextEntity entity = mock(IntegrationContextEntity.class);
        given(dataManager.findIntegrationContextByExecutionId("execId")).willReturn(Arrays.asList(entity));

        //when
        List<IntegrationContextEntity> retrievedResults = manager.findIntegrationContextByExecutionId("execId");

        //then
        assertThat(retrievedResults).contains(entity);
        assertThat(retrievedResults).hasSize(1);
    }

}
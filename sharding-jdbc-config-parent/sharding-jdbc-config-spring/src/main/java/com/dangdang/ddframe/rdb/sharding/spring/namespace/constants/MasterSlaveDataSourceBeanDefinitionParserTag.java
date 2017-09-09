/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.rdb.sharding.spring.namespace.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Master-slave data source parser tag constants.
 * 
 * @author zhangliang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MasterSlaveDataSourceBeanDefinitionParserTag {
    
    public static final String MASTER_DATA_SOURCE_REF_ATTRIBUTE = "master-data-source-ref";
    
    public static final String SLAVE_DATA_SOURCES_REF_ATTRIBUTE = "slave-data-sources-ref";
    
    public static final String STRATEGY_REF_ATTRIBUTE = "strategy-ref";
    
    public static final String STRATEGY_TYPE_ATTRIBUTE = "strategy-type";
}

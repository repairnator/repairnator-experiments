/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
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
 * </p>
 */

package com.dangdang.ddframe.job.lite.internal.instance;

import com.dangdang.ddframe.job.lite.internal.listener.AbstractJobListener;
import com.dangdang.ddframe.job.lite.internal.listener.AbstractListenerManager;
import com.dangdang.ddframe.job.lite.internal.schedule.SchedulerFacade;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;

/**
 * 运行实例关闭监听管理器.
 * 
 * @author zhangliang
 */
public class ShutdownListenerManager extends AbstractListenerManager {
    
    private final InstanceNode instanceNode;
    
    private final SchedulerFacade schedulerFacade;
    
    public ShutdownListenerManager(final CoordinatorRegistryCenter regCenter, final String jobName) {
        super(regCenter, jobName);
        instanceNode = new InstanceNode(jobName);
        schedulerFacade = new SchedulerFacade(regCenter, jobName);
    }
    
    @Override
    public void start() {
        addDataListener(new InstanceShutdownStatusJobListener());
    }
    
    class InstanceShutdownStatusJobListener extends AbstractJobListener {
        
        @Override
        protected void dataChanged(final String path, final Type eventType, final String data) {
            if (instanceNode.isLocalInstancePath(path) && Type.NODE_REMOVED == eventType) {
                schedulerFacade.shutdownInstance();
            }
        }
    }
}

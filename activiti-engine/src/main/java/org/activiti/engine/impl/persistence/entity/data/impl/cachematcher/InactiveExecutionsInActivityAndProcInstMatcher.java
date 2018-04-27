/* Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.activiti.engine.impl.persistence.entity.data.impl.cachematcher;

import java.util.Map;

import org.activiti.engine.impl.persistence.CachedEntityMatcherAdapter;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**

 */
public class InactiveExecutionsInActivityAndProcInstMatcher extends CachedEntityMatcherAdapter<ExecutionEntity> {
  
  @Override
  public boolean isRetained(ExecutionEntity executionEntity, Object parameter) {
    Map<String, Object> paramMap = (Map<String, Object>) parameter;
    String activityId = (String) paramMap.get("activityId");
    String processInstanceId = (String) paramMap.get("processInstanceId");
    
    return executionEntity.getProcessInstanceId() != null 
        && executionEntity.getProcessInstanceId().equals(processInstanceId) 
        && !executionEntity.isActive() 
        && executionEntity.getActivityId() != null 
        && executionEntity.getActivityId().equals(activityId);
  }

}
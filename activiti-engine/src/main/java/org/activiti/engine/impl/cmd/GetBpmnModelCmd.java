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
package org.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

/**

 */
public class GetBpmnModelCmd implements Command<BpmnModel>, Serializable {

  private static final long serialVersionUID = 8167762371289445046L;

  protected String processDefinitionId;

  public GetBpmnModelCmd(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
  }

  public BpmnModel execute(CommandContext commandContext) {
    if (processDefinitionId == null) {
      throw new ActivitiIllegalArgumentException("processDefinitionId is null");
    }

    return ProcessDefinitionUtil.getBpmnModel(processDefinitionId);
  }
}
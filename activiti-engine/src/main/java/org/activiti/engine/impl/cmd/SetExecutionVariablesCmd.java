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

import java.util.Map;

import org.activiti.engine.compatibility.Activiti5CompatibilityHandler;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.util.Activiti5Util;

/**


 */
public class SetExecutionVariablesCmd extends NeedsActiveExecutionCmd<Object> {

  private static final long serialVersionUID = 1L;

  protected Map<String, ? extends Object> variables;
  protected boolean isLocal;

  public SetExecutionVariablesCmd(String executionId, Map<String, ? extends Object> variables, boolean isLocal) {
    super(executionId);
    this.variables = variables;
    this.isLocal = isLocal;
  }

  protected Object execute(CommandContext commandContext, ExecutionEntity execution) {
    
    if (Activiti5Util.isActiviti5ProcessDefinitionId(commandContext, execution.getProcessDefinitionId())) {
      Activiti5CompatibilityHandler activiti5CompatibilityHandler = Activiti5Util.getActiviti5CompatibilityHandler(); 
      activiti5CompatibilityHandler.setExecutionVariables(executionId, variables, isLocal);
      return null;
    }
    
    if (isLocal) {
      if (variables != null) {
        for (String variableName : variables.keySet()) {
          execution.setVariableLocal(variableName, variables.get(variableName), false);
        }
      }
    } else {
      if (variables != null) {
        for (String variableName : variables.keySet()) {
          execution.setVariable(variableName, variables.get(variableName), false);
        }
      }
    }

    // ACT-1887: Force an update of the execution's revision to prevent
    // simultaneous inserts of the same
    // variable. If not, duplicate variables may occur since optimistic
    // locking doesn't work on inserts
    execution.forceUpdate();
    return null;
  }

  @Override
  protected String getSuspendedExceptionMessage() {
    return "Cannot set variables because execution '" + executionId + "' is suspended";
  }

}

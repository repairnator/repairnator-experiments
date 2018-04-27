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

package org.activiti.engine.test.bpmn.event.compensate.helper;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**

 */
public class UndoService implements JavaDelegate {

  private Expression counterName;

  public void execute(DelegateExecution execution) {
    String variableName = (String) counterName.getValue(execution);
    Object variable = execution.getVariable(variableName);
    if (variable == null) {
      execution.setVariable(variableName, 1);
    } else {
      execution.setVariable(variableName, ((Integer) variable) + 1);
    }
  }

}

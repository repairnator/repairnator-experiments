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
package org.activiti.examples.bpmn.scripttask;

import groovy.lang.MissingPropertyException;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**


 */
public class ScriptTaskTest extends PluggableActivitiTestCase {

  @Deployment
  public void testSetScriptResultToProcessVariable() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("echo", "hello");
    variables.put("existingProcessVariableName", "one");

    ProcessInstance pi = runtimeService.startProcessInstanceByKey("setScriptResultToProcessVariable", variables);

    assertEquals("hello", runtimeService.getVariable(pi.getId(), "existingProcessVariableName"));
    assertEquals(pi.getId(), runtimeService.getVariable(pi.getId(), "newProcessVariableName"));
  }

  @Deployment
  public void testFailingScript() {
    Exception expectedException = null;
    try {
      runtimeService.startProcessInstanceByKey("failingScript");
    } catch (Exception e) {
      expectedException = e;
    }

    // Check if correct exception is found in the stacktrace
    verifyExceptionInStacktrace(expectedException, MissingPropertyException.class);
  }

  @Deployment
  public void testExceptionThrownInScript() {
    Exception expectedException = null;
    try {
      runtimeService.startProcessInstanceByKey("failingScript");
    } catch (Exception e) {
      expectedException = e;
    }

    verifyExceptionInStacktrace(expectedException, IllegalStateException.class);
  }

  @Deployment
  public void testAutoStoreVariables() {
    // The first script should NOT store anything as 'autoStoreVariables' is set to false
    String id = runtimeService.startProcessInstanceByKey("testAutoStoreVariables", CollectionUtil.map("a", 20, "b", 22)).getId();
    assertNull(runtimeService.getVariable(id, "sum"));

    // The second script, after the user task will set the variable
    taskService.complete(taskService.createTaskQuery().singleResult().getId());
    assertEquals(42, ((Number) runtimeService.getVariable(id, "sum")).intValue());
  }

  public void testNoScriptProvided() {
    try {
      repositoryService.createDeployment().addClasspathResource("org/activiti/examples/bpmn/scripttask/ScriptTaskTest.testNoScriptProvided.bpmn20.xml").deploy();
    } catch (ActivitiException e) {
      assertTextPresent("No script provided", e.getMessage());
    }
  }

  @Deployment
  public void testDynamicScript() {
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testDynamicScript", CollectionUtil.map("a", 20, "b", 22));
    assertEquals(42, ((Number) runtimeService.getVariable(processInstance.getId(), "test")).intValue());
    taskService.complete(taskService.createTaskQuery().singleResult().getId());
    assertProcessEnded(processInstance.getId());

    String processDefinitionId = processInstance.getProcessDefinitionId();
    ObjectNode infoNode = dynamicBpmnService.changeScriptTaskScript("script1", "var sum = c + d;\nexecution.setVariable('test2', sum);");
    dynamicBpmnService.saveProcessDefinitionInfo(processDefinitionId, infoNode);

    processInstance = runtimeService.startProcessInstanceByKey("testDynamicScript", CollectionUtil.map("c", 10, "d", 12));
    assertEquals(22, ((Number) runtimeService.getVariable(processInstance.getId(), "test2")).intValue());
    taskService.complete(taskService.createTaskQuery().singleResult().getId());
    assertProcessEnded(processInstance.getId());
  }

  protected void verifyExceptionInStacktrace(Exception rootException, Class<?> expectedExceptionClass) {
    Throwable expectedException = rootException;
    boolean found = false;
    while (!found && expectedException != null) {
      if (expectedException.getClass().equals(expectedExceptionClass)) {
        found = true;
      } else {
        expectedException = expectedException.getCause();
      }
    }

    assertEquals(expectedExceptionClass, expectedException.getClass());
  }

}

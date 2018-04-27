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
package org.activiti.engine.test.api.repository;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.test.Deployment;

/**

 */
public class DeployNonExecutableProcessDefinitionTest extends PluggableActivitiTestCase {

  /*
   * Test for https://jira.codehaus.org/browse/ACT-2071
   * 
   * In this test, a process definition is deployed together with one that is not executable. The none-executable should not be startable.
   */
  @Deployment
  public void testDeployNonExecutableProcessDefinition() {
    try {
      runtimeService.startProcessInstanceByKey("oneTaskProcessNonExecutable");
      fail();
    } catch (ActivitiException e) {
      assertTextPresent("no processes deployed with key 'oneTaskProcessNonExecutable'", e.getMessage());
    }
  }

}

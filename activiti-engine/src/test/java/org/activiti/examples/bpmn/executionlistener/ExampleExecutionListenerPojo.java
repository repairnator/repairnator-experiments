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

package org.activiti.examples.bpmn.executionlistener;

import java.io.Serializable;

/**
 * Simple pojo than will be used to act as an event listener.
 * 

 */
public class ExampleExecutionListenerPojo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String receivedEventName;

  public void myMethod(String eventName) {
    this.receivedEventName = eventName;
  }

  public String getReceivedEventName() {
    return receivedEventName;
  }

  public void setReceivedEventName(String receivedEventName) {
    this.receivedEventName = receivedEventName;
  }

}

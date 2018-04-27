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
package org.activiti.engine.impl;

import java.io.Serializable;

import org.activiti.engine.ProcessEngineInfo;

/**

 */
public class ProcessEngineInfoImpl implements Serializable, ProcessEngineInfo {

  private static final long serialVersionUID = 1L;

  String name;
  String resourceUrl;
  String exception;

  public ProcessEngineInfoImpl(String name, String resourceUrl, String exception) {
    this.name = name;
    this.resourceUrl = resourceUrl;
    this.exception = exception;
  }

  public String getName() {
    return name;
  }

  public String getResourceUrl() {
    return resourceUrl;
  }

  public String getException() {
    return exception;
  }
}

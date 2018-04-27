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

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.NativeHistoricActivityInstanceQuery;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

public class NativeHistoricActivityInstanceQueryImpl extends AbstractNativeQuery<NativeHistoricActivityInstanceQuery, HistoricActivityInstance> implements NativeHistoricActivityInstanceQuery {

  private static final long serialVersionUID = 1L;

  public NativeHistoricActivityInstanceQueryImpl(CommandContext commandContext) {
    super(commandContext);
  }

  public NativeHistoricActivityInstanceQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  // results ////////////////////////////////////////////////////////////////

  public List<HistoricActivityInstance> executeList(CommandContext commandContext, Map<String, Object> parameterMap, int firstResult, int maxResults) {
    return commandContext.getHistoricActivityInstanceEntityManager().findHistoricActivityInstancesByNativeQuery(parameterMap, firstResult, maxResults);
  }

  public long executeCount(CommandContext commandContext, Map<String, Object> parameterMap) {
    return commandContext.getHistoricActivityInstanceEntityManager().findHistoricActivityInstanceCountByNativeQuery(parameterMap);
  }

}

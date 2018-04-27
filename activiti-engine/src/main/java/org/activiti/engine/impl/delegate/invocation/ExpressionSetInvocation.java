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
package org.activiti.engine.impl.delegate.invocation;

import javax.el.ELContext;
import javax.el.ValueExpression;

/**
 * Class responsible for handling Expression.setValue() invocations.
 * 

 */
public class ExpressionSetInvocation extends ExpressionInvocation {

  protected final Object value;
  protected ELContext elContext;

  public ExpressionSetInvocation(ValueExpression valueExpression, ELContext elContext, Object value) {
    super(valueExpression);
    this.value = value;
    this.elContext = elContext;
    this.invocationParameters = new Object[] { value };
  }

  @Override
  protected void invoke() {
    valueExpression.setValue(elContext, value);
  }

}

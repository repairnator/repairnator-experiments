/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.struts2.convention.actions.interceptor;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

/**
 * <p>
 * This is a test action with one interceptor at the action level.
 * </p>
 */
@InterceptorRef("interceptor-1")
public class ActionLevelInterceptorAction {

    @Action(value = "action500")
    public String run1() throws Exception {
        return null;
    }

    @Action(value = "action600", interceptorRefs = @InterceptorRef("interceptor-2"))
    public String run2() throws Exception {
        return null;
    }

    @Action(value = "action700", interceptorRefs = @InterceptorRef("stack-1"))
    public String run3() throws Exception {
        return null;
    }
}

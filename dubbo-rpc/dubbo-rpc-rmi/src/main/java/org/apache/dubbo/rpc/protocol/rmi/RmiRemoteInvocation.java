/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc.protocol.rmi;

import org.apache.dubbo.rpc.RpcContext;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.RemoteInvocation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RmiRemoteInvocation extends RemoteInvocation {
    private static final long serialVersionUID = 1L;
    private static final String dubboAttachmentsAttrName = "dubbo.attachments";

    /**
     * executed on consumer side
     */
    public RmiRemoteInvocation(MethodInvocation methodInvocation) {
        super(methodInvocation);
        addAttribute(dubboAttachmentsAttrName, new HashMap<String, String>(RpcContext.getContext().getAttachments()));
    }

    /**
     * Need to restore context on provider side (Though context will be overridden by Invocation's attachment
     * when ContextFilter gets executed, we will restore the attachment when Invocation is constructed, check more
     * from {@link org.apache.dubbo.rpc.proxy.InvokerInvocationHandler}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object targetObject) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        RpcContext context = RpcContext.getContext();
        context.setAttachments((Map<String, String>) getAttribute(dubboAttachmentsAttrName));
        try {
            return super.invoke(targetObject);
        } finally {
            context.setAttachments(null);
        }
    }
}
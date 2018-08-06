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
package com.alipay.sofa.rpc.codec.jackson;

import com.alipay.sofa.rpc.common.utils.ClassUtils;
import com.alipay.sofa.rpc.config.ConfigUniqueNameGenerator;
import com.alipay.sofa.rpc.core.exception.SofaRpcRuntimeException;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 景竹 2018/7/28
 */
public class JacksonSerializerHelper {

    /**
     * 请求参数类型缓存 {service+method:class}
     */
    private final ConcurrentHashMap<String, Class[]> requestClassCache  = new ConcurrentHashMap();

    /**
     * 返回结果类型缓存 {service+method:class}
     */
    private final ConcurrentHashMap<String, Class>   responseClassCache = new ConcurrentHashMap();

    public Class[] getReqClass(String service, String methodName) {

        String key = buildMethodKey(service, methodName);
        Class[] reqClass = requestClassCache.get(key);
        if (reqClass == null) {
            // 读取接口里的方法参数和返回值
            String interfaceClass = ConfigUniqueNameGenerator.getInterfaceName(service);
            Class clazz = ClassUtils.forName(interfaceClass, true);
            setClassToCache(key, clazz, methodName);
        }
        return requestClassCache.get(key);
    }

    private String buildMethodKey(String serviceName, String methodName) {
        return serviceName + "#" + methodName;
    }

    private void setClassToCache(String key, Class clazz, String methodName) {
        Method pbMethod = null;
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                pbMethod = method;
                break;
            }
        }
        if (pbMethod == null) {
            throw new SofaRpcRuntimeException("Cannot found method: " + clazz.getName() + "." + methodName);
        }
        Class[] parameterTypes = pbMethod.getParameterTypes();
        requestClassCache.put(key, parameterTypes);
        //set return type
        Class resClass = pbMethod.getReturnType();
        if (resClass == void.class) {
            throw new SofaRpcRuntimeException("class " + clazz.getName()
                + ", only support return message!");
        }
        responseClassCache.put(key, resClass);
    }

    public Class getResClass(String service, String methodName) {
        String key = service + "#" + methodName;
        Class reqClass = responseClassCache.get(key);
        if (reqClass == null) {
            // 读取接口里的方法参数和返回值
            String interfaceClass = ConfigUniqueNameGenerator.getInterfaceName(service);
            Class clazz = ClassUtils.forName(interfaceClass, true);
            setClassToCache(key, clazz, methodName);
        }
        return responseClassCache.get(key);
    }

}

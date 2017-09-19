/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.sql;

import com.facebook.presto.metadata.FunctionRegistry;
import com.facebook.presto.metadata.Signature;
import com.facebook.presto.operator.scalar.ScalarFunctionImplementation;
import com.facebook.presto.spi.ConnectorSession;
import com.google.common.base.Defaults;
import com.google.common.base.Throwables;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.invoke.MethodHandleProxies.asInterfaceInstance;
import static java.util.Objects.requireNonNull;

public class FunctionInvoker
{
    private final FunctionRegistry registry;

    public FunctionInvoker(FunctionRegistry registry)
    {
        this.registry = requireNonNull(registry, "registry is null");
    }

    public Object invoke(Signature function, ConnectorSession session, Object... arguments)
    {
        return invoke(function, session, Arrays.asList(arguments));
    }

    /**
     * Arguments must be the native container type for the corresponding SQL types.
     * <p>
     * Returns a value in the native container type corresponding to the declared SQL return type
     */
    public Object invoke(Signature function, ConnectorSession session, List<Object> arguments)
    {
        ScalarFunctionImplementation implementation = registry.getScalarFunctionImplementation(function);
        MethodHandle method = implementation.getMethodHandle();

        // handle function on instance method, to allow use of fields
        method = bindInstanceFactory(method, implementation);

        if (method.type().parameterCount() > 0 && method.type().parameterType(0) == ConnectorSession.class) {
            method = method.bindTo(session);
        }
        List<Object> actualArguments = new ArrayList<>();
        for (int i = 0; i < arguments.size(); i++) {
            Object argument = arguments.get(i);
            Optional<Class> lambdaArgument = implementation.getLambdaInterface().get(i);
            if (lambdaArgument.isPresent() && !MethodHandle.class.equals(lambdaArgument.get())) {
                argument = asInterfaceInstance(lambdaArgument.get(), (MethodHandle) argument);
            }
            if (implementation.getNullFlags().get(i)) {
                boolean isNull = argument == null;
                if (isNull) {
                    argument = Defaults.defaultValue(method.type().parameterType(actualArguments.size()));
                }
                actualArguments.add(argument);
                actualArguments.add(isNull);
            }
            else {
                actualArguments.add(argument);
            }
        }

        try {
            return method.invokeWithArguments(actualArguments);
        }
        catch (Throwable throwable) {
            throw Throwables.propagate(throwable);
        }
    }

    private static MethodHandle bindInstanceFactory(MethodHandle method, ScalarFunctionImplementation implementation)
    {
        if (!implementation.getInstanceFactory().isPresent()) {
            return method;
        }

        try {
            return method.bindTo(implementation.getInstanceFactory().get().invoke());
        }
        catch (Throwable throwable) {
            if (throwable instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw Throwables.propagate(throwable);
        }
    }
}

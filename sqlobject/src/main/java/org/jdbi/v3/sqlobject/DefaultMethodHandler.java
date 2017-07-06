/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.sqlobject;

import static java.util.Collections.synchronizedMap;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import org.jdbi.v3.core.extension.HandleSupplier;

class DefaultMethodHandler implements Handler {
    private static final Map<Class<?>, MethodHandles.Lookup> privateLookups = synchronizedMap(new WeakHashMap<>());

    private static MethodHandles.Lookup lookupFor(Class<?> clazz) {
        return privateLookups.computeIfAbsent(clazz, type -> {
            try {
                // TERRIBLE, HORRIBLE, NO GOOD, VERY BAD HACK
                // Courtesy of:
                // https://rmannibucau.wordpress.com/2014/03/27/java-8-default-interface-methods-and-jdk-dynamic-proxies/

                // We can use MethodHandles to look up and invoke the super method, but since this class is not an
                // implementation of method.getDeclaringClass(), MethodHandles.Lookup will throw an exception since
                // this class doesn't have access to the super method, according to Java's access rules. This horrible,
                // awful workaround allows us to directly invoke MethodHandles.Lookup's private constructor, bypassing
                // the usual access checks.

                // We should get rid of this workaround as soon as a viable alternative exists.

                final Constructor<MethodHandles.Lookup> constructor =
                        MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance(type, MethodHandles.Lookup.PRIVATE);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private final MethodHandle methodHandle;

    DefaultMethodHandler(Method method) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();

            methodHandle = lookupFor(declaringClass).unreflectSpecial(method, declaringClass);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invoke(Object target, Object[] args, HandleSupplier handle) {
        try {
            return methodHandle.bindTo(target).invokeWithArguments(args);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}

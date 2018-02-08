/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
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
package org.keycloak.client.admin.cli.aesh;

import org.jboss.aesh.console.AeshConsoleImpl;
import org.jboss.aesh.console.Console;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:mstrukel@redhat.com">Marko Strukelj</a>
 */
public class AeshEnhancer {

    public static void enhance(AeshConsoleImpl console) {
        try {
            Globals.stdin.setConsole(console);

            Field field = AeshConsoleImpl.class.getDeclaredField("console");
            field.setAccessible(true);
            Console internalConsole = (Console) field.get(console);
            internalConsole.setConsoleCallback(new AeshConsoleCallbackImpl(console));
        } catch (Exception e) {
            throw new RuntimeException("Failed to install Aesh enhancement", e);
        }
    }
}

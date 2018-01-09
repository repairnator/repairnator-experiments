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
package org.apache.struts2.oval.interceptor;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.configuration.annotation.IsInvariant;

public class ValidationInMethods extends ActionSupport {
    @IsInvariant
    @NotNull
    public String getName() {
        return null;
    }

    @IsInvariant
    @NotNull
    public Boolean isThereAnyMeaningInLife() {
        return null;
    }

    @IsInvariant 
    @NotNull
    public String getTheManingOfLife() {
        return null;
    }

    @IsInvariant
    @NotNull
    public String SisyphusHasTheAnswer() {
        //some method annotated, whose name is not a field
        return null;
    }
}
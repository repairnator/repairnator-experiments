/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.jackrabbit.oak.query.ast;

import org.apache.jackrabbit.oak.api.PropertyValue;

/**
 * A bind variable.
 */
public class BindVariableValueImpl extends StaticOperandImpl {

    private final String bindVariableName;

    public BindVariableValueImpl(String bindVariableName) {
        this.bindVariableName = bindVariableName;
    }

    public String getBindVariableName() {
        return bindVariableName;
    }

    @Override
    boolean accept(AstVisitor v) {
        return v.visit(this);
    }

    @Override
    public PropertyValue currentValue() {
        return query.getBindVariableValue(bindVariableName);
    }
    
    @Override
    int getPropertyType() {
        PropertyValue v = currentValue();
        return v.getType().tag();
    }

    //------------------------------------------------------------< Object >--

    @Override
    public String toString() {
        return '$' + bindVariableName;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof BindVariableValueImpl) {
            return bindVariableName.equals(
                    ((BindVariableValueImpl) that).bindVariableName);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return bindVariableName.hashCode();
    }

}

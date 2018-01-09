/*
 * $Id$
 *
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

package org.apache.struts2.views.java.simple;

import org.apache.struts2.components.UIBean;

public abstract class AbstractCommonAttributesTest extends AbstractTest {
    public void testRenderTextFieldScriptingAttrs() throws Exception {
        UIBean tag = getUIBean();

        applyScriptingAttrs(tag);

        tag.evaluateParams();
        map.putAll(tag.getParameters());
        theme.renderTag(getTagName(), context);
        String output = writer.getBuffer().toString();

        assertScriptingAttrs(output);
    }

    public void testRenderTextFieldCommonAttrs() throws Exception {
        UIBean tag = getUIBean();


        applyCommonAttrs(tag);

        tag.evaluateParams();
        map.putAll(tag.getParameters());
        theme.renderTag(getTagName(), context);
        String output = writer.getBuffer().toString();

        assertCommonAttrs(output);
    }

    public void testRenderTextFieldDynamicAttrs() throws Exception {
        UIBean tag = getUIBean();

        applyDynamicAttrs(tag);

        tag.evaluateParams();
        map.putAll(tag.getParameters());
        theme.renderTag(getTagName(), context);
        String output = writer.getBuffer().toString();

        assertDynamicAttrs(output);
    }
}

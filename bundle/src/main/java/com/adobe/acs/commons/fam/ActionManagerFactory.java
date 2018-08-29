/*
 * Copyright 2016 Adobe.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package com.adobe.acs.commons.fam;

import aQute.bnd.annotation.ProviderType;
import com.adobe.acs.commons.fam.mbean.ActionManagerMBean;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;

/*
 * In addition to the mbean methods, the implementation factory object also provides a method to create a new ActionManager
 */
@ProviderType
public interface ActionManagerFactory extends ActionManagerMBean {
    public ActionManager createTaskManager(String name, ResourceResolver resourceResolver, int saveInterval) throws LoginException;
}

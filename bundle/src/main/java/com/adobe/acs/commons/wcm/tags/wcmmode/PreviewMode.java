/*
 * #%L
 * ACS AEM Commons Bundle
 * %%
 * Copyright (C) 2013 Adobe
 * %%
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
 * #L%
 */
package com.adobe.acs.commons.wcm.tags.wcmmode;

import tldgen.BodyContentType;
import tldgen.Tag;
import aQute.bnd.annotation.ProviderType;

import com.day.cq.wcm.api.WCMMode;

/**
 * Implementation of the &lt;wcmmode:preview&gt; tag, everything inside this tag
 * will be executed when the <code>WCMMode</code> is <code>PREVIEW</code>.
 * You can also specify the attribute <code>not=true</code>, then it will be executed
 * when the <code>WCMMode</code> is *not* <code>PREVIEW</code>
 *
 * @see <a href="http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/WCMMode.html">WCMMode</a>
 */
@ProviderType
@Tag(bodyContentType = BodyContentType.JSP, value = "preview")
public final class PreviewMode extends AbstractMode {

    private static final long serialVersionUID = -6353861403815741277L;

    @Override
    protected WCMMode getMode() {
        return WCMMode.PREVIEW;
    }

}

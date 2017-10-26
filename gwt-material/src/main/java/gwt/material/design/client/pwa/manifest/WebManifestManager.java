/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
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
package gwt.material.design.client.pwa.manifest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import gwt.material.design.client.pwa.PwaFeature;
import gwt.material.design.client.pwa.PwaManager;

public class WebManifestManager extends PwaFeature {

    private String url;

    private Element manifestElement;

    public WebManifestManager(PwaManager manager) {
        super(manager);
    }

    @Override
    public void load(String url) {
        this.url = url;
        if (url != null && !url.isEmpty()) {
            // Check whether manifestElement was already attached to the head element.
            if (manifestElement == null) {
                manifestElement = Document.get().createLinkElement();
                getManager().getHeadElement().appendChild(manifestElement);
            }

            manifestElement.setAttribute("rel", "manifest");
            manifestElement.setAttribute("href", url);
        }
    }

    @Override
    public void unload() {
        if (manifestElement != null) {
            manifestElement.removeFromParent();
            manifestElement = null;
            GWT.log("Web manifest has been unloaded.");
        }
    }

    @Override
    public void reload() {
        unload();
        load(url);
    }
}

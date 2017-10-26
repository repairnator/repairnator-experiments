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
package gwt.material.design.client.pwa.theme;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import gwt.material.design.client.pwa.PwaFeature;
import gwt.material.design.client.pwa.PwaManager;

public class BrowserThemeManager extends PwaFeature {

    private Element themeColorElement;
    private String themeColor;

    public BrowserThemeManager(PwaManager manager) {
        super(manager);
    }

    @Override
    public void load(String themeColor) {
        this.themeColor = themeColor;
        if (this.themeColor != null && !this.themeColor.isEmpty()) {
            if (themeColorElement == null) {
                themeColorElement = Document.get().createMetaElement();
                getManager().getHeadElement().appendChild(themeColorElement);
            }
            themeColorElement.setAttribute("name", "theme-color");
            themeColorElement.setAttribute("content", this.themeColor);
        }
    }

    @Override
    public void unload() {
        if (themeColorElement != null) {
            themeColorElement.removeFromParent();
            themeColorElement = null;
            GWT.log("Meta theme color has been unloaded.");
        }
    }

    @Override
    public void reload() {
        unload();
        load(themeColor);
    }
}

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
package gwt.material.design.client.base.mixin;

import com.google.gwt.user.client.ui.UIObject;
import gwt.material.design.client.base.helper.StyleHelper;

/**
 * @author Ben Dol
 */
public class ToggleStyleMixin<H extends UIObject> extends AbstractMixin<H> {

    private String style;

    public ToggleStyleMixin(final H widget, String style) {
        super(widget);

        assert !style.isEmpty() : "Style name cannot be empty";

        this.style = style;
    }

    public void setOn(boolean on) {
        uiObject.removeStyleName(style);
        if (on) {
            uiObject.addStyleName(style);
        }
    }

    public boolean isOn() {
        return StyleHelper.containsStyle(uiObject.getStyleName(), style);
    }
}

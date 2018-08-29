/*
 * #%L
 * ACS AEM Commons Bundle
 * %%
 * Copyright (C) 2016 Adobe
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
package com.adobe.acs.commons.dam.impl;

import com.adobe.acs.commons.util.ParameterUtil;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.osgi.PropertiesUtil;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

@SlingServlet(paths = "/bin/acs-commons/dam/custom-components.json", generateComponent = false)
@Component(metatype = true, policy = ConfigurationPolicy.REQUIRE, label = "ACS AEM Commons - Custom DAM Component List Servlet",
    description = "Servlet to list custom component paths to automatically replace in metadata editor.")
public class CustomComponentActivatorListServlet extends SlingSafeMethodsServlet {

    private static final String HISTORY = "xmpMM:History=/apps/acs-commons/dam/content/admin/history";
    private static final String FONTS =  "xmpTPg:Fonts=/apps/acs-commons/dam/content/admin/fonts";
    private static final String COLORANTS = "xmpTPg:Colorants=/apps/acs-commons/dam/content/admin/color-swatches";
    private static final String LOCATION = "location=/apps/acs-commons/dam/content/admin/asset-location-map";

    private static final String[] DEFAULT_COMPONENTS = { HISTORY, FONTS, COLORANTS, LOCATION };

    @Property(label = "Components", description = "Map in the form <propertyName>=<replacement path>", value = {
            HISTORY,
            FONTS,
            COLORANTS,
            LOCATION
        })
    public static String PROP_COMPONENTS = "components";

    private JSONObject json;

    @Activate
    protected void activate(Map<String, Object> config) throws JSONException {
        Map<String, String> components = ParameterUtil.toMap(PropertiesUtil.toStringArray(config.get(PROP_COMPONENTS), DEFAULT_COMPONENTS),"=");
        JSONArray array = new JSONArray();
        for (Map.Entry<String, String> entry : components.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("propertyName", entry.getKey());
            obj.put("componentPath", entry.getValue());
            array.put(obj);
        }
        this.json = new JSONObject();
        json.put("components", array);
    }

    @Override
    protected void doGet(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            json.write(response.getWriter());
        } catch (JSONException e) {
            throw new ServletException(e);
        }
    }
}

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

package org.keycloak.representations.info;

import org.keycloak.common.Profile;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class ProfileInfoRepresentation {

    private String name;
    private List<String> disabledFeatures;

    public static ProfileInfoRepresentation create() {
        ProfileInfoRepresentation info = new ProfileInfoRepresentation();

        info.name = Profile.getName();
        info.disabledFeatures = new LinkedList<>();
        for (Profile.Feature f : Profile.getDisabledFeatures()) {
            info.disabledFeatures.add(f.name());
        }

        return info;
    }

    public String getName() {
        return name;
    }

    public List<String> getDisabledFeatures() {
        return disabledFeatures;
    }

}

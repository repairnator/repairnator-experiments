/*
 * Copyright 2014-2017 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.metrics.dropwizard;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

/**
 * @author Joel Takvorian
 */
public class RegexContainerTest {

    @Test
    public void shouldCheckAndCreate() throws Exception {
        Optional<RegexContainer<String>> result = RegexContainer
                .checkAndCreate("/^some[\\s]regex$/", "content");
        assertThat(result)
                .flatMap(r -> r.match("some\tregex"))
                .isPresent()
                .hasValue("content");

        assertThat(result)
                .flatMap(r -> r.match("some\tregex\tnot\tmatching"))
                .isNotPresent();
    }

    @Test
    public void shouldCheckAndNotCreate() throws Exception {
        Optional<RegexContainer<Map<String, String>>> result = RegexContainer
                .checkAndCreate("/{}won't compile/", Collections.singletonMap("k", "v"));
        assertThat(result).isEmpty();
    }
}

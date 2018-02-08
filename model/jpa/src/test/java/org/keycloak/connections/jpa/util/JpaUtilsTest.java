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

package org.keycloak.connections.jpa.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class JpaUtilsTest {

    @Test
    public void testConvertTableName() {
        Assert.assertEquals("DATABASECHANGELOG_FOO", JpaUtils.getCustomChangelogTableName("foo"));
        Assert.assertEquals("DATABASECHANGELOG_FOOBAR", JpaUtils.getCustomChangelogTableName("foo123bar"));
        Assert.assertEquals("DATABASECHANGELOG_FOO_BAR", JpaUtils.getCustomChangelogTableName("foo_bar568"));
        Assert.assertEquals("DATABASECHANGELOG_FOO_BAR_C", JpaUtils.getCustomChangelogTableName("foo-bar-c568"));
        Assert.assertEquals("DATABASECHANGELOG_EXAMPLE_EN", JpaUtils.getCustomChangelogTableName("example-entity-provider"));
    }
}

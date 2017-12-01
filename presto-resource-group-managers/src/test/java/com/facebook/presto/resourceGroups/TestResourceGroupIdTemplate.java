/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.resourceGroups;

import com.facebook.presto.spi.resourceGroups.ResourceGroupId;
import com.facebook.presto.spi.resourceGroups.SelectionContext;
import com.google.common.collect.ImmutableSet;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.assertEquals;

public class TestResourceGroupIdTemplate
{
    @Test
    public void testExpansion()
    {
        ResourceGroupIdTemplate template = new ResourceGroupIdTemplate("test.${USER}.${SOURCE}");
        ResourceGroupId expected = new ResourceGroupId(new ResourceGroupId(new ResourceGroupId("test"), "u"), "s");
        assertEquals(template.expandTemplate(new SelectionContext(true, "u", Optional.of("s"), ImmutableSet.of(), 1, Optional.empty())), expected);
        template = new ResourceGroupIdTemplate("test.${USER}");
        assertEquals(template.expandTemplate(new SelectionContext(true, "alice.smith", Optional.empty(), ImmutableSet.of(), 1, Optional.empty())), new ResourceGroupId(new ResourceGroupId("test"), "alice.smith"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalid()
    {
        new ResourceGroupIdTemplate("test.${USER}.${SOURCE}.${BROKEN}");
    }
}

/*
 * Copyright 2018 The Hekate Project
 *
 * The Hekate Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.hekate.cluster.split;

import io.hekate.HekateNodeTestBase;
import io.hekate.core.internal.HekateTestNode;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddressReachabilityDetectorTest extends HekateNodeTestBase {
    @Test
    public void testValid() throws Exception {
        HekateTestNode node = createNode().join();

        AddressReachabilityDetector detector = new AddressReachabilityDetector(node.getSocketAddress(), 2000);

        assertTrue(detector.isValid(node.localNode()));

        detector = new AddressReachabilityDetector(node.getSocketAddress().getHostString() + ':' + node.getSocketAddress().getPort());

        assertTrue(detector.isValid(node.localNode()));
    }

    @Test
    public void testInvalid() throws Exception {
        AddressReachabilityDetector detector = new AddressReachabilityDetector("localhost:12345", 2000);

        assertFalse(detector.isValid(newNode()));
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.jackrabbit.oak.segment.standby;

import static org.apache.jackrabbit.oak.segment.SegmentTestUtils.addTestContent;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.jackrabbit.oak.segment.SegmentNodeStoreBuilders;
import org.apache.jackrabbit.oak.segment.file.FileStore;
import org.apache.jackrabbit.oak.segment.standby.client.StandbyClientSync;
import org.apache.jackrabbit.oak.segment.standby.server.StandbyServerSync;
import org.apache.jackrabbit.oak.segment.test.TemporaryFileStore;
import org.apache.jackrabbit.oak.commons.junit.TemporaryPort;
import org.apache.jackrabbit.oak.spi.state.NodeStore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TemporaryFolder;

public class FailoverSslTestIT extends TestBase {

    private TemporaryFolder folder = new TemporaryFolder(new File("target"));

    private TemporaryFileStore serverFileStore = new TemporaryFileStore(folder, false);

    private TemporaryFileStore clientFileStore = new TemporaryFileStore(folder, true);

    @Rule
    public TemporaryPort serverPort = new TemporaryPort();

    @Rule
    public RuleChain chain = RuleChain.outerRule(folder)
            .around(serverFileStore)
            .around(clientFileStore);

    @Test
    public void testFailoverSecure() throws Exception {
        FileStore storeS = serverFileStore.fileStore();
        FileStore storeC = clientFileStore.fileStore();
        try (
                StandbyServerSync serverSync = new StandbyServerSync(serverPort.getPort(), storeS, 1 * MB, true);
                StandbyClientSync clientSync = newStandbyClientSync(storeC, serverPort.getPort(), true);
        ) {
            assertTrue(synchronizeAndCompareHead(serverSync, clientSync));
        }
    }

    @Test
    public void testFailoverSecureServerPlainClient() throws Exception {
        FileStore storeS = serverFileStore.fileStore();
        FileStore storeC = clientFileStore.fileStore();
        try (
                StandbyServerSync serverSync = new StandbyServerSync(serverPort.getPort(), storeS, 1 * MB, true);
                StandbyClientSync clientSync = newStandbyClientSync(storeC, serverPort.getPort());
        ) {
            assertFalse(synchronizeAndCompareHead(serverSync, clientSync));
        }
    }

    @Test
    public void testFailoverPlainServerSecureClient() throws Exception {
        FileStore storeS = serverFileStore.fileStore();
        FileStore storeC = clientFileStore.fileStore();
        try (
                StandbyServerSync serverSync = new StandbyServerSync(serverPort.getPort(), storeS, 1 * MB);
                StandbyClientSync clientSync = newStandbyClientSync(storeC, serverPort.getPort(), true);
        ) {
            assertFalse(synchronizeAndCompareHead(serverSync, clientSync));
        }
    }

    private boolean synchronizeAndCompareHead(StandbyServerSync serverSync, StandbyClientSync clientSync) throws Exception {
        FileStore storeS = serverFileStore.fileStore();
        FileStore storeC = clientFileStore.fileStore();
        NodeStore store = SegmentNodeStoreBuilders.builder(storeS).build();
        serverSync.start();
        addTestContent(store, "server");
        storeS.flush();  // this speeds up the test a little bit...
        clientSync.run();
        return storeS.getHead().equals(storeC.getHead());
    }

}

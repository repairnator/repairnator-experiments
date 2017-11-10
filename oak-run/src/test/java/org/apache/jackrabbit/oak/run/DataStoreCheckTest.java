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
package org.apache.jackrabbit.oak.run;

import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import joptsimple.internal.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.felix.cm.file.ConfigurationHandler;
import org.apache.jackrabbit.core.data.DataStore;
import org.apache.jackrabbit.oak.blob.cloud.azure.blobstorage.AzureConstants;
import org.apache.jackrabbit.oak.blob.cloud.azure.blobstorage.AzureDataStoreUtils;
import org.apache.jackrabbit.oak.blob.cloud.s3.S3Constants;
import org.apache.jackrabbit.oak.blob.cloud.s3.S3DataStoreUtils;
import org.apache.jackrabbit.oak.commons.FileIOUtils;
import org.apache.jackrabbit.oak.plugins.blob.datastore.DataStoreBlobStore;
import org.apache.jackrabbit.oak.plugins.blob.datastore.OakFileDataStore;
import org.apache.jackrabbit.oak.segment.SegmentBlob;
import org.apache.jackrabbit.oak.segment.SegmentNodeStoreBuilders;
import org.apache.jackrabbit.oak.segment.file.FileStore;
import org.apache.jackrabbit.oak.segment.file.FileStoreBuilder;
import org.apache.jackrabbit.oak.spi.commit.CommitInfo;
import org.apache.jackrabbit.oak.spi.commit.EmptyHook;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;
import org.apache.jackrabbit.oak.spi.state.NodeStore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for {@link DataStoreCheckCommand}
 */
public class DataStoreCheckTest {
    private static final Logger log = LoggerFactory.getLogger(DataStoreCheckTest.class);

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder(new File("target"));

    private String storePath;

    private Set<String> blobsAdded;

    private String cfgFilePath;

    private String dsPath;

    private DataStoreBlobStore setupDataStore;

    private String dsOption;

    private String container;

    @Before
    public void setup() throws Exception {
        if (S3DataStoreUtils.isS3Configured()) {
            Properties props = S3DataStoreUtils.getS3Config();
            props.setProperty("cacheSize", "0");
            container = props.getProperty(S3Constants.S3_BUCKET);
            DataStore ds = S3DataStoreUtils.getS3DataStore(S3DataStoreUtils.getFixtures().get(0),
                props,
                temporaryFolder.newFolder().getAbsolutePath());
            setupDataStore = new DataStoreBlobStore(ds);
            cfgFilePath = createTempConfig(temporaryFolder.newFile(), props);
            dsOption = "s3ds";
        } else if (AzureDataStoreUtils.isAzureConfigured()) {
            Properties props = AzureDataStoreUtils.getAzureConfig();
            props.setProperty("cacheSize", "0");
            container = props.getProperty(AzureConstants.AZURE_BLOB_CONTAINER_NAME);
            DataStore ds = AzureDataStoreUtils.getAzureDataStore(props,
                temporaryFolder.newFolder().getAbsolutePath());
            setupDataStore = new DataStoreBlobStore(ds);
            cfgFilePath = createTempConfig(temporaryFolder.newFile(), props);
            dsOption = "azureblobds";
        }
        else {
            OakFileDataStore delegate = new OakFileDataStore();
            dsPath = temporaryFolder.newFolder().getAbsolutePath();
            delegate.setPath(dsPath);
            delegate.init(null);
            setupDataStore = new DataStoreBlobStore(delegate);

            File cfgFile = temporaryFolder.newFile();
            Properties props = new Properties();
            props.put("path", dsPath);
            props.put("minRecordLength", new Long(4096));
            cfgFilePath = createTempConfig(cfgFile, props);
            dsOption = "fds";
        }

        File storeFile = temporaryFolder.newFolder();
        storePath = storeFile.getAbsolutePath();
        FileStore fileStore = FileStoreBuilder.fileStoreBuilder(storeFile)
                .withBlobStore(setupDataStore)
                .withMaxFileSize(256)
                .withSegmentCacheSize(64)
                .build();
        NodeStore store = SegmentNodeStoreBuilders.builder(fileStore).build();

        /* Create nodes with blobs stored in DS*/
        NodeBuilder a = store.getRoot().builder();
        int numBlobs = 10;
        blobsAdded = Sets.newHashSet();
        for (int i = 0; i < numBlobs; i++) {
            SegmentBlob b = (SegmentBlob) store.createBlob(randomStream(i, 18342));
            Iterator<String> idIter = setupDataStore.resolveChunks(b.getBlobId());
            while (idIter.hasNext()) {
                String chunk = idIter.next();
                blobsAdded.add(chunk);
            }
            a.child("c" + i).setProperty("x", b);
        }

        store.merge(a, EmptyHook.INSTANCE, CommitInfo.EMPTY);
        log.info("Created blobs : {}", blobsAdded);

        fileStore.close();
    }

    @After
    public void tearDown() {
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
        if (!Strings.isNullOrEmpty(container)) {
            try {
                if (dsOption.equals("s3ds")) {
                    S3DataStoreUtils.deleteBucket(container, new Date());
                } else {
                    AzureDataStoreUtils.deleteContainer(container);
                }
            } catch (Exception e) {
                log.error("Error in cleaning container", e);
            }
        }
    }

    @Test
    public void testCorrect() throws Exception {
        File dump = temporaryFolder.newFolder();
        setupDataStore.close();
        testAllParams(dump);
    }

    @Test
    public void testConsistency() throws Exception {
        File dump = temporaryFolder.newFolder();

        Random rand = new Random();
        String deletedBlobId = Iterables.get(blobsAdded, rand.nextInt(blobsAdded.size()));
        blobsAdded.remove(deletedBlobId);
        long count = setupDataStore.countDeleteChunks(ImmutableList.of(deletedBlobId), 0);
        assertEquals(1, count);
        setupDataStore.close();

        testAllParams(dump);

        assertFileEquals(dump, "[id]", blobsAdded);
        assertFileEquals(dump, "[ref]", Sets.union(blobsAdded, Sets.newHashSet(deletedBlobId)));
        assertFileEquals(dump, "[consistency]", Sets.newHashSet(deletedBlobId));
    }

    private void testAllParams(File dump) throws Exception {
        DataStoreCheckCommand checkCommand = new DataStoreCheckCommand();
        List<String> argsList = Lists
            .newArrayList("--id", "--ref", "--consistency", "--" + dsOption, cfgFilePath, "--store", storePath,
                "--dump", dump.getAbsolutePath());

        checkCommand.execute(argsList.toArray(new String[0]));
    }

    @Test
    public void testMissingOpParams() throws Exception {
        setupDataStore.close();
        File dump = temporaryFolder.newFolder();
        List<String> argsList = Lists
            .newArrayList("--" + dsOption, cfgFilePath, "--store", storePath,
                "--dump", dump.getAbsolutePath());
        log.info("Running testMissinOpParams: {}", argsList);
        testIncorrectParams(argsList, Lists.newArrayList("Missing required option(s)", "id", "ref", "consistency"));
    }

    @Test
    public void testTarNoDS() throws Exception {
        setupDataStore.close();
        File dump = temporaryFolder.newFolder();
        List<String> argsList = Lists
            .newArrayList("--id", "--ref", "--consistency", "--store", storePath,
                "--dump", dump.getAbsolutePath());
        testIncorrectParams(argsList, Lists.newArrayList("Operation not defined for SegmentNodeStore without external datastore"));

    }

    @Test
    public void testOpNoStore() throws Exception {
        setupDataStore.close();
        File dump = temporaryFolder.newFolder();
        List<String> argsList = Lists
            .newArrayList("--consistency", "--" + dsOption, cfgFilePath,
                "--dump", dump.getAbsolutePath());
        testIncorrectParams(argsList, Lists.newArrayList("Missing required option(s) [store]"));

        argsList = Lists
            .newArrayList("--ref", "--" + dsOption, cfgFilePath,
                "--dump", dump.getAbsolutePath());
        testIncorrectParams(argsList, Lists.newArrayList("Missing required option(s) [store]"));
    }

    public static void testIncorrectParams(List<String> argList, ArrayList<String> assertMsg) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setErr(new PrintStream(buffer, true, UTF_8.toString()));

        DataStoreCheckCommand checkCommand = new DataStoreCheckCommand();

        checkCommand.execute(argList.toArray(new String[0]));
        String message = buffer.toString(UTF_8.toString());
        log.info("Assert message: {}", assertMsg);
        log.info("Message logged in System.err: {}", message);

        for (String msg : assertMsg) {
            Assert.assertTrue(message.contains(msg));
        }
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
    }

    private static void assertFileEquals(File dump, String prefix, Set<String> blobsAdded)
        throws IOException {
        File files[] =
            FileFilterUtils.filter(FileFilterUtils.prefixFileFilter(prefix), dump.listFiles());
        Assert.assertNotNull(files);
        Assert.assertTrue(files.length == 1);
        Assert.assertTrue(files[0].exists());
        assertEquals(blobsAdded,
            FileIOUtils.readStringsAsSet(new FileInputStream(files[0]), false));
    }

    static InputStream randomStream(int seed, int size) {
        Random r = new Random(seed);
        byte[] data = new byte[size];
        r.nextBytes(data);
        return new ByteArrayInputStream(data);
    }

    private static String createTempConfig(File cfgFile, Properties props) throws IOException {
        FileOutputStream fos = FileUtils.openOutputStream(cfgFile);
        ConfigurationHandler.write(fos, props);
        return cfgFile.getAbsolutePath();
    }
}

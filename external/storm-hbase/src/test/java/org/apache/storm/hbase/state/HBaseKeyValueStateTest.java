/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.storm.hbase.state;

import com.google.common.primitives.UnsignedBytes;
import org.apache.storm.hbase.common.HBaseClient;
import org.apache.storm.state.DefaultStateSerializer;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link HBaseKeyValueState}
 */
public class HBaseKeyValueStateTest {
    private static final String COLUMN_FAMILY = "cf";
    private static final String NAMESPACE = "namespace";

    HBaseClient mockClient;
    HBaseKeyValueState<String, String> keyValueState;
    ConcurrentNavigableMap<byte[], NavigableMap<byte[], NavigableMap<byte[], byte[]>>> mockMap;

    @Before
    public void setUp() throws Exception {
        mockMap = new ConcurrentSkipListMap<>(UnsignedBytes.lexicographicalComparator());
        mockClient = HBaseClientTestUtil.mockedHBaseClient(mockMap);
        keyValueState = new HBaseKeyValueState<>(mockClient, COLUMN_FAMILY, NAMESPACE,
                new DefaultStateSerializer<String>(), new DefaultStateSerializer<String>());
    }

    @Test
    public void testPutAndGet() throws Exception {
        keyValueState.put("a", "1");
        keyValueState.put("b", "2");
        assertEquals("1", keyValueState.get("a"));
        assertEquals("2", keyValueState.get("b"));
        assertEquals(null, keyValueState.get("c"));
    }

    @Test
    public void testPutAndDelete() throws Exception {
        keyValueState.put("a", "1");
        keyValueState.put("b", "2");
        assertEquals("1", keyValueState.get("a"));
        assertEquals("2", keyValueState.get("b"));
        assertEquals(null, keyValueState.get("c"));
        assertEquals("1", keyValueState.delete("a"));
        assertEquals(null, keyValueState.get("a"));
        assertEquals("2", keyValueState.get("b"));
        assertEquals(null, keyValueState.get("c"));
    }

    @Test
    public void testPrepareCommitRollback() throws Exception {
        keyValueState.put("a", "1");
        keyValueState.put("b", "2");
        keyValueState.prepareCommit(1);
        keyValueState.put("c", "3");
        assertArrayEquals(new String[]{"1", "2", "3"}, getValues());
        keyValueState.rollback();
        assertArrayEquals(new String[]{null, null, null}, getValues());
        keyValueState.put("a", "1");
        keyValueState.put("b", "2");
        keyValueState.prepareCommit(1);
        keyValueState.commit(1);
        keyValueState.put("c", "3");
        assertArrayEquals(new String[]{"1", "2", "3"}, getValues());
        keyValueState.rollback();
        assertArrayEquals(new String[]{"1", "2", null}, getValues());
        keyValueState.put("c", "3");
        assertEquals("2", keyValueState.delete("b"));
        assertEquals("3", keyValueState.delete("c"));
        assertArrayEquals(new String[]{"1", null, null}, getValues());
        keyValueState.prepareCommit(2);
        assertArrayEquals(new String[]{"1", null, null}, getValues());
        keyValueState.commit(2);
        assertArrayEquals(new String[]{"1", null, null}, getValues());
        keyValueState.put("b", "2");
        keyValueState.prepareCommit(3);
        keyValueState.put("c", "3");
        assertArrayEquals(new String[]{"1", "2", "3"}, getValues());
        keyValueState.rollback();
        assertArrayEquals(new String[]{"1", null, null}, getValues());
    }

    private String[] getValues() {
        return new String[]{
                keyValueState.get("a"),
                keyValueState.get("b"),
                keyValueState.get("c")
        };
    }
}
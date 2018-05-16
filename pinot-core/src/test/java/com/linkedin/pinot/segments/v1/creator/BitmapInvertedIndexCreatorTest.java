/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.segments.v1.creator;

import com.linkedin.pinot.common.data.DimensionFieldSpec;
import com.linkedin.pinot.common.data.FieldSpec.DataType;
import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.segment.creator.impl.V1Constants;
import com.linkedin.pinot.core.segment.creator.impl.inv.OffHeapBitmapInvertedIndexCreator;
import com.linkedin.pinot.core.segment.creator.impl.inv.OnHeapBitmapInvertedIndexCreator;
import com.linkedin.pinot.core.segment.index.readers.BitmapInvertedIndexReader;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.roaringbitmap.IntIterator;
import org.roaringbitmap.buffer.ImmutableRoaringBitmap;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class BitmapInvertedIndexCreatorTest {
  private static final File TEMP_DIR = FileUtils.getTempDirectory();
  private static final File ON_HEAP_INDEX_DIR = new File(TEMP_DIR, "onHeap");
  private static final File OFF_HEAP_INDEX_DIR = new File(TEMP_DIR, "offHeap");
  private static final String COLUMN_NAME = "testColumn";
  private static final File ON_HEAP_INVERTED_INDEX =
      new File(ON_HEAP_INDEX_DIR, COLUMN_NAME + V1Constants.Indexes.BITMAP_INVERTED_INDEX_FILE_EXTENSION);
  private static final File OFF_HEAP_INVERTED_INDEX =
      new File(OFF_HEAP_INDEX_DIR, COLUMN_NAME + V1Constants.Indexes.BITMAP_INVERTED_INDEX_FILE_EXTENSION);
  private static final int CARDINALITY = 10;
  private static final int NUM_DOCS = 100;
  private static final int MAX_NUM_MULTI_VALUES = 10;
  private static final Random RANDOM = new Random();

  @BeforeMethod
  public void setUp() throws IOException {
    FileUtils.forceMkdir(ON_HEAP_INDEX_DIR);
    FileUtils.forceMkdir(OFF_HEAP_INDEX_DIR);
  }

  @Test
  public void testSingleValue() throws IOException {
    int[] dictIds = new int[NUM_DOCS];
    @SuppressWarnings("unchecked")
    Set<Integer>[] postingLists = new Set[CARDINALITY];

    // Generate random dictionary ids
    for (int dictId = 0; dictId < CARDINALITY; dictId++) {
      postingLists[dictId] = new HashSet<>();
    }
    for (int docId = 0; docId < NUM_DOCS; docId++) {
      int dictId = RANDOM.nextInt(CARDINALITY);
      dictIds[docId] = dictId;
      postingLists[dictId].add(docId);
    }

    // Generate inverted index using OnHeapBitmapInvertedIndexCreator
    try (OnHeapBitmapInvertedIndexCreator onHeapCreator = new OnHeapBitmapInvertedIndexCreator(ON_HEAP_INDEX_DIR,
        COLUMN_NAME, CARDINALITY)) {
      for (int docId = 0; docId < NUM_DOCS; docId++) {
        onHeapCreator.addSV(docId, dictIds[docId]);
      }
      onHeapCreator.seal();
    }

    // Generate inverted index using OffHeapBitmapInvertedIndexCreator
    try (OffHeapBitmapInvertedIndexCreator offHeapCreator = new OffHeapBitmapInvertedIndexCreator(OFF_HEAP_INDEX_DIR,
        new DimensionFieldSpec(COLUMN_NAME, DataType.INT, true), CARDINALITY, NUM_DOCS, 0)) {
      for (int docId = 0; docId < NUM_DOCS; docId++) {
        offHeapCreator.addSV(docId, dictIds[docId]);
      }
      offHeapCreator.seal();
    }

    // Validate on-heap inverted index, and check whether two file are the same
    validate(ON_HEAP_INVERTED_INDEX, postingLists);
    Assert.assertTrue(FileUtils.contentEquals(ON_HEAP_INVERTED_INDEX, OFF_HEAP_INVERTED_INDEX));
  }

  @Test
  public void testMultiValue() throws IOException {
    int[][] dictIds = new int[NUM_DOCS][];
    int numValues = 0;
    @SuppressWarnings("unchecked")
    Set<Integer>[] postingLists = new Set[CARDINALITY];

    // Generate random dictionary ids
    for (int dictId = 0; dictId < CARDINALITY; dictId++) {
      postingLists[dictId] = new HashSet<>();
    }
    for (int docId = 0; docId < NUM_DOCS; docId++) {
      int numValuesForDoc = RANDOM.nextInt(MAX_NUM_MULTI_VALUES) + 1;
      dictIds[docId] = new int[numValuesForDoc];
      numValues += numValuesForDoc;
      for (int i = 0; i < numValuesForDoc; i++) {
        int dictId = RANDOM.nextInt(CARDINALITY);
        dictIds[docId][i] = dictId;
        postingLists[dictId].add(docId);
      }
    }

    // Generate inverted index using OnHeapBitmapInvertedIndexCreator
    try (OnHeapBitmapInvertedIndexCreator onHeapCreator = new OnHeapBitmapInvertedIndexCreator(ON_HEAP_INDEX_DIR,
        COLUMN_NAME, CARDINALITY)) {
      for (int docId = 0; docId < NUM_DOCS; docId++) {
        onHeapCreator.addMV(docId, dictIds[docId]);
      }
      onHeapCreator.seal();
    }

    // Generate inverted index using OffHeapBitmapInvertedIndexCreator
    try (OffHeapBitmapInvertedIndexCreator offHeapCreator = new OffHeapBitmapInvertedIndexCreator(OFF_HEAP_INDEX_DIR,
        new DimensionFieldSpec(COLUMN_NAME, DataType.INT, false), CARDINALITY, NUM_DOCS, numValues)) {
      for (int docId = 0; docId < NUM_DOCS; docId++) {
        offHeapCreator.addMV(docId, dictIds[docId]);
      }
      offHeapCreator.seal();
    }

    // Validate on-heap inverted index, and check whether two file are the same
    validate(ON_HEAP_INVERTED_INDEX, postingLists);
    Assert.assertTrue(FileUtils.contentEquals(ON_HEAP_INVERTED_INDEX, OFF_HEAP_INVERTED_INDEX));
  }

  private void validate(File invertedIndex, Set<Integer>[] postingLists) throws IOException {
    try (PinotDataBuffer dataBuffer = PinotDataBuffer.fromFile(invertedIndex, ReadMode.mmap,
        FileChannel.MapMode.READ_ONLY, "BitmapInvertedIndexCreatorTest")) {
      BitmapInvertedIndexReader reader = new BitmapInvertedIndexReader(dataBuffer, CARDINALITY);
      for (int dictId = 0; dictId < CARDINALITY; dictId++) {
        ImmutableRoaringBitmap bitmap = reader.getDocIds(dictId);
        Set<Integer> expected = postingLists[dictId];
        Assert.assertEquals(bitmap.getCardinality(), expected.size());
        IntIterator intIterator = bitmap.getIntIterator();
        while (intIterator.hasNext()) {
          Assert.assertTrue(expected.contains(intIterator.next()));
        }
      }
    }
  }

  @AfterMethod
  public void tearDown() throws IOException {
    FileUtils.deleteDirectory(ON_HEAP_INDEX_DIR);
    FileUtils.deleteDirectory(OFF_HEAP_INDEX_DIR);
  }
}

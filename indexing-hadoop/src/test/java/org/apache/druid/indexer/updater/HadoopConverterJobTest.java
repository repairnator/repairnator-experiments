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

package org.apache.druid.indexer.updater;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.apache.druid.client.ImmutableDruidDataSource;
import org.apache.druid.data.input.impl.DelimitedParseSpec;
import org.apache.druid.data.input.impl.DimensionsSpec;
import org.apache.druid.data.input.impl.StringInputRowParser;
import org.apache.druid.data.input.impl.TimestampSpec;
import org.apache.druid.indexer.HadoopDruidDetermineConfigurationJob;
import org.apache.druid.indexer.HadoopDruidIndexerConfig;
import org.apache.druid.indexer.HadoopDruidIndexerJob;
import org.apache.druid.indexer.HadoopIOConfig;
import org.apache.druid.indexer.HadoopIngestionSpec;
import org.apache.druid.indexer.HadoopTuningConfig;
import org.apache.druid.indexer.JobHelper;
import org.apache.druid.indexer.Jobby;
import org.apache.druid.indexer.SQLMetadataStorageUpdaterJobHandler;
import org.apache.druid.java.util.common.FileUtils;
import org.apache.druid.java.util.common.Intervals;
import org.apache.druid.java.util.common.granularity.Granularities;
import org.apache.druid.metadata.MetadataSegmentManagerConfig;
import org.apache.druid.metadata.MetadataStorageConnectorConfig;
import org.apache.druid.metadata.MetadataStorageTablesConfig;
import org.apache.druid.metadata.SQLMetadataSegmentManager;
import org.apache.druid.metadata.TestDerbyConnector;
import org.apache.druid.metadata.storage.derby.DerbyConnector;
import org.apache.druid.query.Query;
import org.apache.druid.query.aggregation.AggregatorFactory;
import org.apache.druid.query.aggregation.DoubleSumAggregatorFactory;
import org.apache.druid.query.aggregation.hyperloglog.HyperUniquesAggregatorFactory;
import org.apache.druid.segment.IndexSpec;
import org.apache.druid.segment.TestIndex;
import org.apache.druid.segment.data.CompressionFactory;
import org.apache.druid.segment.data.CompressionStrategy;
import org.apache.druid.segment.data.RoaringBitmapSerdeFactory;
import org.apache.druid.segment.indexing.DataSchema;
import org.apache.druid.segment.indexing.granularity.UniformGranularitySpec;
import org.apache.druid.timeline.DataSegment;
import org.joda.time.Interval;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;
import org.skife.jdbi.v2.tweak.HandleCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HadoopConverterJobTest
{
  @Rule
  public final TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Rule
  public final TestDerbyConnector.DerbyConnectorRule derbyConnectorRule = new TestDerbyConnector.DerbyConnectorRule();
  private String storageLocProperty = null;
  private File tmpSegmentDir = null;

  private static final String DATASOURCE = "testDatasource";
  private static final String STORAGE_PROPERTY_KEY = "druid.storage.storageDirectory";

  private Supplier<MetadataStorageTablesConfig> metadataStorageTablesConfigSupplier;
  private DerbyConnector connector;

  private final Interval interval = Intervals.of("2011-01-01T00:00:00.000Z/2011-05-01T00:00:00.000Z");

  @After
  public void tearDown()
  {
    if (storageLocProperty == null) {
      System.clearProperty(STORAGE_PROPERTY_KEY);
    } else {
      System.setProperty(STORAGE_PROPERTY_KEY, storageLocProperty);
    }
    tmpSegmentDir = null;
  }

  @Before
  public void setUp() throws Exception
  {
    final MetadataStorageUpdaterJobSpec metadataStorageUpdaterJobSpec = new MetadataStorageUpdaterJobSpec()
    {
      @Override
      public String getSegmentTable()
      {
        return derbyConnectorRule.metadataTablesConfigSupplier().get().getSegmentsTable();
      }

      @Override
      public MetadataStorageConnectorConfig get()
      {
        return derbyConnectorRule.getMetadataConnectorConfig();
      }
    };
    final File scratchFileDir = temporaryFolder.newFolder();
    storageLocProperty = System.getProperty(STORAGE_PROPERTY_KEY);
    tmpSegmentDir = temporaryFolder.newFolder();
    System.setProperty(STORAGE_PROPERTY_KEY, tmpSegmentDir.getAbsolutePath());

    final URL url = Preconditions.checkNotNull(Query.class.getClassLoader().getResource("druid.sample.tsv"));
    final File tmpInputFile = temporaryFolder.newFile();
    FileUtils.retryCopy(
        new ByteSource()
        {
          @Override
          public InputStream openStream() throws IOException
          {
            return url.openStream();
          }
        },
        tmpInputFile,
        FileUtils.IS_EXCEPTION,
        3
    );
    final HadoopDruidIndexerConfig hadoopDruidIndexerConfig = new HadoopDruidIndexerConfig(
        new HadoopIngestionSpec(
            new DataSchema(
                DATASOURCE,
                HadoopDruidIndexerConfig.JSON_MAPPER.convertValue(
                    new StringInputRowParser(
                        new DelimitedParseSpec(
                            new TimestampSpec("ts", "iso", null),
                            new DimensionsSpec(DimensionsSpec.getDefaultSchemas(Arrays.asList(TestIndex.DIMENSIONS)), null, null),
                            "\t",
                            "\u0001",
                            Arrays.asList(TestIndex.COLUMNS),
                            false,
                            0
                        ),
                        null
                    ),
                    Map.class
                ),
                new AggregatorFactory[]{
                    new DoubleSumAggregatorFactory(TestIndex.DOUBLE_METRICS[0], TestIndex.DOUBLE_METRICS[0]),
                    new HyperUniquesAggregatorFactory("quality_uniques", "quality")
                },
                new UniformGranularitySpec(
                    Granularities.MONTH,
                    Granularities.DAY,
                    ImmutableList.of(interval)
                ),
                null,
                HadoopDruidIndexerConfig.JSON_MAPPER
            ),
            new HadoopIOConfig(
                ImmutableMap.of(
                    "type", "static",
                    "paths", tmpInputFile.getAbsolutePath()
                ),
                metadataStorageUpdaterJobSpec,
                tmpSegmentDir.getAbsolutePath()
            ),
            new HadoopTuningConfig(
                scratchFileDir.getAbsolutePath(),
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                null,
                false,
                false,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null
            )
        )
    );
    metadataStorageTablesConfigSupplier = derbyConnectorRule.metadataTablesConfigSupplier();
    connector = derbyConnectorRule.getConnector();

    try {
      connector.getDBI().withHandle(
          new HandleCallback<Void>()
          {
            @Override
            public Void withHandle(Handle handle)
            {
              handle.execute("DROP TABLE druid_segments");
              return null;
            }
          }
      );
    }
    catch (CallbackFailedException e) {
      // Who cares
    }
    List<Jobby> jobs = ImmutableList.of(
        new Jobby()
        {
          @Override
          public boolean run()
          {
            connector.createSegmentTable(metadataStorageUpdaterJobSpec.getSegmentTable());
            return true;
          }
        },
        new HadoopDruidDetermineConfigurationJob(hadoopDruidIndexerConfig),
        new HadoopDruidIndexerJob(
            hadoopDruidIndexerConfig,
            new SQLMetadataStorageUpdaterJobHandler(connector)
        )
    );
    Assert.assertTrue(JobHelper.runJobs(jobs, hadoopDruidIndexerConfig));
  }

  private List<DataSegment> getDataSegments(
      SQLMetadataSegmentManager manager
  ) throws InterruptedException
  {
    manager.start();
    while (!manager.isStarted()) {
      Thread.sleep(10);
    }
    manager.poll();
    final ImmutableDruidDataSource druidDataSource = manager.getInventoryValue(DATASOURCE);
    manager.stop();
    return Lists.newArrayList(druidDataSource.getSegments());
  }

  @Test
  public void testSimpleJob() throws IOException, InterruptedException
  {

    final SQLMetadataSegmentManager manager = new SQLMetadataSegmentManager(
        HadoopDruidConverterConfig.jsonMapper,
        new Supplier<MetadataSegmentManagerConfig>()
        {
          @Override
          public MetadataSegmentManagerConfig get()
          {
            return new MetadataSegmentManagerConfig();
          }
        },
        metadataStorageTablesConfigSupplier,
        connector
    );

    final List<DataSegment> oldSemgments = getDataSegments(manager);
    final File tmpDir = temporaryFolder.newFolder();
    final HadoopConverterJob converterJob = new HadoopConverterJob(
        new HadoopDruidConverterConfig(
            DATASOURCE,
            interval,
            new IndexSpec(new RoaringBitmapSerdeFactory(null),
                          CompressionStrategy.UNCOMPRESSED,
                          CompressionStrategy.UNCOMPRESSED,
                          CompressionFactory.LongEncodingStrategy.LONGS),
            oldSemgments,
            true,
            tmpDir.toURI(),
            ImmutableMap.of(),
            null,
            tmpSegmentDir.toURI().toString()
        )
    );

    final List<DataSegment> segments = Lists.newArrayList(converterJob.run());
    Assert.assertNotNull("bad result", segments);
    Assert.assertEquals("wrong segment count", 4, segments.size());
    Assert.assertTrue(converterJob.getLoadedBytes() > 0);
    Assert.assertTrue(converterJob.getWrittenBytes() > 0);
    Assert.assertTrue(converterJob.getWrittenBytes() > converterJob.getLoadedBytes());

    Assert.assertEquals(oldSemgments.size(), segments.size());

    final DataSegment segment = segments.get(0);
    Assert.assertTrue(interval.contains(segment.getInterval()));
    Assert.assertTrue(segment.getVersion().endsWith("_converted"));
    Assert.assertTrue(segment.getLoadSpec().get("path").toString().contains("_converted"));

    for (File file : tmpDir.listFiles()) {
      Assert.assertFalse(file.isDirectory());
      Assert.assertTrue(file.isFile());
    }


    final Comparator<DataSegment> segmentComparator = new Comparator<DataSegment>()
    {
      @Override
      public int compare(DataSegment o1, DataSegment o2)
      {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
      }
    };
    Collections.sort(
        oldSemgments,
        segmentComparator
    );
    Collections.sort(
        segments,
        segmentComparator
    );

    for (int i = 0; i < oldSemgments.size(); ++i) {
      final DataSegment oldSegment = oldSemgments.get(i);
      final DataSegment newSegment = segments.get(i);
      Assert.assertEquals(oldSegment.getDataSource(), newSegment.getDataSource());
      Assert.assertEquals(oldSegment.getInterval(), newSegment.getInterval());
      Assert.assertEquals(
          Sets.newHashSet(oldSegment.getMetrics()),
          Sets.newHashSet(newSegment.getMetrics())
      );
      Assert.assertEquals(
          Sets.newHashSet(oldSegment.getDimensions()),
          Sets.newHashSet(newSegment.getDimensions())
      );
      Assert.assertEquals(oldSegment.getVersion() + "_converted", newSegment.getVersion());
      Assert.assertTrue(oldSegment.getSize() < newSegment.getSize());
      Assert.assertEquals(oldSegment.getBinaryVersion(), newSegment.getBinaryVersion());
    }
  }

  private static void corrupt(
      DataSegment segment
  ) throws IOException
  {
    final Map<String, Object> localLoadSpec = segment.getLoadSpec();
    final Path segmentPath = Paths.get(localLoadSpec.get("path").toString());

    final MappedByteBuffer buffer = Files.map(segmentPath.toFile(), FileChannel.MapMode.READ_WRITE);
    while (buffer.hasRemaining()) {
      buffer.put((byte) 0xFF);
    }
  }

  @Test
  @Ignore // This takes a long time due to retries
  public void testHadoopFailure() throws IOException, InterruptedException
  {
    final SQLMetadataSegmentManager manager = new SQLMetadataSegmentManager(
        HadoopDruidConverterConfig.jsonMapper,
        new Supplier<MetadataSegmentManagerConfig>()
        {
          @Override
          public MetadataSegmentManagerConfig get()
          {
            return new MetadataSegmentManagerConfig();
          }
        },
        metadataStorageTablesConfigSupplier,
        connector
    );

    final List<DataSegment> oldSemgments = getDataSegments(manager);
    final File tmpDir = temporaryFolder.newFolder();
    final HadoopConverterJob converterJob = new HadoopConverterJob(
        new HadoopDruidConverterConfig(
            DATASOURCE,
            interval,
            new IndexSpec(new RoaringBitmapSerdeFactory(null),
                          CompressionStrategy.UNCOMPRESSED,
                          CompressionStrategy.UNCOMPRESSED,
                          CompressionFactory.LongEncodingStrategy.LONGS),
            oldSemgments,
            true,
            tmpDir.toURI(),
            ImmutableMap.of(),
            null,
            tmpSegmentDir.toURI().toString()
        )
    );

    corrupt(oldSemgments.get(0));

    final List<DataSegment> result = converterJob.run();
    Assert.assertNull("result should be null", result);

    final List<DataSegment> segments = getDataSegments(manager);

    Assert.assertEquals(oldSemgments.size(), segments.size());

    Assert.assertEquals(oldSemgments, segments);
  }
}

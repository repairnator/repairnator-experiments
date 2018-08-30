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

package org.apache.druid.indexing.common.task;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import org.apache.druid.data.input.impl.DimensionSchema;
import org.apache.druid.data.input.impl.DimensionSchema.MultiValueHandling;
import org.apache.druid.data.input.impl.DimensionsSpec;
import org.apache.druid.data.input.impl.DoubleDimensionSchema;
import org.apache.druid.data.input.impl.FloatDimensionSchema;
import org.apache.druid.data.input.impl.InputRowParser;
import org.apache.druid.data.input.impl.LongDimensionSchema;
import org.apache.druid.data.input.impl.NoopInputRowParser;
import org.apache.druid.data.input.impl.StringDimensionSchema;
import org.apache.druid.data.input.impl.TimeAndDimsParseSpec;
import org.apache.druid.indexer.TaskStatus;
import org.apache.druid.indexing.common.TaskToolbox;
import org.apache.druid.indexing.common.actions.SegmentListUsedAction;
import org.apache.druid.indexing.common.actions.TaskActionClient;
import org.apache.druid.indexing.common.stats.RowIngestionMetersFactory;
import org.apache.druid.indexing.common.task.IndexTask.IndexIOConfig;
import org.apache.druid.indexing.common.task.IndexTask.IndexIngestionSpec;
import org.apache.druid.indexing.common.task.IndexTask.IndexTuningConfig;
import org.apache.druid.indexing.firehose.IngestSegmentFirehoseFactory;
import org.apache.druid.java.util.common.IAE;
import org.apache.druid.java.util.common.ISE;
import org.apache.druid.java.util.common.JodaUtils;
import org.apache.druid.java.util.common.Pair;
import org.apache.druid.java.util.common.RE;
import org.apache.druid.java.util.common.granularity.Granularities;
import org.apache.druid.java.util.common.guava.Comparators;
import org.apache.druid.java.util.common.jackson.JacksonUtils;
import org.apache.druid.java.util.common.logger.Logger;
import org.apache.druid.query.aggregation.AggregatorFactory;
import org.apache.druid.segment.DimensionHandler;
import org.apache.druid.segment.IndexIO;
import org.apache.druid.segment.QueryableIndex;
import org.apache.druid.segment.column.Column;
import org.apache.druid.segment.column.ValueType;
import org.apache.druid.segment.indexing.DataSchema;
import org.apache.druid.segment.indexing.granularity.ArbitraryGranularitySpec;
import org.apache.druid.segment.indexing.granularity.GranularitySpec;
import org.apache.druid.segment.loading.SegmentLoadingException;
import org.apache.druid.segment.realtime.firehose.ChatHandlerProvider;
import org.apache.druid.server.security.AuthorizerMapper;
import org.apache.druid.timeline.DataSegment;
import org.apache.druid.timeline.TimelineObjectHolder;
import org.apache.druid.timeline.VersionedIntervalTimeline;
import org.apache.druid.timeline.partition.PartitionChunk;
import org.apache.druid.timeline.partition.PartitionHolder;
import org.joda.time.Interval;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompactionTask extends AbstractTask
{
  private static final Logger log = new Logger(CompactionTask.class);
  private static final String TYPE = "compact";
  private static final boolean DEFAULT_KEEP_SEGMENT_GRANULARITY = true;

  private final Interval interval;
  private final List<DataSegment> segments;
  private final DimensionsSpec dimensionsSpec;
  private final boolean keepSegmentGranularity;
  private final IndexTuningConfig tuningConfig;
  private final ObjectMapper jsonMapper;
  @JsonIgnore
  private final SegmentProvider segmentProvider;

  @JsonIgnore
  private List<IndexTask> indexTaskSpecs;

  @JsonIgnore
  private final AuthorizerMapper authorizerMapper;

  @JsonIgnore
  private final ChatHandlerProvider chatHandlerProvider;

  @JsonIgnore
  private final RowIngestionMetersFactory rowIngestionMetersFactory;

  @JsonCreator
  public CompactionTask(
      @JsonProperty("id") final String id,
      @JsonProperty("resource") final TaskResource taskResource,
      @JsonProperty("dataSource") final String dataSource,
      @Nullable @JsonProperty("interval") final Interval interval,
      @Nullable @JsonProperty("segments") final List<DataSegment> segments,
      @Nullable @JsonProperty("dimensions") final DimensionsSpec dimensionsSpec,
      @Nullable @JsonProperty("keepSegmentGranularity") final Boolean keepSegmentGranularity,
      @Nullable @JsonProperty("tuningConfig") final IndexTuningConfig tuningConfig,
      @Nullable @JsonProperty("context") final Map<String, Object> context,
      @JacksonInject ObjectMapper jsonMapper,
      @JacksonInject AuthorizerMapper authorizerMapper,
      @JacksonInject ChatHandlerProvider chatHandlerProvider,
      @JacksonInject RowIngestionMetersFactory rowIngestionMetersFactory
  )
  {
    super(getOrMakeId(id, TYPE, dataSource), null, taskResource, dataSource, context);
    Preconditions.checkArgument(interval != null || segments != null, "interval or segments should be specified");
    Preconditions.checkArgument(interval == null || segments == null, "one of interval and segments should be null");

    if (interval != null && interval.toDurationMillis() == 0) {
      throw new IAE("Interval[%s] is empty, must specify a nonempty interval", interval);
    }

    this.interval = interval;
    this.segments = segments;
    this.dimensionsSpec = dimensionsSpec;
    this.keepSegmentGranularity = keepSegmentGranularity == null
                                  ? DEFAULT_KEEP_SEGMENT_GRANULARITY
                                  : keepSegmentGranularity;
    this.tuningConfig = tuningConfig;
    this.jsonMapper = jsonMapper;
    this.segmentProvider = segments == null ? new SegmentProvider(dataSource, interval) : new SegmentProvider(segments);
    this.authorizerMapper = authorizerMapper;
    this.chatHandlerProvider = chatHandlerProvider;
    this.rowIngestionMetersFactory = rowIngestionMetersFactory;
  }

  @JsonProperty
  public Interval getInterval()
  {
    return interval;
  }

  @JsonProperty
  public List<DataSegment> getSegments()
  {
    return segments;
  }

  @JsonProperty
  public DimensionsSpec getDimensionsSpec()
  {
    return dimensionsSpec;
  }

  @JsonProperty
  public boolean isKeepSegmentGranularity()
  {
    return keepSegmentGranularity;
  }

  @JsonProperty
  public IndexTuningConfig getTuningConfig()
  {
    return tuningConfig;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  @Override
  public int getPriority()
  {
    return getContextValue(Tasks.PRIORITY_KEY, Tasks.DEFAULT_MERGE_TASK_PRIORITY);
  }

  @VisibleForTesting
  SegmentProvider getSegmentProvider()
  {
    return segmentProvider;
  }

  @Override
  public boolean isReady(TaskActionClient taskActionClient) throws Exception
  {
    final SortedSet<Interval> intervals = new TreeSet<>(Comparators.intervalsByStartThenEnd());
    intervals.add(segmentProvider.interval);
    return IndexTask.isReady(taskActionClient, intervals);
  }

  @Override
  public TaskStatus run(final TaskToolbox toolbox) throws Exception
  {
    if (indexTaskSpecs == null) {
      indexTaskSpecs = createIngestionSchema(
          toolbox,
          segmentProvider,
          dimensionsSpec,
          keepSegmentGranularity,
          tuningConfig,
          jsonMapper
      ).stream()
      .map(spec -> new IndexTask(
          getId(),
          getGroupId(),
          getTaskResource(),
          getDataSource(),
          spec,
          getContext(),
          authorizerMapper,
          chatHandlerProvider,
          rowIngestionMetersFactory
      ))
      .collect(Collectors.toList());
    }

    if (indexTaskSpecs.isEmpty()) {
      log.warn("Interval[%s] has no segments, nothing to do.", interval);
      return TaskStatus.failure(getId());
    } else {
      log.info("Generated [%d] compaction task specs", indexTaskSpecs.size());

      for (IndexTask eachSpec : indexTaskSpecs) {
        final String json = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(eachSpec);
        log.info("Running indexSpec: " + json);

        try {
          final TaskStatus eachResult = eachSpec.run(toolbox);
          if (!eachResult.isSuccess()) {
            log.warn("Failed to run indexSpec: [%s].\nTrying the next indexSpec.", json);
          }
        }
        catch (Exception e) {
          log.warn(e, "Failed to run indexSpec: [%s].\nTrying the next indexSpec.", json);
        }
      }

      return TaskStatus.success(getId());
    }
  }

  /**
   * Generate {@link IndexIngestionSpec} from input segments.
   *
   * @return an empty list if input segments don't exist. Otherwise, a generated ingestionSpec.
   */
  @VisibleForTesting
  static List<IndexIngestionSpec> createIngestionSchema(
      TaskToolbox toolbox,
      SegmentProvider segmentProvider,
      DimensionsSpec dimensionsSpec,
      boolean keepSegmentGranularity,
      IndexTuningConfig tuningConfig,
      ObjectMapper jsonMapper
  ) throws IOException, SegmentLoadingException
  {
    Pair<Map<DataSegment, File>, List<TimelineObjectHolder<String, DataSegment>>> pair = prepareSegments(
        toolbox,
        segmentProvider
    );
    final Map<DataSegment, File> segmentFileMap = pair.lhs;
    final List<TimelineObjectHolder<String, DataSegment>> timelineSegments = pair.rhs;

    if (timelineSegments.size() == 0) {
      return Collections.emptyList();
    }

    if (keepSegmentGranularity) {
      // if keepSegmentGranularity = true, create indexIngestionSpec per segment interval, so that we can run an index
      // task per segment interval.
      final List<IndexIngestionSpec> specs = new ArrayList<>(timelineSegments.size());
      for (TimelineObjectHolder<String, DataSegment> holder : timelineSegments) {
        final DataSchema dataSchema = createDataSchema(
            segmentProvider.dataSource,
            holder.getInterval(),
            Collections.singletonList(holder),
            dimensionsSpec,
            toolbox.getIndexIO(),
            jsonMapper,
            segmentFileMap
        );

        specs.add(
            new IndexIngestionSpec(
                dataSchema,
                createIoConfig(toolbox, dataSchema, holder.getInterval()),
                tuningConfig
            )
        );
      }

      return specs;
    } else {
      final DataSchema dataSchema = createDataSchema(
          segmentProvider.dataSource,
          segmentProvider.interval,
          timelineSegments,
          dimensionsSpec,
          toolbox.getIndexIO(),
          jsonMapper,
          segmentFileMap
      );

      return Collections.singletonList(
          new IndexIngestionSpec(
              dataSchema,
              createIoConfig(toolbox, dataSchema, segmentProvider.interval),
              tuningConfig
          )
      );
    }
  }

  private static IndexIOConfig createIoConfig(TaskToolbox toolbox, DataSchema dataSchema, Interval interval)
  {
    return new IndexIOConfig(
        new IngestSegmentFirehoseFactory(
            dataSchema.getDataSource(),
            interval,
            null, // no filter
            // set dimensions and metrics names to make sure that the generated dataSchema is used for the firehose
            dataSchema.getParser().getParseSpec().getDimensionsSpec().getDimensionNames(),
            Arrays.stream(dataSchema.getAggregators()).map(AggregatorFactory::getName).collect(Collectors.toList()),
            toolbox.getIndexIO()
        ),
        false
    );
  }

  private static Pair<Map<DataSegment, File>, List<TimelineObjectHolder<String, DataSegment>>> prepareSegments(
      TaskToolbox toolbox,
      SegmentProvider segmentProvider
  ) throws IOException, SegmentLoadingException
  {
    final List<DataSegment> usedSegments = segmentProvider.checkAndGetSegments(toolbox);
    final Map<DataSegment, File> segmentFileMap = toolbox.fetchSegments(usedSegments);
    final List<TimelineObjectHolder<String, DataSegment>> timelineSegments = VersionedIntervalTimeline
        .forSegments(usedSegments)
        .lookup(segmentProvider.interval);
    return Pair.of(segmentFileMap, timelineSegments);
  }

  private static DataSchema createDataSchema(
      String dataSource,
      Interval totalInterval,
      List<TimelineObjectHolder<String, DataSegment>> timelineObjectHolder,
      DimensionsSpec dimensionsSpec,
      IndexIO indexIO,
      ObjectMapper jsonMapper,
      Map<DataSegment, File> segmentFileMap
  )
      throws IOException
  {
    // find metadata for interval
    final List<Pair<QueryableIndex, DataSegment>> queryableIndexAndSegments = loadSegments(
        timelineObjectHolder,
        segmentFileMap,
        indexIO
    );

    // find merged aggregators
    for (Pair<QueryableIndex, DataSegment> pair : queryableIndexAndSegments) {
      final QueryableIndex index = pair.lhs;
      if (index.getMetadata() == null) {
        throw new RE("Index metadata doesn't exist for segment[%s]", pair.rhs.getIdentifier());
      }
    }
    final List<AggregatorFactory[]> aggregatorFactories = queryableIndexAndSegments
        .stream()
        .map(pair -> pair.lhs.getMetadata().getAggregators()) // We have already done null check on index.getMetadata()
        .collect(Collectors.toList());
    final AggregatorFactory[] mergedAggregators = AggregatorFactory.mergeAggregators(aggregatorFactories);

    if (mergedAggregators == null) {
      throw new ISE("Failed to merge aggregators[%s]", aggregatorFactories);
    }

    // find granularity spec
    // set rollup only if rollup is set for all segments
    final boolean rollup = queryableIndexAndSegments.stream().allMatch(pair -> {
      // We have already checked getMetadata() doesn't return null
      final Boolean isRollup = pair.lhs.getMetadata().isRollup();
      return isRollup != null && isRollup;
    });

    final GranularitySpec granularitySpec = new ArbitraryGranularitySpec(
        Granularities.NONE,
        rollup,
        Collections.singletonList(totalInterval)
    );

    // find unique dimensions
    final DimensionsSpec finalDimensionsSpec = dimensionsSpec == null ?
                                               createDimensionsSpec(queryableIndexAndSegments) :
                                               dimensionsSpec;
    final InputRowParser parser = new NoopInputRowParser(new TimeAndDimsParseSpec(null, finalDimensionsSpec));

    return new DataSchema(
        dataSource,
        jsonMapper.convertValue(parser, JacksonUtils.TYPE_REFERENCE_MAP_STRING_OBJECT),
        mergedAggregators,
        granularitySpec,
        null,
        jsonMapper
    );
  }

  private static DimensionsSpec createDimensionsSpec(List<Pair<QueryableIndex, DataSegment>> queryableIndices)
  {
    final BiMap<String, Integer> uniqueDims = HashBiMap.create();
    final Map<String, DimensionSchema> dimensionSchemaMap = new HashMap<>();

    // Here, we try to retain the order of dimensions as they were specified since the order of dimensions may be
    // optimized for performance.
    // Dimensions are extracted from the recent segments to olders because recent segments are likely to be queried more
    // frequently, and thus the performance should be optimized for recent ones rather than old ones.

    // timelineSegments are sorted in order of interval, but we do a sanity check here.
    final Comparator<Interval> intervalComparator = Comparators.intervalsByStartThenEnd();
    for (int i = 0; i < queryableIndices.size() - 1; i++) {
      final Interval shouldBeSmaller = queryableIndices.get(i).lhs.getDataInterval();
      final Interval shouldBeLarger = queryableIndices.get(i + 1).lhs.getDataInterval();
      Preconditions.checkState(
          intervalComparator.compare(shouldBeSmaller, shouldBeLarger) <= 0,
          "QueryableIndexes are not sorted! Interval[%s] of segment[%s] is laster than interval[%s] of segment[%s]",
          shouldBeSmaller,
          queryableIndices.get(i).rhs.getIdentifier(),
          shouldBeLarger,
          queryableIndices.get(i + 1).rhs.getIdentifier()
      );
    }

    int index = 0;
    for (Pair<QueryableIndex, DataSegment> pair : Lists.reverse(queryableIndices)) {
      final QueryableIndex queryableIndex = pair.lhs;
      final Map<String, DimensionHandler> dimensionHandlerMap = queryableIndex.getDimensionHandlers();

      for (String dimension : queryableIndex.getAvailableDimensions()) {
        final Column column = Preconditions.checkNotNull(
            queryableIndex.getColumn(dimension),
            "Cannot find column for dimension[%s]",
            dimension
        );

        if (!uniqueDims.containsKey(dimension)) {
          final DimensionHandler dimensionHandler = Preconditions.checkNotNull(
              dimensionHandlerMap.get(dimension),
              "Cannot find dimensionHandler for dimension[%s]",
              dimension
          );

          uniqueDims.put(dimension, index++);
          dimensionSchemaMap.put(
              dimension,
              createDimensionSchema(
                  column.getCapabilities().getType(),
                  dimension,
                  dimensionHandler.getMultivalueHandling(),
                  column.getCapabilities().hasBitmapIndexes()
              )
          );
        }
      }
    }

    final BiMap<Integer, String> orderedDims = uniqueDims.inverse();
    final List<DimensionSchema> dimensionSchemas = IntStream.range(0, orderedDims.size())
                                                            .mapToObj(i -> {
                                                              final String dimName = orderedDims.get(i);
                                                              return Preconditions.checkNotNull(
                                                                  dimensionSchemaMap.get(dimName),
                                                                  "Cannot find dimension[%s] from dimensionSchemaMap",
                                                                  dimName
                                                              );
                                                            })
                                                            .collect(Collectors.toList());

    return new DimensionsSpec(dimensionSchemas, null, null);
  }

  private static List<Pair<QueryableIndex, DataSegment>> loadSegments(
      List<TimelineObjectHolder<String, DataSegment>> timelineObjectHolders,
      Map<DataSegment, File> segmentFileMap,
      IndexIO indexIO
  ) throws IOException
  {
    final List<Pair<QueryableIndex, DataSegment>> segments = new ArrayList<>();

    for (TimelineObjectHolder<String, DataSegment> timelineObjectHolder : timelineObjectHolders) {
      final PartitionHolder<DataSegment> partitionHolder = timelineObjectHolder.getObject();
      for (PartitionChunk<DataSegment> chunk : partitionHolder) {
        final DataSegment segment = chunk.getObject();
        final QueryableIndex queryableIndex = indexIO.loadIndex(
            Preconditions.checkNotNull(segmentFileMap.get(segment), "File for segment %s", segment.getIdentifier())
        );
        segments.add(Pair.of(queryableIndex, segment));
      }
    }

    return segments;
  }

  private static DimensionSchema createDimensionSchema(
      ValueType type,
      String name,
      MultiValueHandling multiValueHandling,
      boolean hasBitmapIndexes
  )
  {
    switch (type) {
      case FLOAT:
        Preconditions.checkArgument(
            multiValueHandling == null,
            "multi-value dimension [%s] is not supported for float type yet",
            name
        );
        return new FloatDimensionSchema(name);
      case LONG:
        Preconditions.checkArgument(
            multiValueHandling == null,
            "multi-value dimension [%s] is not supported for long type yet",
            name
        );
        return new LongDimensionSchema(name);
      case DOUBLE:
        Preconditions.checkArgument(
            multiValueHandling == null,
            "multi-value dimension [%s] is not supported for double type yet",
            name
        );
        return new DoubleDimensionSchema(name);
      case STRING:
        return new StringDimensionSchema(name, multiValueHandling, hasBitmapIndexes);
      default:
        throw new ISE("Unsupported value type[%s] for dimension[%s]", type, name);
    }
  }

  @VisibleForTesting
  static class SegmentProvider
  {
    private final String dataSource;
    private final Interval interval;
    private final List<DataSegment> segments;

    SegmentProvider(String dataSource, Interval interval)
    {
      this.dataSource = Preconditions.checkNotNull(dataSource);
      this.interval = Preconditions.checkNotNull(interval);
      this.segments = null;
    }

    SegmentProvider(List<DataSegment> segments)
    {
      Preconditions.checkArgument(segments != null && !segments.isEmpty());
      final String dataSource = segments.get(0).getDataSource();
      Preconditions.checkArgument(
          segments.stream().allMatch(segment -> segment.getDataSource().equals(dataSource)),
          "segments should have the same dataSource"
      );
      this.segments = segments;
      this.dataSource = dataSource;
      this.interval = JodaUtils.umbrellaInterval(
          segments.stream().map(DataSegment::getInterval).collect(Collectors.toList())
      );
    }

    List<DataSegment> getSegments()
    {
      return segments;
    }

    List<DataSegment> checkAndGetSegments(TaskToolbox toolbox) throws IOException
    {
      final List<DataSegment> usedSegments = toolbox.getTaskActionClient()
                                                    .submit(new SegmentListUsedAction(dataSource, interval, null));
      if (segments != null) {
        Collections.sort(usedSegments);
        Collections.sort(segments);

        if (!usedSegments.equals(segments)) {
          final List<DataSegment> unknownSegments = segments.stream()
                                                            .filter(segment -> !usedSegments.contains(segment))
                                                            .collect(Collectors.toList());
          final List<DataSegment> missingSegments = usedSegments.stream()
                                                                .filter(segment -> !segments.contains(segment))
                                                                .collect(Collectors.toList());
          throw new ISE(
              "Specified segments in the spec are different from the current used segments. "
              + "There are unknown segments[%s] and missing segments[%s] in the spec.",
              unknownSegments,
              missingSegments
          );
        }
      }
      return usedSegments;
    }
  }
}

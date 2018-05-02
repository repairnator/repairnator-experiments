/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.druid.indexing.common.task;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import io.druid.indexer.HadoopDruidDetermineConfigurationJob;
import io.druid.indexer.HadoopDruidIndexerConfig;
import io.druid.indexer.HadoopDruidIndexerJob;
import io.druid.indexer.HadoopIngestionSpec;
import io.druid.indexer.IngestionState;
import io.druid.indexer.MetadataStorageUpdaterJobHandler;
import io.druid.indexer.TaskMetricsGetter;
import io.druid.indexer.TaskMetricsUtils;
import io.druid.indexing.common.IngestionStatsAndErrorsTaskReport;
import io.druid.indexing.common.IngestionStatsAndErrorsTaskReportData;
import io.druid.indexing.common.TaskLock;
import io.druid.indexing.common.TaskLockType;
import io.druid.indexing.common.TaskReport;
import io.druid.indexing.common.TaskStatus;
import io.druid.indexing.common.TaskToolbox;
import io.druid.indexing.common.actions.LockAcquireAction;
import io.druid.indexing.common.actions.LockTryAcquireAction;
import io.druid.indexing.common.actions.TaskActionClient;
import io.druid.indexing.hadoop.OverlordActionBasedUsedSegmentLister;
import io.druid.java.util.common.DateTimes;
import io.druid.java.util.common.JodaUtils;
import io.druid.java.util.common.StringUtils;
import io.druid.java.util.common.logger.Logger;
import io.druid.segment.realtime.firehose.ChatHandler;
import io.druid.segment.realtime.firehose.ChatHandlerProvider;
import io.druid.server.security.Action;
import io.druid.server.security.AuthorizerMapper;
import io.druid.timeline.DataSegment;
import org.joda.time.Interval;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class HadoopIndexTask extends HadoopTask implements ChatHandler
{
  private static final Logger log = new Logger(HadoopIndexTask.class);

  private static String getTheDataSource(HadoopIngestionSpec spec)
  {
    return spec.getDataSchema().getDataSource();
  }

  @JsonIgnore
  private HadoopIngestionSpec spec;

  @JsonIgnore
  private final String classpathPrefix;

  @JsonIgnore
  private final ObjectMapper jsonMapper;

  @JsonIgnore
  private final AuthorizerMapper authorizerMapper;

  @JsonIgnore
  private final Optional<ChatHandlerProvider> chatHandlerProvider;

  @JsonIgnore
  private InnerProcessingStatsGetter determinePartitionsStatsGetter;

  @JsonIgnore
  private InnerProcessingStatsGetter buildSegmentsStatsGetter;

  @JsonIgnore
  private IngestionState ingestionState;

  @JsonIgnore
  private HadoopDetermineConfigInnerProcessingStatus determineConfigStatus = null;

  @JsonIgnore
  private HadoopIndexGeneratorInnerProcessingStatus buildSegmentsStatus = null;

  @JsonIgnore
  private String errorMsg;

  /**
   * @param spec is used by the HadoopDruidIndexerJob to set up the appropriate parameters
   *             for creating Druid index segments. It may be modified.
   *             <p/>
   *             Here, we will ensure that the DbConnectorConfig field of the spec is set to null, such that the
   *             job does not push a list of published segments the database. Instead, we will use the method
   *             IndexGeneratorJob.getPublishedSegments() to simply return a list of the published
   *             segments, and let the indexing service report these segments to the database.
   */

  @JsonCreator
  public HadoopIndexTask(
      @JsonProperty("id") String id,
      @JsonProperty("spec") HadoopIngestionSpec spec,
      @JsonProperty("hadoopCoordinates") String hadoopCoordinates,
      @JsonProperty("hadoopDependencyCoordinates") List<String> hadoopDependencyCoordinates,
      @JsonProperty("classpathPrefix") String classpathPrefix,
      @JacksonInject ObjectMapper jsonMapper,
      @JsonProperty("context") Map<String, Object> context,
      @JacksonInject AuthorizerMapper authorizerMapper,
      @JacksonInject ChatHandlerProvider chatHandlerProvider
  )
  {
    super(
        id != null ? id : StringUtils.format("index_hadoop_%s_%s", getTheDataSource(spec), DateTimes.nowUtc()),
        getTheDataSource(spec),
        hadoopDependencyCoordinates == null
        ? (hadoopCoordinates == null ? null : ImmutableList.of(hadoopCoordinates))
        : hadoopDependencyCoordinates,
        context
    );
    this.authorizerMapper = authorizerMapper;
    this.chatHandlerProvider = Optional.fromNullable(chatHandlerProvider);
    this.spec = spec;

    // Some HadoopIngestionSpec stuff doesn't make sense in the context of the indexing service
    Preconditions.checkArgument(
        this.spec.getIOConfig().getSegmentOutputPath() == null,
        "segmentOutputPath must be absent"
    );
    Preconditions.checkArgument(this.spec.getTuningConfig().getWorkingPath() == null, "workingPath must be absent");
    Preconditions.checkArgument(
        this.spec.getIOConfig().getMetadataUpdateSpec() == null,
        "metadataUpdateSpec must be absent"
    );

    this.classpathPrefix = classpathPrefix;
    this.jsonMapper = Preconditions.checkNotNull(jsonMapper, "null ObjectMappper");
    this.ingestionState = IngestionState.NOT_STARTED;
  }

  @Override
  public int getPriority()
  {
    return getContextValue(Tasks.PRIORITY_KEY, Tasks.DEFAULT_BATCH_INDEX_TASK_PRIORITY);
  }

  @Override
  public String getType()
  {
    return "index_hadoop";
  }

  @Override
  public boolean isReady(TaskActionClient taskActionClient) throws Exception
  {
    Optional<SortedSet<Interval>> intervals = spec.getDataSchema().getGranularitySpec().bucketIntervals();
    if (intervals.isPresent()) {
      Interval interval = JodaUtils.umbrellaInterval(
          JodaUtils.condenseIntervals(
              intervals.get()
          )
      );
      return taskActionClient.submit(new LockTryAcquireAction(TaskLockType.EXCLUSIVE, interval)) != null;
    } else {
      return true;
    }
  }

  @JsonProperty("spec")
  public HadoopIngestionSpec getSpec()
  {
    return spec;
  }

  @Override
  @JsonProperty
  public List<String> getHadoopDependencyCoordinates()
  {
    return super.getHadoopDependencyCoordinates();
  }

  @JsonProperty
  @Override
  public String getClasspathPrefix()
  {
    return classpathPrefix;
  }

  @Override
  public TaskStatus run(TaskToolbox toolbox) throws Exception
  {
    try {
      if (chatHandlerProvider.isPresent()) {
        log.info("Found chat handler of class[%s]", chatHandlerProvider.get().getClass().getName());
        chatHandlerProvider.get().register(getId(), this, false);
      } else {
        log.warn("No chat handler detected");
      }

      return runInternal(toolbox);
    }
    catch (Exception e) {
      Throwable effectiveException;
      if (e instanceof RuntimeException && e.getCause() instanceof InvocationTargetException) {
        InvocationTargetException ite = (InvocationTargetException) e.getCause();
        effectiveException = ite.getCause();
        log.error(effectiveException, "Got invocation target exception in run(), cause: ");
      } else {
        effectiveException = e;
        log.error(e, "Encountered exception in run():");
      }

      errorMsg = Throwables.getStackTraceAsString(effectiveException);
      toolbox.getTaskReportFileWriter().write(getTaskCompletionReports());
      return TaskStatus.failure(
          getId(),
          errorMsg
      );
    }
    finally {
      if (chatHandlerProvider.isPresent()) {
        chatHandlerProvider.get().unregister(getId());
      }
    }
  }

  @SuppressWarnings("unchecked")
  private TaskStatus runInternal(TaskToolbox toolbox) throws Exception
  {
    final ClassLoader loader = buildClassLoader(toolbox);
    boolean determineIntervals = !spec.getDataSchema().getGranularitySpec().bucketIntervals().isPresent();

    spec = HadoopIngestionSpec.updateSegmentListIfDatasourcePathSpecIsUsed(
        spec,
        jsonMapper,
        new OverlordActionBasedUsedSegmentLister(toolbox)
    );

    Object determinePartitionsInnerProcessingRunner = getForeignClassloaderObject(
        "io.druid.indexing.common.task.HadoopIndexTask$HadoopDetermineConfigInnerProcessingRunner",
        loader
    );
    determinePartitionsStatsGetter = new InnerProcessingStatsGetter(determinePartitionsInnerProcessingRunner);

    String[] determinePartitionsInput = new String[]{
        toolbox.getObjectMapper().writeValueAsString(spec),
        toolbox.getConfig().getHadoopWorkingPath(),
        toolbox.getSegmentPusher().getPathForHadoop()
    };

    HadoopIngestionSpec indexerSchema = null;
    final ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
    Class<?> determinePartitionsRunnerClass = determinePartitionsInnerProcessingRunner.getClass();
    Method determinePartitionsInnerProcessingRunTask = determinePartitionsRunnerClass.getMethod(
        "runTask",
        determinePartitionsInput.getClass()
    );
    try {
      Thread.currentThread().setContextClassLoader(loader);

      ingestionState = IngestionState.DETERMINE_PARTITIONS;

      final String determineConfigStatusString = (String) determinePartitionsInnerProcessingRunTask.invoke(
          determinePartitionsInnerProcessingRunner,
          new Object[]{determinePartitionsInput}
      );


      determineConfigStatus = toolbox
          .getObjectMapper()
          .readValue(determineConfigStatusString, HadoopDetermineConfigInnerProcessingStatus.class);

      indexerSchema = determineConfigStatus.getSchema();
      if (indexerSchema == null) {
        errorMsg = determineConfigStatus.getErrorMsg();
        toolbox.getTaskReportFileWriter().write(getTaskCompletionReports());
        return TaskStatus.failure(
            getId(),
            errorMsg
        );
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    finally {
      Thread.currentThread().setContextClassLoader(oldLoader);
    }

    // We should have a lock from before we started running only if interval was specified
    String version;
    if (determineIntervals) {
      Interval interval = JodaUtils.umbrellaInterval(
          JodaUtils.condenseIntervals(
              indexerSchema.getDataSchema().getGranularitySpec().bucketIntervals().get()
          )
      );
      final long lockTimeoutMs = getContextValue(Tasks.LOCK_TIMEOUT_KEY, Tasks.DEFAULT_LOCK_TIMEOUT);
      // Note: if lockTimeoutMs is larger than ServerConfig.maxIdleTime, the below line can incur http timeout error.
      final TaskLock lock = Preconditions.checkNotNull(
          toolbox.getTaskActionClient().submit(
              new LockAcquireAction(TaskLockType.EXCLUSIVE, interval, lockTimeoutMs)
          ),
          "Cannot acquire a lock for interval[%s]", interval
      );
      version = lock.getVersion();
    } else {
      Iterable<TaskLock> locks = getTaskLocks(toolbox.getTaskActionClient());
      final TaskLock myLock = Iterables.getOnlyElement(locks);
      version = myLock.getVersion();
    }

    final String specVersion = indexerSchema.getTuningConfig().getVersion();
    if (indexerSchema.getTuningConfig().isUseExplicitVersion()) {
      if (specVersion.compareTo(version) < 0) {
        version = specVersion;
      } else {
        log.error(
            "Spec version can not be greater than or equal to the lock version, Spec version: [%s] Lock version: [%s].",
            specVersion,
            version
        );
        toolbox.getTaskReportFileWriter().write(null);
        return TaskStatus.failure(getId());
      }
    }

    log.info("Setting version to: %s", version);

    Object innerProcessingRunner = getForeignClassloaderObject(
        "io.druid.indexing.common.task.HadoopIndexTask$HadoopIndexGeneratorInnerProcessingRunner",
        loader
    );
    buildSegmentsStatsGetter = new InnerProcessingStatsGetter(innerProcessingRunner);

    String[] buildSegmentsInput = new String[]{
        toolbox.getObjectMapper().writeValueAsString(indexerSchema),
        version
    };

    Class<?> buildSegmentsRunnerClass = innerProcessingRunner.getClass();
    Method innerProcessingRunTask = buildSegmentsRunnerClass.getMethod("runTask", buildSegmentsInput.getClass());

    try {
      Thread.currentThread().setContextClassLoader(loader);

      ingestionState = IngestionState.BUILD_SEGMENTS;
      final String jobStatusString = (String) innerProcessingRunTask.invoke(
          innerProcessingRunner,
          new Object[]{buildSegmentsInput}
      );

      buildSegmentsStatus = toolbox.getObjectMapper().readValue(
          jobStatusString,
          HadoopIndexGeneratorInnerProcessingStatus.class
      );

      if (buildSegmentsStatus.getDataSegments() != null) {
        ingestionState = IngestionState.COMPLETED;
        toolbox.publishSegments(buildSegmentsStatus.getDataSegments());
        toolbox.getTaskReportFileWriter().write(getTaskCompletionReports());
        return TaskStatus.success(
            getId(),
            null
        );
      } else {
        errorMsg = buildSegmentsStatus.getErrorMsg();
        toolbox.getTaskReportFileWriter().write(getTaskCompletionReports());
        return TaskStatus.failure(
            getId(),
            errorMsg
        );
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    finally {
      Thread.currentThread().setContextClassLoader(oldLoader);
    }
  }

  @GET
  @Path("/rowStats")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRowStats(
      @Context final HttpServletRequest req,
      @QueryParam("windows") List<Integer> windows
  )
  {
    IndexTaskUtils.datasourceAuthorizationCheck(req, Action.READ, getDataSource(), authorizerMapper);
    Map<String, Object> returnMap = Maps.newHashMap();
    Map<String, Object> totalsMap = Maps.newHashMap();

    if (determinePartitionsStatsGetter != null) {
      totalsMap.put("determinePartitions", determinePartitionsStatsGetter.getTotalMetrics());
    }

    if (buildSegmentsStatsGetter != null) {
      totalsMap.put("buildSegments", buildSegmentsStatsGetter.getTotalMetrics());
    }

    returnMap.put("totals", totalsMap);
    return Response.ok(returnMap).build();
  }

  private Map<String, TaskReport> getTaskCompletionReports()
  {
    return TaskReport.buildTaskReports(
        new IngestionStatsAndErrorsTaskReport(
            getId(),
            new IngestionStatsAndErrorsTaskReportData(
                ingestionState,
                null,
                getTaskCompletionRowStats(),
                errorMsg
            )
        )
    );
  }

  private Map<String, Object> getTaskCompletionRowStats()
  {
    Map<String, Object> metrics = Maps.newHashMap();
    if (determineConfigStatus != null) {
      metrics.put(
          "determinePartitions",
          determineConfigStatus.getMetrics()
      );
    }
    if (buildSegmentsStatus != null) {
      metrics.put(
          "buildSegments",
          buildSegmentsStatus.getMetrics()
      );
    }
    return metrics;
  }

  public static class InnerProcessingStatsGetter implements TaskMetricsGetter
  {
    public static final List<String> KEYS = Arrays.asList(
        TaskMetricsUtils.ROWS_PROCESSED,
        TaskMetricsUtils.ROWS_PROCESSED_WITH_ERRORS,
        TaskMetricsUtils.ROWS_THROWN_AWAY,
        TaskMetricsUtils.ROWS_UNPARSEABLE
    );

    private final Method getStatsMethod;
    private final Object innerProcessingRunner;

    public InnerProcessingStatsGetter(
        Object innerProcessingRunner
    )
    {
      try {
        Class<?> aClazz = innerProcessingRunner.getClass();
        this.getStatsMethod = aClazz.getMethod("getStats");
        this.innerProcessingRunner = innerProcessingRunner;
      }
      catch (NoSuchMethodException nsme) {
        throw new RuntimeException(nsme);
      }
    }

    @Override
    public List<String> getKeys()
    {
      return KEYS;
    }

    @Override
    public Map<String, Number> getTotalMetrics()
    {
      try {
        Map<String, Object> statsMap = (Map<String, Object>) getStatsMethod.invoke(innerProcessingRunner);
        if (statsMap == null) {
          return null;
        }
        long curProcessed = (Long) statsMap.get(TaskMetricsUtils.ROWS_PROCESSED);
        long curProcessedWithErrors = (Long) statsMap.get(TaskMetricsUtils.ROWS_PROCESSED_WITH_ERRORS);
        long curThrownAway = (Long) statsMap.get(TaskMetricsUtils.ROWS_THROWN_AWAY);
        long curUnparseable = (Long) statsMap.get(TaskMetricsUtils.ROWS_UNPARSEABLE);

        return ImmutableMap.of(
            TaskMetricsUtils.ROWS_PROCESSED, curProcessed,
            TaskMetricsUtils.ROWS_PROCESSED_WITH_ERRORS, curProcessedWithErrors,
            TaskMetricsUtils.ROWS_THROWN_AWAY, curThrownAway,
            TaskMetricsUtils.ROWS_UNPARSEABLE, curUnparseable
        );
      }
      catch (Exception e) {
        log.error(e, "Got exception from getTotalMetrics(): ");
        return null;
      }
    }
  }


  /** Called indirectly in {@link HadoopIndexTask#run(TaskToolbox)}. */
  @SuppressWarnings("unused")
  public static class HadoopDetermineConfigInnerProcessingRunner
  {
    private HadoopDruidDetermineConfigurationJob job;

    public String runTask(String[] args) throws Exception
    {
      final String schema = args[0];
      final String workingPath = args[1];
      final String segmentOutputPath = args[2];

      final HadoopIngestionSpec theSchema = HadoopDruidIndexerConfig.JSON_MAPPER
          .readValue(
              schema,
              HadoopIngestionSpec.class
          );
      final HadoopDruidIndexerConfig config = HadoopDruidIndexerConfig.fromSpec(
          theSchema
              .withIOConfig(theSchema.getIOConfig().withSegmentOutputPath(segmentOutputPath))
              .withTuningConfig(theSchema.getTuningConfig().withWorkingPath(workingPath))
      );

      job = new HadoopDruidDetermineConfigurationJob(config);

      log.info("Starting a hadoop determine configuration job...");
      if (job.run()) {
        return HadoopDruidIndexerConfig.JSON_MAPPER.writeValueAsString(
            new HadoopDetermineConfigInnerProcessingStatus(config.getSchema(), job.getStats(), null)
        );
      } else {
        return HadoopDruidIndexerConfig.JSON_MAPPER.writeValueAsString(
            new HadoopDetermineConfigInnerProcessingStatus(null, job.getStats(), job.getErrorMessage())
        );
      }
    }

    public Map<String, Object> getStats()
    {
      if (job == null) {
        return null;
      }

      return job.getStats();
    }
  }

  @SuppressWarnings("unused")
  public static class HadoopIndexGeneratorInnerProcessingRunner
  {
    private HadoopDruidIndexerJob job;

    public String runTask(String[] args) throws Exception
    {
      final String schema = args[0];
      String version = args[1];

      final HadoopIngestionSpec theSchema = HadoopDruidIndexerConfig.JSON_MAPPER
          .readValue(
              schema,
              HadoopIngestionSpec.class
          );
      final HadoopDruidIndexerConfig config = HadoopDruidIndexerConfig.fromSpec(
          theSchema
              .withTuningConfig(theSchema.getTuningConfig().withVersion(version))
      );

      // MetadataStorageUpdaterJobHandler is only needed when running standalone without indexing service
      // In that case the whatever runs the Hadoop Index Task must ensure MetadataStorageUpdaterJobHandler
      // can be injected based on the configuration given in config.getSchema().getIOConfig().getMetadataUpdateSpec()
      final MetadataStorageUpdaterJobHandler maybeHandler;
      if (config.isUpdaterJobSpecSet()) {
        maybeHandler = injector.getInstance(MetadataStorageUpdaterJobHandler.class);
      } else {
        maybeHandler = null;
      }
      job = new HadoopDruidIndexerJob(config, maybeHandler);

      log.info("Starting a hadoop index generator job...");
      try {
        if (job.run()) {
          return HadoopDruidIndexerConfig.JSON_MAPPER.writeValueAsString(
              new HadoopIndexGeneratorInnerProcessingStatus(
                  job.getPublishedSegments(),
                  job.getStats(),
                  null
              )
          );
        } else {
          return HadoopDruidIndexerConfig.JSON_MAPPER.writeValueAsString(
              new HadoopIndexGeneratorInnerProcessingStatus(
                  null,
                  job.getStats(),
                  job.getErrorMessage()
              )
          );
        }
      }
      catch (Exception e) {
        log.error(e, "Encountered exception in HadoopIndexGeneratorInnerProcessing.");
        return HadoopDruidIndexerConfig.JSON_MAPPER.writeValueAsString(
            new HadoopIndexGeneratorInnerProcessingStatus(
                null,
                job.getStats(),
                e.getMessage()
            )
        );
      }
    }

    public Map<String, Object> getStats()
    {
      if (job == null) {
        return null;
      }

      return job.getStats();
    }
  }

  public static class HadoopIndexGeneratorInnerProcessingStatus
  {
    private final List<DataSegment> dataSegments;
    private final Map<String, Object> metrics;
    private final String errorMsg;

    @JsonCreator
    public HadoopIndexGeneratorInnerProcessingStatus(
        @JsonProperty("dataSegments") List<DataSegment> dataSegments,
        @JsonProperty("metrics") Map<String, Object> metrics,
        @JsonProperty("errorMsg") String errorMsg
    )
    {
      this.dataSegments = dataSegments;
      this.metrics = metrics;
      this.errorMsg = errorMsg;
    }

    @JsonProperty
    public List<DataSegment> getDataSegments()
    {
      return dataSegments;
    }

    @JsonProperty
    public Map<String, Object> getMetrics()
    {
      return metrics;
    }

    @JsonProperty
    public String getErrorMsg()
    {
      return errorMsg;
    }
  }

  public static class HadoopDetermineConfigInnerProcessingStatus
  {
    private final HadoopIngestionSpec schema;
    private final Map<String, Object> metrics;
    private final String errorMsg;

    @JsonCreator
    public HadoopDetermineConfigInnerProcessingStatus(
        @JsonProperty("schema") HadoopIngestionSpec schema,
        @JsonProperty("metrics") Map<String, Object> metrics,
        @JsonProperty("errorMsg") String errorMsg
    )
    {
      this.schema = schema;
      this.metrics = metrics;
      this.errorMsg = errorMsg;
    }

    @JsonProperty
    public HadoopIngestionSpec getSchema()
    {
      return schema;
    }

    @JsonProperty
    public Map<String, Object> getMetrics()
    {
      return metrics;
    }

    @JsonProperty
    public String getErrorMsg()
    {
      return errorMsg;
    }
  }
}

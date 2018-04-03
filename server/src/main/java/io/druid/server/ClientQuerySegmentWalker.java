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

package io.druid.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.druid.java.util.emitter.service.ServiceEmitter;
import io.druid.client.CachingClusteredClient;
import io.druid.client.cache.Cache;
import io.druid.client.cache.CacheConfig;
import io.druid.query.FluentQueryRunnerBuilder;
import io.druid.query.PostProcessingOperator;
import io.druid.query.Query;
import io.druid.query.QueryRunner;
import io.druid.query.QuerySegmentWalker;
import io.druid.query.QueryToolChest;
import io.druid.query.QueryToolChestWarehouse;
import io.druid.query.ResultLevelCachingQueryRunner;
import io.druid.query.RetryQueryRunner;
import io.druid.query.RetryQueryRunnerConfig;
import io.druid.query.SegmentDescriptor;
import io.druid.server.initialization.ServerConfig;
import org.joda.time.Interval;

/**
 */
public class ClientQuerySegmentWalker implements QuerySegmentWalker
{
  private final ServiceEmitter emitter;
  private final CachingClusteredClient baseClient;
  private final QueryToolChestWarehouse warehouse;
  private final RetryQueryRunnerConfig retryConfig;
  private final ObjectMapper objectMapper;
  private final ServerConfig serverConfig;
  private final Cache cache;
  private final CacheConfig cacheConfig;


  @Inject
  public ClientQuerySegmentWalker(
      ServiceEmitter emitter,
      CachingClusteredClient baseClient,
      QueryToolChestWarehouse warehouse,
      RetryQueryRunnerConfig retryConfig,
      ObjectMapper objectMapper,
      ServerConfig serverConfig,
      Cache cache,
      CacheConfig cacheConfig
  )
  {
    this.emitter = emitter;
    this.baseClient = baseClient;
    this.warehouse = warehouse;
    this.retryConfig = retryConfig;
    this.objectMapper = objectMapper;
    this.serverConfig = serverConfig;
    this.cache = cache;
    this.cacheConfig = cacheConfig;
  }

  @Override
  public <T> QueryRunner<T> getQueryRunnerForIntervals(Query<T> query, Iterable<Interval> intervals)
  {
    return makeRunner(query, baseClient.getQueryRunnerForIntervals(query, intervals));
  }

  @Override
  public <T> QueryRunner<T> getQueryRunnerForSegments(Query<T> query, Iterable<SegmentDescriptor> specs)
  {
    return makeRunner(query, baseClient.getQueryRunnerForSegments(query, specs));
  }

  private <T> QueryRunner<T> makeRunner(Query<T> query, QueryRunner<T> baseClientRunner)
  {
    QueryToolChest<T, Query<T>> toolChest = warehouse.getToolChest(query);

    // This does not adhere to the fluent workflow. See https://github.com/druid-io/druid/issues/5517
    return new ResultLevelCachingQueryRunner<>(makeRunner(query, baseClientRunner, toolChest),
                                               toolChest,
                                               query,
                                               objectMapper,
                                               cache,
                                               cacheConfig);
  }

  private <T> QueryRunner<T> makeRunner(
      Query<T> query,
      QueryRunner<T> baseClientRunner,
      QueryToolChest<T, Query<T>> toolChest
  )
  {
    PostProcessingOperator<T> postProcessing = objectMapper.convertValue(
        query.<String>getContextValue("postProcessing"),
        new TypeReference<PostProcessingOperator<T>>()
        {
        }
    );

    return new FluentQueryRunnerBuilder<>(toolChest)
        .create(
            new SetAndVerifyContextQueryRunner(
                serverConfig,
                new RetryQueryRunner<>(
                    baseClientRunner,
                    retryConfig,
                    objectMapper
                )
            )
        )
        .applyPreMergeDecoration()
        .mergeResults()
        .applyPostMergeDecoration()
        .emitCPUTimeMetric(emitter)
        .postProcess(postProcessing);
  }
}

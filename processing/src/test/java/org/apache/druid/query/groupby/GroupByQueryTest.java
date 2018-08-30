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

package org.apache.druid.query.groupby;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import org.apache.druid.data.input.MapBasedRow;
import org.apache.druid.data.input.Row;
import org.apache.druid.java.util.common.Intervals;
import org.apache.druid.java.util.common.granularity.Granularities;
import org.apache.druid.query.BaseQuery;
import org.apache.druid.query.Query;
import org.apache.druid.query.QueryRunnerTestHelper;
import org.apache.druid.query.aggregation.AggregatorFactory;
import org.apache.druid.query.aggregation.LongSumAggregatorFactory;
import org.apache.druid.query.aggregation.post.FieldAccessPostAggregator;
import org.apache.druid.query.dimension.DefaultDimensionSpec;
import org.apache.druid.query.groupby.orderby.DefaultLimitSpec;
import org.apache.druid.query.groupby.orderby.OrderByColumnSpec;
import org.apache.druid.query.ordering.StringComparators;
import org.apache.druid.query.spec.MultipleIntervalSegmentSpec;
import org.apache.druid.query.spec.QuerySegmentSpec;
import org.apache.druid.segment.TestHelper;
import org.apache.druid.segment.column.ValueType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GroupByQueryTest
{
  private static final ObjectMapper jsonMapper = TestHelper.makeJsonMapper();

  @Test
  public void testQuerySerialization() throws IOException
  {
    Query query = GroupByQuery
        .builder()
        .setDataSource(QueryRunnerTestHelper.dataSource)
        .setQuerySegmentSpec(QueryRunnerTestHelper.firstToThird)
        .setDimensions(new DefaultDimensionSpec("quality", "alias"))
        .setAggregatorSpecs(QueryRunnerTestHelper.rowsCount, new LongSumAggregatorFactory("idx", "index"))
        .setGranularity(QueryRunnerTestHelper.dayGran)
        .setPostAggregatorSpecs(ImmutableList.of(new FieldAccessPostAggregator("x", "idx")))
        .setLimitSpec(
            new DefaultLimitSpec(
                ImmutableList.of(new OrderByColumnSpec(
                    "alias",
                    OrderByColumnSpec.Direction.ASCENDING,
                    StringComparators.LEXICOGRAPHIC
                )),
                100
            )
        )
        .build();

    String json = jsonMapper.writeValueAsString(query);
    Query serdeQuery = jsonMapper.readValue(json, Query.class);

    Assert.assertEquals(query, serdeQuery);
  }

  @Test
  public void testRowOrderingMixTypes()
  {
    final GroupByQuery query = GroupByQuery.builder()
                                           .setDataSource("dummy")
                                           .setGranularity(Granularities.ALL)
                                           .setInterval("2000/2001")
                                           .addDimension(new DefaultDimensionSpec("foo", "foo", ValueType.LONG))
                                           .addDimension(new DefaultDimensionSpec("bar", "bar", ValueType.FLOAT))
                                           .addDimension(new DefaultDimensionSpec("baz", "baz", ValueType.STRING))
                                           .build();

    final Ordering<Row> rowOrdering = query.getRowOrdering(false);
    final int compare = rowOrdering.compare(
        new MapBasedRow(0L, ImmutableMap.of("foo", 1, "bar", 1f, "baz", "a")),
        new MapBasedRow(0L, ImmutableMap.of("foo", 1L, "bar", 1d, "baz", "b"))
    );
    Assert.assertEquals(-1, compare);
  }

  @Test
  public void testSegmentLookUpForNestedQueries()
  {
    QuerySegmentSpec innerQuerySegmentSpec = new MultipleIntervalSegmentSpec(Collections.singletonList(Intervals.of(
        "2011-11-07/2011-11-08")));
    QuerySegmentSpec outerQuerySegmentSpec = new MultipleIntervalSegmentSpec(Collections.singletonList((Intervals.of(
        "2011-11-04/2011-11-08"))));
    List<AggregatorFactory> aggs = Collections.singletonList(QueryRunnerTestHelper.rowsCount);
    final GroupByQuery innerQuery = GroupByQuery.builder()
                                                .setDataSource("blah")
                                                .setInterval(innerQuerySegmentSpec)
                                                .setGranularity(Granularities.DAY)
                                                .setAggregatorSpecs(aggs)
                                                .build();
    final GroupByQuery query = GroupByQuery
        .builder()
        .setDataSource(innerQuery)
        .setInterval(outerQuerySegmentSpec)
        .setAggregatorSpecs(aggs)
        .setGranularity(Granularities.DAY)
        .build();
    Assert.assertEquals(innerQuerySegmentSpec, BaseQuery.getQuerySegmentSpecForLookUp(query));
  }
}

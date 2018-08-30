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

package org.apache.druid.query.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

/**
 */
public class SearchQueryConfig
{
  public static final String CTX_KEY_STRATEGY = "searchStrategy";

  @JsonProperty
  @Min(1)
  private int maxSearchLimit = 1000;

  @JsonProperty
  private String searchStrategy = UseIndexesStrategy.NAME;

  public int getMaxSearchLimit()
  {
    return maxSearchLimit;
  }

  public String getSearchStrategy()
  {
    return searchStrategy;
  }

  public void setSearchStrategy(final String strategy)
  {
    this.searchStrategy = strategy;
  }

  public SearchQueryConfig withOverrides(final SearchQuery query)
  {
    final SearchQueryConfig newConfig = new SearchQueryConfig();
    newConfig.maxSearchLimit = query.getLimit();
    newConfig.searchStrategy = query.getContextValue(CTX_KEY_STRATEGY, searchStrategy);
    return newConfig;
  }
}

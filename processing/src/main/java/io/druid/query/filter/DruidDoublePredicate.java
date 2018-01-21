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

package io.druid.query.filter;


public interface DruidDoublePredicate
{
  DruidDoublePredicate ALWAYS_FALSE = input -> false;

  DruidDoublePredicate ALWAYS_TRUE = input -> true;

  DruidDoublePredicate MATCH_NULL_ONLY = new DruidDoublePredicate()
  {
    @Override
    public boolean applyDouble(double input)
    {
      return false;
    }

    @Override
    public boolean applyNull()
    {
      return true;
    }
  };

  boolean applyDouble(double input);

  default boolean applyNull()
  {
    return false;
  }
}

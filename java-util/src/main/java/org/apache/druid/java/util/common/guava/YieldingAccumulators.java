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

package org.apache.druid.java.util.common.guava;

/**
 */
public class YieldingAccumulators
{
  public static <AccumulatedType, InType> YieldingAccumulator<AccumulatedType, InType> fromAccumulator(
      final Accumulator<AccumulatedType, InType> accumulator
  )
  {
    return new YieldingAccumulator<AccumulatedType, InType>()
    {
      @Override
      public AccumulatedType accumulate(AccumulatedType accumulated, InType in)
      {
        return accumulator.accumulate(accumulated, in);
      }
    };
  }
}

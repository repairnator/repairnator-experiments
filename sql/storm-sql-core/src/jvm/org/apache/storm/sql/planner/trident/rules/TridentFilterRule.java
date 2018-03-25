/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.storm.sql.planner.trident.rules;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.logical.LogicalFilter;
import org.apache.storm.sql.planner.trident.rel.TridentFilterRel;
import org.apache.storm.sql.planner.trident.rel.TridentLogicalConvention;

public class TridentFilterRule extends ConverterRule {
  public static TridentFilterRule INSTANCE = new TridentFilterRule();

  private TridentFilterRule() {
    super(LogicalFilter.class, Convention.NONE, TridentLogicalConvention.INSTANCE, "TridentFilterRule");
  }

  @Override
  public RelNode convert(RelNode rel) {
    final Filter filter = (Filter) rel;
    final RelNode input = filter.getInput();

    return new TridentFilterRel(filter.getCluster(),
        filter.getTraitSet().replace(TridentLogicalConvention.INSTANCE),
        convert(input, input.getTraitSet().replace(TridentLogicalConvention.INSTANCE)),
        filter.getCondition());
  }
}

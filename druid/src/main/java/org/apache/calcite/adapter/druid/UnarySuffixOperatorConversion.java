/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.adapter.druid;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlOperator;

import com.google.common.collect.Iterables;

import java.util.List;

/**
 * Unary suffix operator conversion, used to convert function like: expression Unary_Operator
 */
public class UnarySuffixOperatorConversion implements DruidSqlOperatorConverter {
  private final SqlOperator operator;
  private final String druidOperator;

  public UnarySuffixOperatorConversion(SqlOperator operator, String druidOperator) {
    this.operator = operator;
    this.druidOperator = druidOperator;
  }

  @Override public SqlOperator calciteOperator() {
    return operator;
  }

  @Override public String toDruidExpression(RexNode rexNode, RelDataType rowType,
      DruidQuery druidQuery) {
    final RexCall call = (RexCall) rexNode;

    final List<String> druidExpressions = DruidExpressions.toDruidExpressions(
        druidQuery, rowType,
        call.getOperands());

    if (druidExpressions == null) {
      return null;
    }

    return DruidQuery.format(
            "(%s %s)",
            Iterables.getOnlyElement(druidExpressions), druidOperator);
  }
}

// End UnarySuffixOperatorConversion.java

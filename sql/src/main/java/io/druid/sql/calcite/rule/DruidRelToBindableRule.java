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

package io.druid.sql.calcite.rule;

import io.druid.sql.calcite.rel.DruidRel;
import org.apache.calcite.interpreter.BindableConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;

public class DruidRelToBindableRule extends ConverterRule
{
  private static DruidRelToBindableRule INSTANCE = new DruidRelToBindableRule();

  private DruidRelToBindableRule()
  {
    super(
        DruidRel.class,
        Convention.NONE,
        BindableConvention.INSTANCE,
        DruidRelToBindableRule.class.getSimpleName()
    );
  }

  public static DruidRelToBindableRule instance()
  {
    return INSTANCE;
  }

  @Override
  public RelNode convert(RelNode rel)
  {
    return ((DruidRel) rel).asBindable();
  }
}

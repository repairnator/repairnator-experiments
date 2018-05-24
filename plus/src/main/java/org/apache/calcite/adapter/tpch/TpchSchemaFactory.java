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
package org.apache.calcite.adapter.tpch;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.util.Util;

import java.util.Map;

/**
 * Factory that creates a {@link TpchSchema}.
 *
 * <p>Allows a custom schema to be included in a model.json file.</p>
 */
@SuppressWarnings("UnusedDeclaration")
public class TpchSchemaFactory implements SchemaFactory {
  // public constructor, per factory contract
  public TpchSchemaFactory() {
  }

  public Schema create(SchemaPlus parentSchema, String name,
      Map<String, Object> operand) {
    Map map = (Map) operand;
    double scale = Util.first((Double) map.get("scale"), 1D);
    int part = Util.first((Integer) map.get("part"), 1);
    int partCount = Util.first((Integer) map.get("partCount"), 1);
    boolean columnPrefix = Util.first((Boolean) map.get("columnPrefix"), true);
    return new TpchSchema(scale, part, partCount, columnPrefix);
  }
}

// End TpchSchemaFactory.java

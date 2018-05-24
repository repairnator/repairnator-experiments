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
package org.apache.calcite.sql.test;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.advise.SqlAdvisor;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorWithHints;
import org.apache.calcite.test.MockCatalogReader;

/**
* Creates the objects needed to run a SQL parsing or validation test.
 *
 * @see org.apache.calcite.sql.test.SqlTester
*/
public interface SqlTestFactory {
  MockCatalogReader createCatalogReader(SqlTestFactory testFactory,
      JavaTypeFactory typeFactory);
  SqlOperatorTable createOperatorTable(SqlTestFactory factory);
  SqlParser createParser(SqlTestFactory factory, String sql);
  SqlValidator getValidator(SqlTestFactory factory);
  SqlAdvisor createAdvisor(SqlValidatorWithHints validator);
  Object get(String name);
}

// End SqlTestFactory.java

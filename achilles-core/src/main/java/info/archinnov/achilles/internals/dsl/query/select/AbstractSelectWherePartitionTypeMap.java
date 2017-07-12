/*
 * Copyright (C) 2012-2016 DuyHai DOAN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.archinnov.achilles.internals.dsl.query.select;


import com.datastax.driver.core.querybuilder.Select;

import info.archinnov.achilles.internals.options.CassandraOptions;


public abstract class AbstractSelectWherePartitionTypeMap {

    protected final Select.Where where;
    protected final CassandraOptions cassandraOptions;

    protected AbstractSelectWherePartitionTypeMap(Select.Where where, CassandraOptions cassandraOptions) {
        this.where = where;
        this.cassandraOptions = cassandraOptions;
    }
}

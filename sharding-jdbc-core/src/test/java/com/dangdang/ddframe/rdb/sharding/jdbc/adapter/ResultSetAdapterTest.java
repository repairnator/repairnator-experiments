/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.rdb.sharding.jdbc.adapter;

import com.dangdang.ddframe.rdb.common.base.AbstractShardingJDBCDatabaseAndTableTest;
import com.dangdang.ddframe.rdb.sharding.constant.DatabaseType;
import com.dangdang.ddframe.rdb.sharding.jdbc.core.connection.ShardingConnection;
import com.dangdang.ddframe.rdb.sharding.jdbc.core.datasource.ShardingDataSource;
import com.dangdang.ddframe.rdb.sharding.jdbc.util.JDBCTestSQL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dangdang.ddframe.rdb.sharding.constant.DatabaseType.Oracle;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class ResultSetAdapterTest extends AbstractShardingJDBCDatabaseAndTableTest {
    
    private List<ShardingConnection> shardingConnections = new ArrayList<>();
    
    private List<Statement> statements = new ArrayList<>();
    
    private Map<DatabaseType, ResultSet> resultSets = new HashMap<>();
    
    @Before
    public void init() throws SQLException {
        for (Map.Entry<DatabaseType, ShardingDataSource> each : getShardingDataSources().entrySet()) {
            ShardingConnection shardingConnection = each.getValue().getConnection();
            shardingConnections.add(shardingConnection);
            Statement statement = shardingConnection.createStatement();
            statements.add(statement);
            resultSets.put(each.getKey(), statement.executeQuery(JDBCTestSQL.SELECT_GROUP_BY_USER_ID_SQL));
        }
    }
    
    @After
    public void close() throws SQLException {
        for (ResultSet each : resultSets.values()) {
            each.close();
        }
        for (Statement each : statements) {
            each.close();
        }
        for (ShardingConnection each : shardingConnections) {
            each.close();
        }
    }
    
    @Test
    public void assertClose() throws SQLException {
        for (Map.Entry<DatabaseType, ResultSet> each : resultSets.entrySet()) {
            each.getValue().close();
            assertClose((AbstractResultSetAdapter) each.getValue(), each.getKey());
        }
    }
    
    private void assertClose(final AbstractResultSetAdapter actual, final DatabaseType type) throws SQLException {
        assertTrue(actual.isClosed());
        assertThat(actual.getResultSets().size(), is(4));
        if (Oracle != type) {
            for (ResultSet each : actual.getResultSets()) {
                assertTrue(each.isClosed());
            }
        }
    }
    
    @Test
    public void assertWasNull() throws SQLException {
        for (ResultSet each : resultSets.values()) {
            assertFalse(each.wasNull());
        }
    }
    
    @Test
    public void assertSetFetchDirection() throws SQLException {
        for (Map.Entry<DatabaseType, ResultSet> each : resultSets.entrySet()) {
            assertThat(each.getValue().getFetchDirection(), is(ResultSet.FETCH_FORWARD));
            try {
                each.getValue().setFetchDirection(ResultSet.FETCH_REVERSE);
            } catch (final SQLException ignore) {
            }
            if (each.getKey() == DatabaseType.MySQL || each.getKey() == DatabaseType.PostgreSQL) {
                assertFetchDirection((AbstractResultSetAdapter) each.getValue(), ResultSet.FETCH_REVERSE, each.getKey());
            }
        }
    }
    
    private void assertFetchDirection(final AbstractResultSetAdapter actual, final int fetchDirection, final DatabaseType type) throws SQLException {
        // H2数据库未实现getFetchDirection方法
        assertThat(actual.getFetchDirection(), is(DatabaseType.H2 == type || DatabaseType.PostgreSQL == type ? ResultSet.FETCH_FORWARD : fetchDirection));
        assertThat(actual.getResultSets().size(), is(4));
        for (ResultSet each : actual.getResultSets()) {
            assertThat(each.getFetchDirection(), is(DatabaseType.H2 == type || DatabaseType.PostgreSQL == type ? ResultSet.FETCH_FORWARD : fetchDirection));
        }
    }
    
    @Test
    public void assertSetFetchSize() throws SQLException {
        for (Map.Entry<DatabaseType, ResultSet> each : resultSets.entrySet()) {
            if (DatabaseType.MySQL == each.getKey() || DatabaseType.PostgreSQL == each.getKey()) {
                assertThat(each.getValue().getFetchSize(), is(0));
            }
            each.getValue().setFetchSize(100);
            assertFetchSize((AbstractResultSetAdapter) each.getValue(), each.getKey());
        }
    }
    
    private void assertFetchSize(final AbstractResultSetAdapter actual, final DatabaseType type) throws SQLException {
        // H2数据库未实现getFetchSize方法
        assertThat(actual.getFetchSize(), is(DatabaseType.H2 == type ? 0 : 100));
        assertThat(actual.getResultSets().size(), is(4));
        for (ResultSet each : actual.getResultSets()) {
            assertThat(each.getFetchSize(), is(DatabaseType.H2 == type ? 0 : 100));
        }
    }
    
    @Test
    public void assertGetType() throws SQLException {
        for (ResultSet each : resultSets.values()) {
            assertThat(each.getType(), is(ResultSet.TYPE_FORWARD_ONLY));
        }
    }
    
    @Test
    public void assertGetConcurrency() throws SQLException {
        for (ResultSet each : resultSets.values()) {
            assertThat(each.getConcurrency(), is(ResultSet.CONCUR_READ_ONLY));
        }
    }
    
    @Test
    public void assertGetStatement() throws SQLException {
        for (ResultSet each : resultSets.values()) {
            assertNotNull(each.getStatement());
        }
    }
    
    @Test
    public void assertClearWarnings() throws SQLException {
        for (ResultSet each : resultSets.values()) {
            assertNull(each.getWarnings());
            each.clearWarnings();
            assertNull(each.getWarnings());
        }
    }
    
    @Test
    public void assertGetMetaData() throws SQLException {
        for (ResultSet each : resultSets.values()) {
            assertNotNull(each.getMetaData());
        }
    }
    
    @Test
    public void assertFindColumn() throws SQLException {
        for (Map.Entry<DatabaseType, ResultSet> each : resultSets.entrySet()) {
            assertThat(each.getValue().findColumn("user_id"), is(1));
        }
    }
}

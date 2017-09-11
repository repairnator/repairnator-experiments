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

package com.dangdang.ddframe.rdb.sharding.rewrite;

import com.dangdang.ddframe.rdb.sharding.api.fixture.ShardingRuleMockBuilder;
import com.dangdang.ddframe.rdb.sharding.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.constant.OrderType;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.context.OrderItem;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.context.limit.Limit;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.context.limit.LimitValue;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.context.table.Table;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.sql.dql.select.SelectStatement;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.token.ItemsToken;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.token.OffsetToken;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.token.OrderByToken;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.token.RowCountToken;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.token.TableToken;
import com.dangdang.ddframe.rdb.sharding.routing.type.TableUnit;
import com.dangdang.ddframe.rdb.sharding.routing.type.complex.CartesianTableReference;
import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class SQLRewriteEngineTest {
    
    private ShardingRule shardingRule;
    
    private SelectStatement selectStatement;
    
    private Map<String, String> tableTokens;
    
    @Before
    public void setUp() {
        shardingRule = new ShardingRuleMockBuilder().addGenerateKeyColumn("table_x", "id").addBindingTable("table_y").build();
        selectStatement = new SelectStatement();
        tableTokens = new HashMap<>(1, 1);
        tableTokens.put("table_x", "table_1");
    }
    
    @Test
    public void assertRewriteWithoutChange() {
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT table_y.id FROM table_y WHERE table_y.id=?", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT table_y.id FROM table_y WHERE table_y.id=?"));
    }
    
    @Test
    public void assertRewriteForTableName() {
        selectStatement.getSqlTokens().add(new TableToken(7, "table_x"));
        selectStatement.getSqlTokens().add(new TableToken(31, "table_x"));
        selectStatement.getSqlTokens().add(new TableToken(47, "table_x"));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT table_x.id, x.name FROM table_x x WHERE table_x.id=? AND x.name=?", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT table_1.id, x.name FROM table_1 x WHERE table_1.id=? AND x.name=?"));
    }
    
    @Test
    public void assertRewriteForOrderByAndGroupByDerivedColumns() {
        selectStatement.getSqlTokens().add(new TableToken(18, "table_x"));
        ItemsToken itemsToken = new ItemsToken(12);
        itemsToken.getItems().addAll(Arrays.asList("x.id as ORDER_BY_DERIVED_0", "x.name as GROUP_BY_DERIVED_0"));
        selectStatement.getSqlTokens().add(itemsToken);
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT x.age FROM table_x x GROUP BY x.id ORDER BY x.name", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT x.age, x.id as ORDER_BY_DERIVED_0, x.name as GROUP_BY_DERIVED_0 FROM table_1 x GROUP BY x.id ORDER BY x.name"));
    }
    
    @Test
    public void assertRewriteForAggregationDerivedColumns() {
        selectStatement.getSqlTokens().add(new TableToken(23, "table_x"));
        ItemsToken itemsToken = new ItemsToken(17);
        itemsToken.getItems().addAll(Arrays.asList("COUNT(x.age) as AVG_DERIVED_COUNT_0", "SUM(x.age) as AVG_DERIVED_SUM_0"));
        selectStatement.getSqlTokens().add(itemsToken);
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT AVG(x.age) FROM table_x x", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT AVG(x.age), COUNT(x.age) as AVG_DERIVED_COUNT_0, SUM(x.age) as AVG_DERIVED_SUM_0 FROM table_1 x"));
    }
    
    @Test
    public void assertRewriteForAutoGeneratedKeyColumn() {
        selectStatement.getSqlTokens().add(new TableToken(12, "table_x"));
        ItemsToken itemsToken1 = new ItemsToken(30);
        itemsToken1.getItems().add("id");
        ItemsToken itemsToken2 = new ItemsToken(44);
        itemsToken2.getItems().add("?");
        selectStatement.getSqlTokens().addAll(Arrays.asList(itemsToken1, itemsToken2));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "INSERT INTO table_x (name, age) VALUES (?, ?)", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("INSERT INTO table_1 (name, age, id) VALUES (?, ?, ?)"));
    }
    
    @Test
    public void assertRewriteForLimit() {
        selectStatement.setLimit(new Limit(true));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(2, -1));
        selectStatement.getSqlTokens().add(new TableToken(17, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(33, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(36, 2));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT x.id FROM table_x x LIMIT 2, 2", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT x.id FROM table_1 x LIMIT 0, 4"));
    }
    
    @Test
    public void assertRewriteForRowNum() {
        selectStatement.setLimit(new Limit(false));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(4, -1));
        selectStatement.getSqlTokens().add(new TableToken(68, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(119, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(98, 4));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule,
                "SELECT * FROM (SELECT row_.*, rownum rownum_ FROM (SELECT x.id FROM table_x x) row_ WHERE rownum<=4) t WHERE t.rownum_>2", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT * FROM (SELECT row_.*, rownum rownum_ FROM (SELECT x.id FROM table_1 x) row_ WHERE rownum<=4) t WHERE t.rownum_>0"));
    }
    
    @Test
    public void assertRewriteForTopAndRowNumber() {
        selectStatement.setLimit(new Limit(false));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(4, -1));
        selectStatement.getSqlTokens().add(new TableToken(85, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(123, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(26, 4));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule,
                "SELECT * FROM (SELECT TOP(4) row_number() OVER (ORDER BY x.id) AS rownum_, x.id FROM table_x x) AS row_ WHERE row_.rownum_>2", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT * FROM (SELECT TOP(4) row_number() OVER (ORDER BY x.id) AS rownum_, x.id FROM table_1 x) AS row_ WHERE row_.rownum_>0"));
    }
    
    @Test
    public void assertRewriteForLimitForMemoryGroupBy() {
        selectStatement.setLimit(new Limit(true));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(2, -1));
        selectStatement.getOrderByItems().add(new OrderItem("x", "id", OrderType.ASC, OrderType.ASC, Optional.<String>absent()));
        selectStatement.getGroupByItems().add(new OrderItem("x", "id", OrderType.DESC, OrderType.ASC, Optional.<String>absent()));
        selectStatement.getSqlTokens().add(new TableToken(17, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(33, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(36, 2));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT x.id FROM table_x x LIMIT 2, 2", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT x.id FROM table_1 x LIMIT 0, 2147483647"));
    }
    
    @Test
    public void assertRewriteForRowNumForMemoryGroupBy() {
        selectStatement.setLimit(new Limit(false));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(4, -1));
        selectStatement.getSqlTokens().add(new TableToken(68, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(119, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(98, 4));
        selectStatement.getOrderByItems().add(new OrderItem("x", "id", OrderType.ASC, OrderType.ASC, Optional.<String>absent()));
        selectStatement.getGroupByItems().add(new OrderItem("x", "id", OrderType.DESC, OrderType.ASC, Optional.<String>absent()));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule,
                "SELECT * FROM (SELECT row_.*, rownum rownum_ FROM (SELECT x.id FROM table_x x) row_ WHERE rownum<=4) t WHERE t.rownum_>2", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), 
                is("SELECT * FROM (SELECT row_.*, rownum rownum_ FROM (SELECT x.id FROM table_1 x) row_ WHERE rownum<=2147483647) t WHERE t.rownum_>0"));
    }
    
    @Test
    public void assertRewriteForTopAndRowNumberForMemoryGroupBy() {
        selectStatement.setLimit(new Limit(false));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(4, -1));
        selectStatement.getSqlTokens().add(new TableToken(85, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(123, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(26, 4));
        selectStatement.getOrderByItems().add(new OrderItem("x", "id", OrderType.ASC, OrderType.ASC, Optional.<String>absent()));
        selectStatement.getGroupByItems().add(new OrderItem("x", "id", OrderType.DESC, OrderType.ASC, Optional.<String>absent()));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule,
                "SELECT * FROM (SELECT TOP(4) row_number() OVER (ORDER BY x.id) AS rownum_, x.id FROM table_x x) AS row_ WHERE row_.rownum_>2", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), 
                is("SELECT * FROM (SELECT TOP(2147483647) row_number() OVER (ORDER BY x.id) AS rownum_, x.id FROM table_1 x) AS row_ WHERE row_.rownum_>0"));
    }
    
    @Test
    public void assertRewriteForLimitForNotRewriteLimit() {
        selectStatement.setLimit(new Limit(true));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(2, -1));
        selectStatement.getSqlTokens().add(new TableToken(17, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(33, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(36, 2));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT x.id FROM table_x x LIMIT 2, 2", selectStatement);
        assertThat(rewriteEngine.rewrite(false).toSQL(tableTokens), is("SELECT x.id FROM table_1 x LIMIT 2, 2"));
    }
    
    @Test
    public void assertRewriteForRowNumForNotRewriteLimit() {
        selectStatement.setLimit(new Limit(false));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(4, -1));
        selectStatement.getSqlTokens().add(new TableToken(68, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(119, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(98, 4));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule,
                "SELECT * FROM (SELECT row_.*, rownum rownum_ FROM (SELECT x.id FROM table_x x) row_ WHERE rownum<=4) t WHERE t.rownum_>2", selectStatement);
        assertThat(rewriteEngine.rewrite(false).toSQL(tableTokens), is("SELECT * FROM (SELECT row_.*, rownum rownum_ FROM (SELECT x.id FROM table_1 x) row_ WHERE rownum<=4) t WHERE t.rownum_>2"));
    }
    
    @Test
    public void assertRewriteForTopAndRowNumberForNotRewriteLimit() {
        selectStatement.setLimit(new Limit(false));
        selectStatement.getLimit().setOffset(new LimitValue(2, -1));
        selectStatement.getLimit().setRowCount(new LimitValue(4, -1));
        selectStatement.getSqlTokens().add(new TableToken(85, "table_x"));
        selectStatement.getSqlTokens().add(new OffsetToken(123, 2));
        selectStatement.getSqlTokens().add(new RowCountToken(26, 4));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule,
                "SELECT * FROM (SELECT TOP(4) row_number() OVER (ORDER BY x.id) AS rownum_, x.id FROM table_x x) AS row_ WHERE row_.rownum_>2", selectStatement);
        assertThat(rewriteEngine.rewrite(false).toSQL(tableTokens), 
                is("SELECT * FROM (SELECT TOP(4) row_number() OVER (ORDER BY x.id) AS rownum_, x.id FROM table_1 x) AS row_ WHERE row_.rownum_>2"));
    }
    
    @Test
    public void assertRewriteForDerivedOrderBy() {
        selectStatement.setGroupByLastPosition(61);
        selectStatement.getOrderByItems().add(new OrderItem("x", "id", OrderType.ASC, OrderType.ASC, Optional.<String>absent()));
        selectStatement.getOrderByItems().add(new OrderItem("x", "name", OrderType.DESC, OrderType.ASC, Optional.<String>absent()));
        selectStatement.getSqlTokens().add(new TableToken(25, "table_x"));
        selectStatement.getSqlTokens().add(new OrderByToken(61));
        SQLRewriteEngine rewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT x.id, x.name FROM table_x x GROUP BY x.id, x.name DESC", selectStatement);
        assertThat(rewriteEngine.rewrite(true).toSQL(tableTokens), is("SELECT x.id, x.name FROM table_1 x GROUP BY x.id, x.name DESC ORDER BY id ASC,name DESC "));
    }
    
    @Test
    public void assertGenerateSQL() {
        selectStatement.getSqlTokens().add(new TableToken(7, "table_x"));
        selectStatement.getSqlTokens().add(new TableToken(31, "table_x"));
        selectStatement.getSqlTokens().add(new TableToken(58, "table_x"));
        selectStatement.getTables().add(new Table("table_x", Optional.of("x")));
        selectStatement.getTables().add(new Table("table_y", Optional.of("y")));
        SQLRewriteEngine sqlRewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT table_x.id, x.name FROM table_x x, table_y y WHERE table_x.id=? AND x.name=?", selectStatement);
        SQLBuilder sqlBuilder = sqlRewriteEngine.rewrite(true);
        assertThat(sqlRewriteEngine.generateSQL(new TableUnit("db0", "table_x", "table_x"), sqlBuilder), is("SELECT table_x.id, x.name FROM table_x x, table_y y WHERE table_x.id=? AND x.name=?"));
    }
    
    @Test
    public void assertGenerateSQLForCartesian() {
        selectStatement.getSqlTokens().add(new TableToken(7, "table_x"));
        selectStatement.getSqlTokens().add(new TableToken(31, "table_x"));
        selectStatement.getSqlTokens().add(new TableToken(47, "table_x"));
        SQLRewriteEngine sqlRewriteEngine = new SQLRewriteEngine(shardingRule, "SELECT table_x.id, x.name FROM table_x x WHERE table_x.id=? AND x.name=?", selectStatement);
        SQLBuilder sqlBuilder = sqlRewriteEngine.rewrite(true);
        CartesianTableReference cartesianTableReference = new CartesianTableReference(Collections.singletonList(new TableUnit("db0", "table_x", "table_x")));
        assertThat(sqlRewriteEngine.generateSQL(cartesianTableReference, sqlBuilder), is("SELECT table_x.id, x.name FROM table_x x WHERE table_x.id=? AND x.name=?"));
    }
}

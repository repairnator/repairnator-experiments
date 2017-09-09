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

package com.dangdang.ddframe.rdb.sharding.parsing.parser.dialect.mysql.sql;

import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.parsing.lexer.LexerEngine;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.dialect.mysql.clause.MySQLLimitClauseParser;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.dialect.mysql.clause.facade.MySQLSelectClauseParserFacade;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.dialect.mysql.clause.MySQLSelectOptionClauseParser;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.sql.dql.select.AbstractSelectParser;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.sql.dql.select.SelectStatement;

/**
 * Select parser for MySQL.
 *
 * @author zhangliang
 */
public final class MySQLSelectParser extends AbstractSelectParser {
    
    private final MySQLSelectOptionClauseParser selectOptionClauseParser;
    
    private final MySQLLimitClauseParser limitClauseParser;
    
    public MySQLSelectParser(final ShardingRule shardingRule, final LexerEngine lexerEngine) {
        super(shardingRule, lexerEngine, new MySQLSelectClauseParserFacade(shardingRule, lexerEngine));
        selectOptionClauseParser = new MySQLSelectOptionClauseParser(lexerEngine);
        limitClauseParser = new MySQLLimitClauseParser(lexerEngine);
    }
    
    @Override
    protected void parseInternal(final SelectStatement selectStatement) {
        parseDistinct();
        parseSelectOption();
        parseSelectList(selectStatement, getItems());
        parseFrom(selectStatement);
        parseWhere(getShardingRule(), selectStatement, getItems());
        parseGroupBy(selectStatement);
        parseHaving();
        parseOrderBy(selectStatement);
        parseLimit(selectStatement);
        parseSelectRest();
    }
    
    private void parseSelectOption() {
        selectOptionClauseParser.parse();
    }
    
    private void parseLimit(final SelectStatement selectStatement) {
        limitClauseParser.parse(selectStatement);
    }
}

/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License,Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.rdb.sharding.parsing.lexer.token;

/**
 * 默认词法关键词.
 * 
 * @author zhangliang 
 */
public enum DefaultKeyword implements Keyword {
    
    SELECT,
    DELETE,
    INSERT,
    UPDATE,
    CREATE,
    ALTER,
    DROP,
    TRUNCATE,
    REPLACE,
    DECLARE,
    GRANT,
    REVOKE,
    AS,
    DISTINCT,
    DISTINCTROW,
    MAX,
    MIN,
    SUM,
    AVG,
    COUNT,
    FROM,
    WHERE,
    ORDER,
    ASC,
    DESC,
    GROUP,
    BY,
    HAVING,
    INTO,
    VALUES,
    COLUMN,
    TABLE,
    TABLESPACE,
    SET,
    PRIMARY,
    KEY,
    INDEX,
    CONSTRAINT,
    CHECK,
    UNIQUE,
    FOREIGN,
    REFERENCES,
    INNER,
    LEFT,
    RIGHT,
    FULL,
    OUTER,
    CROSS, 
    JOIN,
    STRAIGHT_JOIN,
    APPLY,
    ON,
    IS,
    IN,
    BETWEEN,
    LIKE,
    AND,
    OR,
    XOR,
    NULL,
    NOT,
    FOR,
    ALL,
    UNION,
    CAST,
    USE,
    USING,
    TO,
    CASE,
    WHEN,
    THEN,
    ELSE,
    END,
    EXISTS,
    NEW,
    ESCAPE,
    INTERVAL,
    LOCK,
    SOME,
    ANY,
    WHILE,
    DO,
    LEAVE,
    ITERATE,
    REPEAT,
    UNTIL,
    OPEN,
    CLOSE,
    OUT,
    INOUT,
    OVER,
    FETCH,
    WITH,
    CURSOR,
    ADVISE,
    SIBLINGS,
    LOOP,
    ENABLE,
    DISABLE,
    EXPLAIN,
    SCHEMA,
    DATABASE,
    VIEW,
    SEQUENCE,
    TRIGGER,
    PROCEDURE,
    FUNCTION,
    DEFAULT,
    EXCEPT,
    INTERSECT,
    MINUS,
    USER,
    PASSWORD,
    CONNECT_BY_ROOT
}

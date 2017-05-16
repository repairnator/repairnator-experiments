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
package org.apache.calcite.adapter.jdbc;

import org.apache.calcite.adapter.enumerable.EnumerableRel;
import org.apache.calcite.adapter.enumerable.EnumerableRelImplementor;
import org.apache.calcite.adapter.enumerable.JavaRowFormat;
import org.apache.calcite.adapter.enumerable.PhysType;
import org.apache.calcite.adapter.enumerable.PhysTypeImpl;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.tree.BlockBuilder;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.linq4j.tree.Expressions;
import org.apache.calcite.linq4j.tree.ParameterExpression;
import org.apache.calcite.linq4j.tree.Primitive;
import org.apache.calcite.linq4j.tree.UnaryExpression;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.prepare.CalcitePrepareImpl;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterImpl;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.runtime.Hook;
import org.apache.calcite.runtime.SqlFunctions;
import org.apache.calcite.schema.Schemas;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.BuiltInMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Relational expression representing a scan of a table in a JDBC data source.
 */
public class JdbcToEnumerableConverter
    extends ConverterImpl
    implements EnumerableRel {
  protected JdbcToEnumerableConverter(
      RelOptCluster cluster,
      RelTraitSet traits,
      RelNode input) {
    super(cluster, ConventionTraitDef.INSTANCE, traits, input);
  }

  @Override public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
    return new JdbcToEnumerableConverter(
        getCluster(), traitSet, sole(inputs));
  }

  @Override public RelOptCost computeSelfCost(RelOptPlanner planner,
      RelMetadataQuery mq) {
    return super.computeSelfCost(planner, mq).multiplyBy(.1);
  }

  public Result implement(EnumerableRelImplementor implementor, Prefer pref) {
    // Generate:
    //   ResultSetEnumerable.of(schema.getDataSource(), "select ...")
    final BlockBuilder builder0 = new BlockBuilder(false);
    final JdbcRel child = (JdbcRel) getInput();
    final PhysType physType =
        PhysTypeImpl.of(
            implementor.getTypeFactory(), getRowType(),
            pref.prefer(JavaRowFormat.CUSTOM));
    final JdbcConvention jdbcConvention =
        (JdbcConvention) child.getConvention();
    String sql = generateSql(jdbcConvention.dialect);
    if (CalcitePrepareImpl.DEBUG) {
      System.out.println("[" + sql + "]");
    }
    Hook.QUERY_PLAN.run(sql);
    final Expression sql_ =
        builder0.append("sql", Expressions.constant(sql));
    final int fieldCount = getRowType().getFieldCount();
    BlockBuilder builder = new BlockBuilder();
    final ParameterExpression resultSet_ =
        Expressions.parameter(Modifier.FINAL, ResultSet.class,
            builder.newName("resultSet"));
    CalendarPolicy calendarPolicy = CalendarPolicy.of(jdbcConvention.dialect);
    final Expression calendar_;
    switch (calendarPolicy) {
    case LOCAL:
      calendar_ =
          builder0.append("calendar",
              Expressions.call(Calendar.class, "getInstance",
                  getTimeZoneExpression(implementor)));
      break;
    default:
      calendar_ = null;
    }
    if (fieldCount == 1) {
      final ParameterExpression value_ =
          Expressions.parameter(Object.class, builder.newName("value"));
      builder.add(Expressions.declare(Modifier.FINAL, value_, null));
      generateGet(implementor, physType, builder, resultSet_, 0, value_,
          calendar_, calendarPolicy);
      builder.add(Expressions.return_(null, value_));
    } else {
      final Expression values_ =
          builder.append("values",
              Expressions.newArrayBounds(Object.class, 1,
                  Expressions.constant(fieldCount)));
      for (int i = 0; i < fieldCount; i++) {
        generateGet(implementor, physType, builder, resultSet_, i,
            Expressions.arrayIndex(values_, Expressions.constant(i)),
            calendar_, calendarPolicy);
      }
      builder.add(
          Expressions.return_(null, values_));
    }
    final ParameterExpression e_ =
        Expressions.parameter(SQLException.class, builder.newName("e"));
    final Expression rowBuilderFactory_ =
        builder0.append("rowBuilderFactory",
            Expressions.lambda(
                Expressions.block(
                    Expressions.return_(null,
                        Expressions.lambda(
                            Expressions.block(
                                Expressions.tryCatch(
                                    builder.toBlock(),
                                    Expressions.catch_(
                                        e_,
                                        Expressions.throw_(
                                            Expressions.new_(
                                                RuntimeException.class,
                                                e_)))))))),
                resultSet_));
    final Expression enumerable =
        builder0.append(
            "enumerable",
            Expressions.call(
                BuiltInMethod.RESULT_SET_ENUMERABLE_OF.method,
                Expressions.call(
                    Schemas.unwrap(jdbcConvention.expression,
                        JdbcSchema.class),
                    BuiltInMethod.JDBC_SCHEMA_DATA_SOURCE.method),
                sql_,
                rowBuilderFactory_));
    builder0.add(
        Expressions.return_(null, enumerable));
    return implementor.result(physType, builder0.toBlock());
  }

  private UnaryExpression getTimeZoneExpression(
      EnumerableRelImplementor implementor) {
    return Expressions.convert_(
        Expressions.call(
            implementor.getRootExpression(),
            "get",
            Expressions.constant("timeZone")),
        TimeZone.class);
  }

  private void generateGet(EnumerableRelImplementor implementor,
      PhysType physType, BlockBuilder builder, ParameterExpression resultSet_,
      int i, Expression target, Expression calendar_,
      CalendarPolicy calendarPolicy) {
    final Primitive primitive = Primitive.ofBoxOr(physType.fieldClass(i));
    final RelDataType fieldType =
        physType.getRowType().getFieldList().get(i).getType();
    final List<Expression> dateTimeArgs = new ArrayList<Expression>();
    dateTimeArgs.add(Expressions.constant(i + 1));
    SqlTypeName sqlTypeName = fieldType.getSqlTypeName();
    boolean offset = false;
    switch (calendarPolicy) {
    case LOCAL:
      dateTimeArgs.add(calendar_);
      break;
    case NULL:
      // We don't specify a calendar at all, so we don't add an argument and
      // instead use the version of the getXXX that doesn't take a Calendar
      break;
    case DIRECT:
      sqlTypeName = SqlTypeName.ANY;
      break;
    case SHIFT:
      switch (sqlTypeName) {
      case TIMESTAMP:
      case DATE:
        offset = true;
      }
      break;
    }
    final Expression source;
    switch (sqlTypeName) {
    case DATE:
    case TIME:
    case TIMESTAMP:
      source = Expressions.call(
          getMethod(sqlTypeName, fieldType.isNullable(), offset),
          Expressions.<Expression>list()
              .append(
                  Expressions.call(resultSet_,
                      getMethod2(sqlTypeName), dateTimeArgs))
          .appendIf(offset, getTimeZoneExpression(implementor)));
      break;
    case ARRAY:
      final Expression x = Expressions.convert_(
          Expressions.call(resultSet_, jdbcGetMethod(primitive),
              Expressions.constant(i + 1)),
          java.sql.Array.class);
      source = Expressions.call(BuiltInMethod.JDBC_ARRAY_TO_LIST.method, x);
      break;
    default:
      source = Expressions.call(
          resultSet_, jdbcGetMethod(primitive), Expressions.constant(i + 1));
    }
    builder.add(
        Expressions.statement(
            Expressions.assign(
                target, source)));

    // [CALCITE-596] If primitive type columns contain null value, returns null
    // object
    if (primitive != null) {
      builder.add(
          Expressions.ifThen(
              Expressions.call(resultSet_, "wasNull"),
              Expressions.statement(
                  Expressions.assign(target,
                      Expressions.constant(null)))));
    }
  }

  private Method getMethod(SqlTypeName sqlTypeName, boolean nullable,
      boolean offset) {
    switch (sqlTypeName) {
    case DATE:
      return (nullable
          ? BuiltInMethod.DATE_TO_INT_OPTIONAL
          : BuiltInMethod.DATE_TO_INT).method;
    case TIME:
      return (nullable
          ? BuiltInMethod.TIME_TO_INT_OPTIONAL
          : BuiltInMethod.TIME_TO_INT).method;
    case TIMESTAMP:
      return (nullable
          ? (offset
          ? BuiltInMethod.TIMESTAMP_TO_LONG_OPTIONAL_OFFSET
          : BuiltInMethod.TIMESTAMP_TO_LONG_OPTIONAL)
          : (offset
              ? BuiltInMethod.TIMESTAMP_TO_LONG_OFFSET
              : BuiltInMethod.TIMESTAMP_TO_LONG)).method;
    default:
      throw new AssertionError(sqlTypeName + ":" + nullable);
    }
  }

  private Method getMethod2(SqlTypeName sqlTypeName) {
    switch (sqlTypeName) {
    case DATE:
      return BuiltInMethod.RESULT_SET_GET_DATE2.method;
    case TIME:
      return BuiltInMethod.RESULT_SET_GET_TIME2.method;
    case TIMESTAMP:
      return BuiltInMethod.RESULT_SET_GET_TIMESTAMP2.method;
    default:
      throw new AssertionError(sqlTypeName);
    }
  }

  /** E,g, {@code jdbcGetMethod(int)} returns "getInt". */
  private String jdbcGetMethod(Primitive primitive) {
    return primitive == null
        ? "getObject"
        : "get" + SqlFunctions.initcap(primitive.primitiveName);
  }

  private String generateSql(SqlDialect dialect) {
    final JdbcImplementor jdbcImplementor =
        new JdbcImplementor(dialect,
            (JavaTypeFactory) getCluster().getTypeFactory());
    final JdbcImplementor.Result result =
        jdbcImplementor.visitChild(0, getInput());
    return result.asStatement().toSqlString(dialect).getSql();
  }

  /** Whether this JDBC driver needs you to pass a Calendar object to methods
   * such as {@link ResultSet#getTimestamp(int, java.util.Calendar)}. */
  private enum CalendarPolicy {
    NONE,
    NULL,
    LOCAL,
    DIRECT,
    SHIFT;

    static CalendarPolicy of(SqlDialect dialect) {
      switch (dialect.getDatabaseProduct()) {
      case MYSQL:
        return SHIFT;
      case HSQLDB:
      default:
        // NULL works for hsqldb-2.3; nothing worked for hsqldb-1.8.
        return NULL;
      }
    }
  }
}

// End JdbcToEnumerableConverter.java

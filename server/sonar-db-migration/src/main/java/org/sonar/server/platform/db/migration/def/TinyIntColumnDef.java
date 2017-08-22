/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.def;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;
import org.sonar.db.dialect.Dialect;
import org.sonar.db.dialect.H2;
import org.sonar.db.dialect.MsSql;
import org.sonar.db.dialect.MySql;
import org.sonar.db.dialect.Oracle;
import org.sonar.db.dialect.PostgreSql;

import static org.sonar.server.platform.db.migration.def.Validations.validateColumnName;

/**
 * Integer that supports at least range [0..128]. Full range depends on database vendor.
 */
@Immutable
public class TinyIntColumnDef extends AbstractColumnDef {

  private TinyIntColumnDef(Builder builder) {
    super(builder.columnName, builder.isNullable, null);
  }

  @Override
  public String generateSqlType(Dialect dialect) {
    switch (dialect.getId()) {
      case PostgreSql.ID:
        return "SMALLINT";
      case Oracle.ID:
        return "NUMBER(3)";
      case MySql.ID:
        // do not use TINYINT(1) as it's considered as booleans by connector/J.
        return "TINYINT(2)";
      case MsSql.ID:
      case H2.ID:
        return "TINYINT";
      default:
        throw new UnsupportedOperationException(String.format("Unknown dialect '%s'", dialect.getId()));
    }
  }

  public static class Builder {
    @CheckForNull
    private String columnName;
    private boolean isNullable = true;

    public Builder setColumnName(String columnName) {
      this.columnName = validateColumnName(columnName);
      return this;
    }

    public Builder setIsNullable(boolean isNullable) {
      this.isNullable = isNullable;
      return this;
    }

    public TinyIntColumnDef build() {
      validateColumnName(columnName);
      return new TinyIntColumnDef(this);
    }
  }

}

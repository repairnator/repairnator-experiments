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
package org.sonar.api.rules;

import com.google.common.base.Function;
import java.util.Arrays;
import java.util.Set;
import javax.annotation.Nonnull;

import static com.google.common.collect.FluentIterable.from;
import static java.lang.String.format;

public enum RuleType {
  CODE_SMELL(1), BUG(2), VULNERABILITY(3);

  private static final Set<String> ALL_NAMES = from(Arrays.asList(values())).transform(ToName.INSTANCE).toSet();

  private final int dbConstant;

  RuleType(int dbConstant) {
    this.dbConstant = dbConstant;
  }

  public int getDbConstant() {
    return dbConstant;
  }

  public static Set<String> names() {
    return ALL_NAMES;
  }

  /**
   * Returns the enum constant of the specified DB column value.
   */
  public static RuleType valueOf(int dbConstant) {
    // iterating the array is fast-enough as size is small. No need for a map.
    for (RuleType type : values()) {
      if (type.getDbConstant() == dbConstant) {
        return type;
      }
    }
    throw new IllegalArgumentException(format("Unsupported type value : %d", dbConstant));
  }

  private enum ToName implements Function<RuleType, String> {
    INSTANCE;

    @Override
    public String apply(@Nonnull RuleType input) {
      return input.name();
    }
  }

}

/*
 * SonarQube PHP Plugin
 * Copyright (C) 2010-2018 SonarSource SA
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
package org.sonar.php.checks;

import org.sonar.check.Rule;
import org.sonar.php.checks.utils.CheckUtils;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = ExitOrDieUsageCheck.KEY)
public class ExitOrDieUsageCheck extends PHPVisitorCheck {

  public static final String KEY = "S1799";
  public static final String MESSAGE = "Remove this \"%s()\" call or ensure it is really required";

  @Override
  public void visitFunctionCall(FunctionCallTree tree) {
    if (CheckUtils.isExitExpression(tree)) {
      String callee = tree.callee().toString();
      context().newIssue(this, tree.callee(), String.format(MESSAGE, callee));
    }

    super.visitFunctionCall(tree);
  }

}

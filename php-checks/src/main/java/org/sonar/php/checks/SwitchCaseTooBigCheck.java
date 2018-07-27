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
import org.sonar.check.RuleProperty;
import org.sonar.php.metrics.LineVisitor;
import org.sonar.plugins.php.api.tree.statement.SwitchCaseClauseTree;
import org.sonar.plugins.php.api.tree.statement.SwitchStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = SwitchCaseTooBigCheck.KEY)
public class SwitchCaseTooBigCheck extends PHPVisitorCheck {

  public static final String KEY = "S1151";

  private static final String MESSAGE = "Reduce this \"switch/case\" number of lines from %s to at most %s, for example by extracting code into function.";

  public static final int DEFAULT = 10;

  @RuleProperty(
    key = "max",
    description = "Maximum number of lines of code",
    defaultValue = "" + DEFAULT)
  int max = DEFAULT;

  @Override
  public void visitSwitchStatement(SwitchStatementTree tree) {
    tree.cases().forEach(this::checkCaseClause);
    super.visitSwitchStatement(tree);
  }

  private void checkCaseClause(SwitchCaseClauseTree clause) {
    int lines = LineVisitor.linesOfCode(clause);
    if (lines > max) {
      context().newIssue(this, clause.caseToken(), String.format(MESSAGE, lines, max));
    }
  }
}

/*
 * Copyright 2011 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.javascript.jscomp;


/**
 * {@link CheckDebuggerStatementTest} is a unit test for
 * {@link CheckDebuggerStatement}.
 *
 * @author bolinfest@google.com (Michael Bolin)
 */
public final class CheckDebuggerStatementTest extends CompilerTestCase {

  private CheckLevel checkLevel;

  @Override
  public void tearDown() {
    checkLevel = null;
  }

  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return new CheckDebuggerStatement(compiler);
  }

  @Override
  protected CompilerOptions getOptions() {
    CompilerOptions options = super.getOptions();
    if (checkLevel != null) {
      options.setWarningLevel(
          DiagnosticGroups.DEBUGGER_STATEMENT_PRESENT,
          checkLevel);
    }
    return options;
  }

  public void testCheckDebuggerStatement() {
    checkLevel = CheckLevel.WARNING;

    testWarning("debugger;", CheckDebuggerStatement.DEBUGGER_STATEMENT_PRESENT);
    testWarning(
        "function foo() { debugger; }", CheckDebuggerStatement.DEBUGGER_STATEMENT_PRESENT);
  }

  public void testCheckIsDisabledByDefault() {
    checkLevel = null;

    testSame("debugger;");
    testSame("function foo() { debugger; }");
  }

  public void testNoWarningWhenExplicitlyDisabled() {
    checkLevel = CheckLevel.OFF;

    testSame("debugger;");
    testSame("function foo() { debugger; }");
  }

  public void testCheckDebuggerKeywordMayAppearInComments() {
    checkLevel = CheckLevel.WARNING;

    test("// I like the debugger; it is helpful.", "");
  }

  public void testCheckDebuggerStatementInEval() {
    checkLevel = CheckLevel.WARNING;

    testSame("eval('debugger');");
  }

}

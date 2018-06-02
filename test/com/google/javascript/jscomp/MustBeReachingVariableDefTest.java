/*
 * Copyright 2008 The Closure Compiler Authors.
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

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import junit.framework.TestCase;

/**
 * Tests for {@link MustBeReachingVariableDef}.
 *
 */
public final class MustBeReachingVariableDefTest extends TestCase {

  private MustBeReachingVariableDef defUse = null;
  private Node def = null;
  private Node use = null;

  public static final String EXTERNS = "var goog = {}";

  public void testStraightLine() {
    assertMatch("D:var x=1; U: x");
    assertMatch("var x; D:x=1; U: x");
    assertNotMatch("D:var x=1; x = 2; U: x");
    assertMatch("var x=1; D:x=2; U: x");
    assertNotMatch("U:x; D:var x = 1");
    assertNotMatch("D:var x; U:x; x=1");
    assertNotMatch("D:var x; U:x; x=1; x");
    assertMatch("D: var x = 1; var y = 2; y; U:x");

    assertMatch("D:let x=1; U: x");
    assertMatch("let x; D:x=1; U: x");

    assertMatch("D: const x = 1; U: x");
  }

  public void testIf() {
    assertNotMatch("var x; if(a){ D:x=1 } else { x=2 }; U:x");
    assertNotMatch("var x; if(a){ x=1 } else { D:x=2 }; U:x");
    assertMatch("D:var x=1; if(a){ U:x } else { x };");
    assertMatch("D:var x=1; if(a){ x } else { U:x };");
    assertNotMatch("var x; if(a) { D: x = 1 }; U:x;");
  }

  public void testLoops() {
    assertNotMatch("var x=0; while(a){ D:x=1 }; U:x");
    assertNotMatch("var x=0; for(;;) { D:x=1 }; U:x");
    assertMatch("D:var x=1; while(a) { U:x }");
    assertMatch("D:var x=1; for(;;)  { U:x }");
  }

  public void testConditional() {
    assertMatch("var x=0,y; D:(x=1)&&y; U:x");
    assertNotMatch("var x=0,y; D:y&&(x=1); U:x");
  }

  public void testUseAndDefInSameInstruction() {
    assertMatch("D:var x=0; U:x=1,x");
    assertMatch("D:var x=0; U:x,x=1");
  }

  public void testAssignmentInExpressions() {
    assertMatch("var x=0; D:foo(bar(x=1)); U:x");
    assertMatch("var x=0; D:foo(bar + (x = 1)); U:x");
  }

  public void testHook() {
    assertNotMatch("var x=0; D:foo() ? x=1 : bar(); U:x");
    assertNotMatch("var x=0; D:foo() ? x=1 : x=2; U:x");
  }

  public void testExpressionVariableReassignment() {
    assertMatch("var a,b; D: var x = a + b; U:x");
    assertNotMatch("var a,b,c; D: var x = a + b; a = 1; U:x");
    assertNotMatch("var a,b,c; D: var x = a + b; f(b = 1); U:x");
    assertMatch("var a,b,c; D: var x = a + b; c = 1; U:x");

    // Even if the sub-expression is change conditionally
    assertNotMatch("var a,b,c; D: var x = a + b; c ? a = 1 : 0; U:x");
  }

  public void testMergeDefinitions() {
    assertNotMatch("var x,y; D: y = x + x; if(x) { x = 1 }; U:y");
  }

  public void testMergesWithOneDefinition() {
    assertNotMatch(
        "var x,y; while(y) { if (y) { print(x) } else { D: x = 1 } } U:x");
  }

  public void testRedefinitionUsingItself() {
    assertMatch("var x = 1; D: x = x + 1; U:x;");
    assertNotMatch("var x = 1; D: x = x + 1; x = 1; U:x;");
  }

  public void testMultipleDefinitionsWithDependence() {
    assertMatch("var x, a, b; D: x = a, x = b; U: x");
    assertMatch("var x, a, b; D: x = a, x = b; a = 1; U: x");
    assertNotMatch("var x, a, b; D: x = a, x = b; b = 1; U: x");
  }

  public void testExterns() {
    assertNotMatch("D: goog = {}; U: goog");
  }

  public void testAssignmentOp() {
    assertMatch("var x = 0; D: x += 1; U: x");
    assertMatch("var x = 0; D: x *= 1; U: x");
    assertNotMatch("D: var x = 0; x += 1; U: x");
  }

  public void testIncAndDec() {
    assertMatch("var x; D: x++; U: x");
    assertMatch("var x; D: x--; U: x");
  }

  public void testFunctionParams1() {
    computeDefUse("if (param2) { D: param1 = 1; U: param1 }");
    assertSame(def, defUse.getDefNode("param1", use));
  }

  public void testFunctionParams2() {
    computeDefUse("if (param2) { D: param1 = 1} U: param1");
    assertNotSame(def, defUse.getDefNode("param1", use));
  }

  public void testArgumentsObjectModifications() {
    computeDefUse("D: param1 = 1; arguments[0] = 2; U: param1");
    assertNotSame(def, defUse.getDefNode("param1", use));
  }

  public void testArgumentsObjectEscaped() {
    computeDefUse("D: param1 = 1; var x = arguments; x[0] = 2; U: param1");
    assertNotSame(def, defUse.getDefNode("param1", use));
  }

  public void testArgumentsObjectEscapedDependents() {
    assertNotMatch("param1=1; var x; D:x=param1; var y=arguments; U:x");
  }

  public void testSideEffects() {
    assertNotMatch("var a = 1; D: var x = a; a++; U: print(x);");
    // FlowSensitiveInlineVariables needs to handle this case, where a subexpression in the same CFG
    // node as the read of x makes it unsafe to inline "x = a" into print(a++, a);
    assertMatch("var a = 1; D: var x = a; U: print(a++, x);");
  }

  public void testDestructuringDefinitions() {
    assertMatch("D: var [x] = [1]; U: x;");
    assertMatch("D: var x = [1]; U: var [y] = [x];");
    assertMatch("var x = 1; D: [x] = [2]; U: x;");

    assertNotMatch("D: var x = 1; [x] = [2]; U: x;");
    assertNotMatch("var y = 1; D: var x = y; [y] = [2]; U: x;");

    assertMatch("var x; var y; D: [x] = [x = y]; y = 1; U: x;");
    assertMatch("var x; D: var [y] = [x = 1]; U: x;");
  }

  public void testDestructuringDefaultValue() {
    // conditional definitions of x do not match the usage of x.
    assertNotMatch("var x; D: var [y = x = 3] = []; U: x;");
    assertNotMatch("var x, obj = {}; D: [obj[x = 1] = x = 2] = []; U: x;");

    assertMatch("var x, obj = {}; D: [[obj[x = 1]] = [x = 2]] = []; U: x;");
    assertMatch("var x; D: [x = x = 3] = []; U: x");
  }

  /**
   * Asserts that the use of x at U: is the definition of x at D:.
   */
  private void assertMatch(String src) {
    computeDefUse(src);
    assertSame(def, defUse.getDefNode("x", use));
  }

  /**
   * Asserts that the use of x at U: is not the definition of x at D:.
   */
  private void assertNotMatch(String src) {
    computeDefUse(src);
    assertNotSame(def, defUse.getDefNode("x", use));
  }

  /**
   * Computes reaching definition on given source.
   */
  private void computeDefUse(String src) {
    Compiler compiler = new Compiler();
    compiler.setLifeCycleStage(LifeCycleStage.NORMALIZED);
    CompilerOptions options = new CompilerOptions();
    options.setCodingConvention(new GoogleCodingConvention());
    compiler.init(ImmutableList.<SourceFile>of(), ImmutableList.<SourceFile>of(), options);
    compiler.getOptions().setLanguageIn(LanguageMode.ECMASCRIPT_2017);
    compiler.getOptions().setLanguageOut(LanguageMode.ECMASCRIPT_2017);
    Es6SyntacticScopeCreator scopeCreator = new Es6SyntacticScopeCreator(compiler);
    src = "function _FUNCTION(param1, param2){" + src + "}";
    Node script = compiler.parseTestCode(src);
    Node root = script.getFirstChild();
    Node functionBlock = root.getLastChild();
    assertEquals(0, compiler.getErrorCount());
    Scope globalScope = scopeCreator.createScope(script, null);
    Scope functionScope = scopeCreator.createScope(root, globalScope);
    Scope funcBlockScope = scopeCreator.createScope(functionBlock, functionScope);
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, true);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    defUse = new MustBeReachingVariableDef(cfg, funcBlockScope, compiler, scopeCreator);
    defUse.analyze();
    def = null;
    use = null;
    NodeTraversal.traverse(compiler, root, new LabelFinder());
    assertNotNull("Code should have an instruction labeled D", def);
    assertNotNull("Code should have an instruction labeled U", use);
  }

  /**
   * Finds the D: and U: label and store which node they point to.
   */
  private class LabelFinder extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isLabel()) {
        if (n.getFirstChild().getString().equals("D")) {
          def = n.getLastChild();
        } else if (n.getFirstChild().getString().equals("U")) {
          use = n.getLastChild();
        }
      }
    }
  }
}

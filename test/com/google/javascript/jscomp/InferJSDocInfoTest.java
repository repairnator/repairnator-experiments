/*
 * Copyright 2009 The Closure Compiler Authors.
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

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static com.google.javascript.jscomp.parsing.Config.JsDocParsing.INCLUDE_DESCRIPTIONS_NO_WHITESPACE;

import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSDocInfo.Visibility;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Tests for {@link InferJSDocInfo}.
 * @author nicksantos@google.com (Nick Santos)
 */
// TODO(nicksantos): A lot of this code is duplicated from
// TypedScopeCreatorTest. We should create a common test harness for
// assertions about type information.
public final class InferJSDocInfoTest extends CompilerTestCase {

  private static final String OBJECT_EXTERNS = ""
        + "/**\n"
        + " * Object.\n"
        + " * @param {*=} x\n"
        + " * @return {!Object}\n"
        + " * @constructor\n"
        + " */\n"
        + "function Object(x) {};";

  private TypedScope globalScope;

  @Override
  protected CompilerOptions getOptions() {
    CompilerOptions options = super.getOptions();
    options.setParseJsDocDocumentation(INCLUDE_DESCRIPTIONS_NO_WHITESPACE);
    options.setLanguageIn(LanguageMode.ECMASCRIPT_NEXT);
    options.setLanguageOut(LanguageMode.ECMASCRIPT_NEXT);
    return options;
  }

  private final Callback callback = new AbstractPostOrderCallback() {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      TypedScope s = t.getTypedScope();
      if (s.isGlobal()) {
        globalScope = s;
      }
    }
  };

  @Override
  protected CompilerPass getProcessor(final Compiler compiler) {
    return new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        TypedScopeCreator scopeCreator = new TypedScopeCreator(compiler);
        TypedScope topScope = scopeCreator.createScope(root.getParent(), null);
        (new TypeInferencePass(
            compiler, compiler.getReverseAbstractInterpreter(),
            topScope, scopeCreator)).process(externs, root);
        NodeTraversal t = new NodeTraversal(
            compiler, callback, scopeCreator);
        t.traverseRoots(externs, root);
        (new InferJSDocInfo(compiler)).process(externs, root);
      }
    };
  }

  public void testNativeCtor() {
    testSame(
        externs(OBJECT_EXTERNS),
        srcs("var x = new Object();" + "/** Another object. */ var y = new Object();"));
    assertEquals(
        "Object.",
        findGlobalNameType("x").getJSDocInfo().getBlockDescription());
    assertEquals(
        "Object.",
        findGlobalNameType("y").getJSDocInfo().getBlockDescription());
    assertEquals(
        "Object.",
        globalScope.getVar("y").getType().getJSDocInfo().getBlockDescription());
  }

  public void testStructuralFunctions() {
    testSame(
        externs(OBJECT_EXTERNS),
        srcs(
            "/** Function. \n * @param {*} x */ "
                + "function fn(x) {};"
                + "var goog = {};"
                + "/** Another object. \n * @type {Object} */ goog.x = new Object();"
                + "/** Another function. \n * @param {number} x */ goog.y = fn;"));
    assertEquals(
        "(Object|null)",
        globalScope.getVar("goog.x").getType().toString());
    assertEquals(
        "Object.",
        globalScope.getVar("goog.x").getType().restrictByNotNullOrUndefined()
        .getJSDocInfo().getBlockDescription());
    assertEquals(
        "Another function.",
        globalScope.getVar("goog.y").getType()
        .getJSDocInfo().getBlockDescription());
  }

  public void testInstanceObject() {
    // Asserts that no property gets attached to the instance object.
    testSame(
        "/** @constructor */ function Foo() {}" +
        "var f = new Foo();" +
        "/** @type {number} */ f.bar = 4;");
    ObjectType type = (ObjectType) globalScope.getVar("f").getType();
    assertEquals("Foo", type.toString());
    assertFalse(type.hasProperty("bar"));
    assertNull(type.getOwnPropertyJSDocInfo("bar"));
  }

  public void testInterface() {
    testInterface("var");
    testInterface("let");
    testInterface("const");
  }

  private void testInterface(String varOrLetOrConst) {
    testSame(
        lines(
            "/**", // preserve newlines
            " * An interface.",
            " * @interface",
            " */",
            varOrLetOrConst + " Foo = function Foo() {}",
            ""));
    assertThat(getGlobalVarTypeJSDocInfoBlockDescription("Foo")).isEqualTo("An interface.");
  }

  private String getGlobalVarTypeJSDocInfoBlockDescription(String varName) {
    TypedVar var = globalScope.getVar(varName);
    assertWithMessage("%s is not defined", varName).that(var).isNotNull();
    ObjectType type = (ObjectType) var.getType();
    assertWithMessage("%s has no type", varName).that(type).isNotNull();
    JSDocInfo jsDocInfo = type.getJSDocInfo();
    assertWithMessage("%s type has no JSDocInfo", varName).that(jsDocInfo).isNotNull();
    return jsDocInfo.getBlockDescription();
  }

  public void testNamespacedCtor() {
    testSame(
        "var goog = {};" +
        "/** Hello! \n * @constructor */ goog.Foo = function() {};" +
        "goog.Foo.bar = goog.Foo;" +
        "/** Bye! \n * @param {string=} opt_x */" +
        "goog.Foo.prototype.baz = goog.Foo;" +
        "/** Blargh */ var x = new goog.Foo();");
    assertEquals(
        "Hello!",
        findGlobalNameType("x").getJSDocInfo().getBlockDescription());
    assertEquals(
        "Hello!",
        findGlobalNameType("goog.Foo").getJSDocInfo().getBlockDescription());
    assertEquals(
        "Hello!",
        findGlobalNameType(
            "goog.Foo.bar").getJSDocInfo().getBlockDescription());

    assertEquals(
        "Hello!",
        findGlobalNameType(
            "goog.Foo.prototype.baz").getJSDocInfo().getBlockDescription());

    ObjectType proto = (ObjectType) findGlobalNameType("goog.Foo.prototype");
    assertEquals(
        "Bye!",
        proto.getPropertyType("baz").getJSDocInfo().getBlockDescription());
  }

  public void testAbstractMethod() {
    testSame(
        "/** Abstract method. \n * @type {!Function} */ var abstractMethod;" +
        "/** @constructor */ function Foo() {}" +
        "/** Block description. \n * @param {number} x */" +
        "Foo.prototype.bar = abstractMethod;");
    FunctionType abstractMethod =
        (FunctionType) findGlobalNameType("abstractMethod");
    assertNull(abstractMethod.getJSDocInfo());

    FunctionType ctor = (FunctionType) findGlobalNameType("Foo");
    ObjectType proto = ctor.getInstanceType().getImplicitPrototype();
    FunctionType method = (FunctionType) proto.getPropertyType("bar");
    assertEquals(
        "Block description.",
        method.getJSDocInfo().getBlockDescription());
    assertEquals(
        "Block description.",
        proto.getOwnPropertyJSDocInfo("bar").getBlockDescription());
  }

  public void testPrototypeObjectLiteral() {
    testSame(
        lines(
            "/** @constructor */ function Foo() {}",
            "Foo.prototype = {",
            "  /** @protected */ a: function() {},",
            "  /** @protected */ get b() {},",
            "  /** @protected */ set c(x) {},",
            "  /** @protected */ d() {}",
            "};"));

    FunctionType ctor = findGlobalNameType("Foo").toMaybeFunctionType();
    ObjectType prototype = ctor.getInstanceType().getImplicitPrototype();

    assertThat(prototype.getOwnPropertyJSDocInfo("a").getVisibility())
        .isEqualTo(Visibility.PROTECTED);
    assertThat(prototype.getOwnPropertyJSDocInfo("b").getVisibility())
        .isEqualTo(Visibility.PROTECTED);
    assertThat(prototype.getOwnPropertyJSDocInfo("c").getVisibility())
        .isEqualTo(Visibility.PROTECTED);
    assertThat(prototype.getOwnPropertyJSDocInfo("d").getVisibility())
        .isEqualTo(Visibility.PROTECTED);
  }

  private JSType findGlobalNameType(String name) {
    return findNameType(name, globalScope);
  }

  private JSType findNameType(String name, TypedScope scope) {
    Node root = scope.getRootNode();
    Deque<Node> queue = new ArrayDeque<>();
    queue.push(root);
    while (!queue.isEmpty()) {
      Node current = queue.pop();
      if (current.matchesQualifiedName(name) && current.getJSType() != null) {
        return current.getJSType();
      }

      for (Node child : current.children()) {
        queue.push(child);
      }
    }
    return null;
  }
}

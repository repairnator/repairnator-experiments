/*
 * Copyright 2016 The Closure Compiler Authors.
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

/**
 * @fileoverview
 * Tests mocking of new ES6 classes.
 *
 * @author mattloring@google.com (Matthew Loring)
 */
goog.require('goog.testing.jsunit');

var DMock = class {
  g() {
    return this.f();
  }
  f() {
    return 'super';
  }
};
var CMock = class extends DMock {
  f() {
    return 'sub';
  }
};
DMock = function() {};
var initialCInstance = new CMock();
CMock = function() {};

function testReassignedClassNames() {
  assertEquals('sub', initialCInstance.g());
}

var A = class {
  f() {
    return 1729;
  }
};
var C = class extends A {
  f() {
    return super.f() + 1;
  }
};

var old = new C();
C = class extends A {
  f() {
    return super.f() + 2;
  }
};
var n = new C();

function testRenameClass() {
  assertEquals(1730, old.f());
  assertEquals(1731, n.f());
}

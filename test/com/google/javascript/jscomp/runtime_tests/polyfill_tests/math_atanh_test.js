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

goog.module('jscomp.runtime_tests.polyfill_tests.math_atanh_test');
goog.setTestOnly();

const testSuite = goog.require('goog.testing.testSuite');
const testing = goog.require('jscomp.runtime_tests.polyfill_tests.testing');

const assertExactlyNaN = testing.assertExactlyNaN;
const assertPositiveZero = testing.assertPositiveZero;
const assertNegativeZero = testing.assertNegativeZero;
const noCheck = testing.noCheck;

testSuite({
  testAtanh() {
    assertPositiveZero(Math.atanh(0));
    assertPositiveZero(Math.atanh(noCheck(null)));
    assertNegativeZero(Math.atanh(-0));
    assertExactlyNaN(Math.atanh(noCheck('foo')));
    assertEquals(Infinity, Math.atanh(1));
    assertEquals(-Infinity, Math.atanh(-1));
  },
});

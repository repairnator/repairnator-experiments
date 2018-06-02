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

package com.google.javascript.jscomp.parsing.parser;

import com.google.javascript.jscomp.parsing.parser.util.SourceRange;

/**
 * A token representing a javascript literal. Includes string, regexp, and number literals.
 * Boolean and null literals are represented as regular keyword tokens.
 *
 * The value just includes the raw lexeme. For string literals it includes the beginning and ending
 * delimiters.
 *
 * TODO: Regexp literals should have their own token type.
 * TODO: A way to get the processed value, rather than the raw value.
 */
public class LiteralToken extends Token {

  public final String value;

  public LiteralToken(TokenType type, String value, SourceRange location) {
    super(type, location);
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}

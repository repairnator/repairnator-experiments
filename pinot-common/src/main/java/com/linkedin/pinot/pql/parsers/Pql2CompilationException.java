/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.pql.parsers;

import org.antlr.v4.runtime.RecognitionException;


/**
 * Exceptions that occur while compiling PQL.
 */
public class Pql2CompilationException extends RuntimeException {
  public Pql2CompilationException(String message) {
    super(message);
  }

  public Pql2CompilationException(String msg, Object offendingSymbol, int line, int charPositionInLine,
      RecognitionException e) {
    super(line + ":" + charPositionInLine + ": '" + offendingSymbol + "' " + msg, e);
  }
}

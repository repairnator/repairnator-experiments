/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.interpreter;

import org.apache.calcite.rel.SingleRel;

/**
 * An interpreter that takes expects one incoming source relational expression.
 *
 * @param <T> Type of relational expression
 */
abstract class AbstractSingleNode<T extends SingleRel> implements Node {
  protected final Source source;
  protected final Sink sink;
  protected final T rel;

  public AbstractSingleNode(Interpreter interpreter, T rel) {
    this.rel = rel;
    this.source = interpreter.source(rel, 0);
    this.sink = interpreter.sink(rel);
  }
}

// End AbstractSingleNode.java

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

import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.core.Join;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.core.Union;
import org.apache.calcite.rel.core.Values;
import org.apache.calcite.rel.core.Window;
import org.apache.calcite.rex.RexNode;

import com.google.common.collect.ImmutableList;

/**
 * Helper methods for {@link Node} and implementations for core relational
 * expressions.
 */
public class Nodes {
  /** Extension to
   * {@link org.apache.calcite.interpreter.Interpreter.Compiler}
   * that knows how to handle the core logical
   * {@link org.apache.calcite.rel.RelNode}s. */
  public static class CoreCompiler extends Interpreter.Compiler {
    CoreCompiler(Interpreter interpreter) {
      super(interpreter);
    }

    public void visit(Aggregate agg) {
      node = new AggregateNode(interpreter, agg);
    }

    public void visit(Filter filter) {
      node = new FilterNode(interpreter, filter);
    }

    public void visit(Project project) {
      node = new ProjectNode(interpreter, project);
    }

    public void visit(Values value) {
      node = new ValuesNode(interpreter, value);
    }

    public void visit(TableScan scan) {
      node = TableScanNode.create(interpreter, scan,
          ImmutableList.<RexNode>of(), null);
    }

    public void visit(Bindables.BindableTableScan scan) {
      node = TableScanNode.create(interpreter, scan, scan.filters,
          scan.projects);
    }

    public void visit(Sort sort) {
      node = new SortNode(interpreter, sort);
    }

    public void visit(Union union) {
      node = new UnionNode(interpreter, union);
    }

    public void visit(Join join) {
      node = new JoinNode(interpreter, join);
    }

    public void visit(Window window) {
      node = new WindowNode(interpreter, window);
    }
  }
}

// End Nodes.java

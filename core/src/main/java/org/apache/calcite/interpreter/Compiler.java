/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Compiler.java
*    
*    Copyrights:
*      copyright (c) nicolas gallagher and jonathan neal
*      copyright (c) 2014 alexander farkas (afarkas)
*      copyright (c) 2013 coby chapple
*      copyright 2008 google inc.  all rights reserved
*      copyright (c) 2013 scott jehl
*      copyright (c) 2008-present tom preston-werner and jekyll contributors
*    
*    Licenses:
*      Apache License 2.0
*      SPDXId: Apache-2.0
*    
*    Auto-attribution by Threatrix, Inc.
*    
*    ------ END LICENSE ATTRIBUTION ------
*/
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

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Context while converting a tree of {@link RelNode} to a program
 * that can be run by an {@link Interpreter}.
 */
public interface Compiler {

  /** Compiles an expression to an executable form. */
  Scalar compile(List<RexNode> nodes, @Nullable RelDataType inputRowType);

  RelDataType combinedRowType(List<RelNode> inputs);

  Source source(RelNode rel, int ordinal);

  /**
   * Creates a Sink for a relational expression to write into.
   *
   * <p>This method is generally called from the constructor of a {@link Node}.
   * But a constructor could instead call
   * {@link #enumerable(RelNode, Enumerable)}.
   *
   * @param rel Relational expression
   * @return Sink
   */
  Sink sink(RelNode rel);

  /** Tells the interpreter that a given relational expression wishes to
   * give its output as an enumerable.
   *
   * <p>This is as opposed to the norm, where a relational expression calls
   * {@link #sink(RelNode)}, then its {@link Node#run()} method writes into that
   * sink.
   *
   * @param rel Relational expression
   * @param rowEnumerable Contents of relational expression
   */
  void enumerable(RelNode rel, Enumerable<Row> rowEnumerable);

  DataContext getDataContext();

  Context createContext();

}

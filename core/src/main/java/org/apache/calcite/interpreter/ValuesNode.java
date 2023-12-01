/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ValuesNode.java
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

import org.apache.calcite.rel.core.Values;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Interpreter node that implements a
 * {@link org.apache.calcite.rel.core.Values}.
 */
public class ValuesNode implements Node {
  private final Sink sink;
  private final int fieldCount;
  private final ImmutableList<Row> rows;

  public ValuesNode(Compiler compiler, Values rel) {
    this.sink = compiler.sink(rel);
    this.fieldCount = rel.getRowType().getFieldCount();
    this.rows = createRows(compiler, fieldCount, rel.getTuples());
  }

  private static ImmutableList<Row> createRows(Compiler compiler,
      int fieldCount,
      ImmutableList<ImmutableList<RexLiteral>> tuples) {
    final List<RexNode> nodes = new ArrayList<>();
    for (ImmutableList<RexLiteral> tuple : tuples) {
      nodes.addAll(tuple);
    }
    final Scalar scalar = compiler.compile(nodes, null);
    final Object[] values = new Object[nodes.size()];
    final Context context = compiler.createContext();
    scalar.execute(context, values);
    final ImmutableList.Builder<Row> rows = ImmutableList.builder();
    Object[] subValues = new Object[fieldCount];
    for (int r = 0, n = tuples.size(); r < n; ++r) {
      System.arraycopy(values, r * fieldCount, subValues, 0, fieldCount);
      rows.add(Row.asCopy(subValues));
    }
    return rows.build();
  }

  @Override public void run() throws InterruptedException {
    for (Row row : rows) {
      sink.send(row);
    }
    sink.end();
  }
}

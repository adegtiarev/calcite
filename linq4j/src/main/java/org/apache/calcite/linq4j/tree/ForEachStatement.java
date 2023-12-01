/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ForEachStatement.java
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
package org.apache.calcite.linq4j.tree;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents a "for-each" loop, "for (T v : iterable) { f(v); }".
 */
public class ForEachStatement extends Statement {
  public final ParameterExpression parameter;
  public final Expression iterable;
  public final Statement body;

  /** Cached hash code for the expression. */
  private int hash;

  public ForEachStatement(ParameterExpression parameter, Expression iterable,
      Statement body) {
    super(ExpressionType.ForEach, Void.TYPE);
    this.parameter = Objects.requireNonNull(parameter, "parameter");
    this.iterable = Objects.requireNonNull(iterable, "iterable");
    this.body = Objects.requireNonNull(body, "body"); // may be empty block, not null
  }

  @Override public ForEachStatement accept(Shuttle shuttle) {
    shuttle = shuttle.preVisit(this);
    final Expression iterable1 = iterable.accept(shuttle);
    final Statement body1 = body.accept(shuttle);
    return shuttle.visit(this, parameter, iterable1, body1);
  }

  @Override public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }

  @Override void accept0(ExpressionWriter writer) {
    writer.append("for (")
        .append(parameter.type)
        .append(" ")
        .append(parameter)
        .append(" : ")
        .append(iterable)
        .append(") ")
        .append(Blocks.toBlock(body));
  }

  @Override public boolean equals(@Nullable Object o) {
    return this == o
        || o instanceof ForEachStatement
        && parameter.equals(((ForEachStatement) o).parameter)
        && iterable.equals(((ForEachStatement) o).iterable)
        && body.equals(((ForEachStatement) o).body);
  }

  @Override public int hashCode() {
    int result = hash;
    if (result == 0) {
      result =
          Objects.hash(nodeType, type, parameter, iterable, body);
      if (result == 0) {
        result = 1;
      }
      hash = result;
    }
    return result;
  }
}

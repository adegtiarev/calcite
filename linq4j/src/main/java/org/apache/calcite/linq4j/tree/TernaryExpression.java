/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: TernaryExpression.java
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

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Represents an expression that has a ternary operator.
 */
public class TernaryExpression extends Expression {
  public final Expression expression0;
  public final Expression expression1;
  public final Expression expression2;

  TernaryExpression(ExpressionType nodeType, Type type, Expression expression0,
      Expression expression1, Expression expression2) {
    super(nodeType, type);
    assert expression0 != null : "expression0 should not be null";
    assert expression1 != null : "expression1 should not be null";
    assert expression2 != null : "expression2 should not be null";
    this.expression0 = expression0;
    this.expression1 = expression1;
    this.expression2 = expression2;
  }

  @Override public Expression accept(Shuttle shuttle) {
    shuttle = shuttle.preVisit(this);
    Expression expression0 = this.expression0.accept(shuttle);
    Expression expression1 = this.expression1.accept(shuttle);
    Expression expression2 = this.expression2.accept(shuttle);
    return shuttle.visit(this, expression0, expression1, expression2);
  }

  @Override public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }

  @Override void accept(ExpressionWriter writer, int lprec, int rprec) {
    if (writer.requireParentheses(this, lprec, rprec)) {
      return;
    }
    expression0.accept(writer, lprec, nodeType.lprec);
    writer.append(nodeType.op);
    expression1.accept(writer, nodeType.rprec, nodeType.lprec);
    writer.append(nodeType.op2);
    expression2.accept(writer, nodeType.rprec, rprec);
  }

  @Override public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    TernaryExpression that = (TernaryExpression) o;

    if (!expression0.equals(that.expression0)) {
      return false;
    }
    if (!expression1.equals(that.expression1)) {
      return false;
    }
    if (!expression2.equals(that.expression2)) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    return Objects.hash(nodeType, type, expression0, expression1, expression2);
  }
}

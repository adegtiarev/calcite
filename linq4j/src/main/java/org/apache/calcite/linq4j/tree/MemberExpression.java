/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MemberExpression.java
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Represents accessing a field or property.
 */
public class MemberExpression extends Expression {
  public final @Nullable Expression expression;
  public final PseudoField field;

  public MemberExpression(Expression expression, Field field) {
    this(expression, Types.field(field));
  }

  public MemberExpression(@Nullable Expression expression, PseudoField field) {
    super(ExpressionType.MemberAccess, field.getType());
    assert field != null : "field should not be null";
    assert expression != null || Modifier.isStatic(field.getModifiers())
        : "must specify expression if field is not static";
    this.expression = expression;
    this.field = field;
  }

  @Override public Expression accept(Shuttle shuttle) {
    shuttle = shuttle.preVisit(this);
    Expression expression1 = expression == null
        ? null
        : expression.accept(shuttle);
    return shuttle.visit(this, expression1);
  }

  @Override public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }

  @Override public @Nullable Object evaluate(Evaluator evaluator) {
    final Object o = expression == null
        ? null
        : expression.evaluate(evaluator);
    try {
      return field.get(o);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("error while evaluating " + this, e);
    }
  }

  @Override void accept(ExpressionWriter writer, int lprec, int rprec) {
    if (writer.requireParentheses(this, lprec, rprec)) {
      return;
    }
    if (expression != null) {
      expression.accept(writer, lprec, nodeType.lprec);
    } else {
      assert (field.getModifiers() & Modifier.STATIC) != 0;
      writer.append(field.getDeclaringClass());
    }
    writer.append('.').append(field.getName());
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

    MemberExpression that = (MemberExpression) o;

    if (expression != null ? !expression.equals(that.expression) : that
        .expression != null) {
      return false;
    }
    if (!field.equals(that.field)) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    return Objects.hash(nodeType, type, expression, field);
  }
}

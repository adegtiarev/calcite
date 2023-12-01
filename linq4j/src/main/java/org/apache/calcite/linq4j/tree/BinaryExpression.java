/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: BinaryExpression.java
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

import static java.util.Objects.requireNonNull;

/**
 * Represents an expression that has a binary operator.
 */
public class BinaryExpression extends Expression {
  public final Expression expression0;
  public final Expression expression1;
  private final @Nullable Primitive primitive;

  BinaryExpression(ExpressionType nodeType, Type type, Expression expression0,
      Expression expression1) {
    super(nodeType, type);
    assert expression0 != null : "expression0 should not be null";
    assert expression1 != null : "expression1 should not be null";
    this.expression0 = expression0;
    this.expression1 = expression1;
    this.primitive = Primitive.of(expression0.getType());
  }

  @Override public Expression accept(Shuttle visitor) {
    visitor = visitor.preVisit(this);
    Expression expression0 = this.expression0.accept(visitor);
    Expression expression1 = this.expression1.accept(visitor);
    return visitor.visit(this, expression0, expression1);
  }

  @Override public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }

  @Override public Object evaluate(Evaluator evaluator) {
    switch (nodeType) {
    case AndAlso:
      return evaluateBoolean(evaluator, expression0)
             && evaluateBoolean(evaluator, expression1);
    case Add:
      if (primitive == null) {
        throw cannotEvaluate();
      }
      switch (primitive) {
      case INT:
        return evaluateInt(expression0, evaluator) + evaluateInt(expression1, evaluator);
      case SHORT:
        return evaluateShort(expression0, evaluator) + evaluateShort(expression1, evaluator);
      case BYTE:
        return evaluateByte(expression0, evaluator) + evaluateByte(expression1, evaluator);
      case FLOAT:
        return evaluateFloat(expression0, evaluator) + evaluateFloat(expression1, evaluator);
      case DOUBLE:
        return evaluateDouble(expression0, evaluator) + evaluateDouble(expression1, evaluator);
      case LONG:
        return evaluateLong(expression0, evaluator) + evaluateLong(expression1, evaluator);
      default:
        throw cannotEvaluate();
      }
    case Divide:
      if (primitive == null) {
        throw cannotEvaluate();
      }
      switch (primitive) {
      case INT:
        return evaluateInt(expression0, evaluator) / evaluateInt(expression1, evaluator);
      case SHORT:
        return evaluateShort(expression0, evaluator) / evaluateShort(expression1, evaluator);
      case BYTE:
        return evaluateByte(expression0, evaluator) / evaluateByte(expression1, evaluator);
      case FLOAT:
        return evaluateFloat(expression0, evaluator) / evaluateFloat(expression1, evaluator);
      case DOUBLE:
        return evaluateDouble(expression0, evaluator) / evaluateDouble(expression1, evaluator);
      case LONG:
        return evaluateLong(expression0, evaluator) / evaluateLong(expression1, evaluator);
      default:
        throw cannotEvaluate();
      }
    case Equal:
      return Objects.equals(expression0.evaluate(evaluator), expression1.evaluate(evaluator));
    case GreaterThan:
      if (primitive == null) {
        throw cannotEvaluate();
      }
      switch (primitive) {
      case INT:
        return evaluateInt(expression0, evaluator) > evaluateInt(expression1, evaluator);
      case SHORT:
        return evaluateShort(expression0, evaluator) > evaluateShort(expression1, evaluator);
      case BYTE:
        return evaluateByte(expression0, evaluator) > evaluateByte(expression1, evaluator);
      case FLOAT:
        return evaluateFloat(expression0, evaluator) > evaluateFloat(expression1, evaluator);
      case DOUBLE:
        return evaluateDouble(expression0, evaluator) > evaluateDouble(expression1, evaluator);
      case LONG:
        return evaluateLong(expression0, evaluator) > evaluateLong(expression1, evaluator);
      default:
        throw cannotEvaluate();
      }
    case GreaterThanOrEqual:
      if (primitive == null) {
        throw cannotEvaluate();
      }
      switch (primitive) {
      case INT:
        return evaluateInt(expression0, evaluator) >= evaluateInt(expression1, evaluator);
      case SHORT:
        return evaluateShort(expression0, evaluator) >= evaluateShort(expression1, evaluator);
      case BYTE:
        return evaluateByte(expression0, evaluator) >= evaluateByte(expression1, evaluator);
      case FLOAT:
        return evaluateFloat(expression0, evaluator) >= evaluateFloat(expression1, evaluator);
      case DOUBLE:
        return evaluateDouble(expression0, evaluator) >= evaluateDouble(expression1, evaluator);
      case LONG:
        return evaluateLong(expression0, evaluator) >= evaluateLong(expression1, evaluator);
      default:
        throw cannotEvaluate();
      }
    case LessThan:
      if (primitive == null) {
        throw cannotEvaluate();
      }
      switch (primitive) {
      case INT:
        return evaluateInt(expression0, evaluator) < evaluateInt(expression1, evaluator);
      case SHORT:
        return evaluateShort(expression0, evaluator) < evaluateShort(expression1, evaluator);
      case BYTE:
        return evaluateByte(expression0, evaluator) < evaluateByte(expression1, evaluator);
      case FLOAT:
        return evaluateFloat(expression0, evaluator) < evaluateFloat(expression1, evaluator);
      case DOUBLE:
        return evaluateDouble(expression0, evaluator) < evaluateDouble(expression1, evaluator);
      case LONG:
        return evaluateLong(expression0, evaluator) < evaluateLong(expression1, evaluator);
      default:
        throw cannotEvaluate();
      }
    case LessThanOrEqual:
      if (primitive == null) {
        throw cannotEvaluate();
      }
      switch (primitive) {
      case INT:
        return evaluateInt(expression0, evaluator) <= evaluateInt(expression1, evaluator);
      case SHORT:
        return evaluateShort(expression0, evaluator) <= evaluateShort(expression1, evaluator);
      case BYTE:
        return evaluateByte(expression0, evaluator) <= evaluateByte(expression1, evaluator);
      case FLOAT:
        return evaluateFloat(expression0, evaluator) <= evaluateFloat(expression1, evaluator);
      case DOUBLE:
        return evaluateDouble(expression0, evaluator) <= evaluateDouble(expression1, evaluator);
      case LONG:
        return evaluateLong(expression0, evaluator) <= evaluateLong(expression1, evaluator);
      default:
        throw cannotEvaluate();
      }
    case Multiply:
      if (primitive == null) {
        throw cannotEvaluate();
      }
      switch (primitive) {
      case INT:
        return evaluateInt(expression0, evaluator) * evaluateInt(expression1, evaluator);
      case SHORT:
        return evaluateShort(expression0, evaluator) * evaluateShort(expression1, evaluator);
      case BYTE:
        return evaluateByte(expression0, evaluator) * evaluateByte(expression1, evaluator);
      case FLOAT:
        return evaluateFloat(expression0, evaluator) * evaluateFloat(expression1, evaluator);
      case DOUBLE:
        return evaluateDouble(expression0, evaluator) * evaluateDouble(expression1, evaluator);
      case LONG:
        return evaluateLong(expression0, evaluator) * evaluateLong(expression1, evaluator);
      default:
        throw cannotEvaluate();
      }
    case NotEqual:
      return !Objects.equals(expression0.evaluate(evaluator), expression1.evaluate(evaluator));
    case OrElse:
      return evaluateBoolean(evaluator, expression0)
             || evaluateBoolean(evaluator, expression1);
    case Subtract:
      if (primitive == null) {
        throw cannotEvaluate();
      }
      switch (primitive) {
      case INT:
        return evaluateInt(expression0, evaluator) - evaluateInt(expression1, evaluator);
      case SHORT:
        return evaluateShort(expression0, evaluator) - evaluateShort(expression1, evaluator);
      case BYTE:
        return evaluateByte(expression0, evaluator) - evaluateByte(expression1, evaluator);
      case FLOAT:
        return evaluateFloat(expression0, evaluator) - evaluateFloat(expression1, evaluator);
      case DOUBLE:
        return evaluateDouble(expression0, evaluator) - evaluateDouble(expression1, evaluator);
      case LONG:
        return evaluateLong(expression0, evaluator) - evaluateLong(expression1, evaluator);
      default:
        throw cannotEvaluate();
      }
    default:
      throw cannotEvaluate();
    }
  }

  @Override void accept(ExpressionWriter writer, int lprec, int rprec) {
    if (writer.requireParentheses(this, lprec, rprec)) {
      return;
    }
    expression0.accept(writer, lprec, nodeType.lprec);
    writer.append(nodeType.op);
    expression1.accept(writer, nodeType.rprec, rprec);
  }

  private RuntimeException cannotEvaluate() {
    return new RuntimeException("cannot evaluate " + this + ", nodeType="
      + nodeType + ", primitive=" + primitive);
  }

  private static boolean evaluateBoolean(Evaluator evaluator, Expression expression) {
    return (Boolean) requireNonNull(
        expression.evaluate(evaluator),
        () -> "boolean expected, got null while evaluating " + expression);
  }

  private static Number evaluateNumber(Expression expression, Evaluator evaluator) {
    return (Number) requireNonNull(
        expression.evaluate(evaluator),
        () -> "number expected, got null while evaluating " + expression);
  }

  private static int evaluateInt(Expression expression, Evaluator evaluator) {
    return evaluateNumber(expression, evaluator).intValue();
  }

  private static short evaluateShort(Expression expression, Evaluator evaluator) {
    return evaluateNumber(expression, evaluator).shortValue();
  }

  private static long evaluateLong(Expression expression, Evaluator evaluator) {
    return evaluateNumber(expression, evaluator).longValue();
  }

  private static byte evaluateByte(Expression expression, Evaluator evaluator) {
    return evaluateNumber(expression, evaluator).byteValue();
  }

  private static float evaluateFloat(Expression expression, Evaluator evaluator) {
    return evaluateNumber(expression, evaluator).floatValue();
  }

  private static double evaluateDouble(Expression expression, Evaluator evaluator) {
    return evaluateNumber(expression, evaluator).doubleValue();
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

    BinaryExpression that = (BinaryExpression) o;

    if (!expression0.equals(that.expression0)) {
      return false;
    }
    if (!expression1.equals(that.expression1)) {
      return false;
    }
    if (primitive != that.primitive) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    return Objects.hash(nodeType, type, expression0, expression1, primitive);
  }
}

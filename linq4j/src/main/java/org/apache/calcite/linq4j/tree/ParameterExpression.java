/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ParameterExpression.java
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

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a named parameter expression.
 */
public class ParameterExpression extends Expression {
  private static final AtomicInteger SEQ = new AtomicInteger();

  public final int modifier;
  public final String name;

  public ParameterExpression(Type type) {
    this(0, type, "p" + SEQ.getAndIncrement());
  }

  public ParameterExpression(int modifier, Type type, String name) {
    super(ExpressionType.Parameter, type);
    assert name != null : "name should not be null";
    assert Character.isJavaIdentifierStart(name.charAt(0))
      : "parameter name should be valid java identifier: "
        + name + ". The first character is invalid.";
    this.modifier = modifier;
    this.name = name;
  }

  @Override public Expression accept(Shuttle shuttle) {
    return shuttle.visit(this);
  }

  @Override public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }

  @Override public @Nullable Object evaluate(Evaluator evaluator) {
    return evaluator.peek(this);
  }

  @Override void accept(ExpressionWriter writer, int lprec, int rprec) {
    writer.append(name);
  }

  String declString() {
    return declString(type);
  }

  String declString(Type type) {
    final String modifiers = Modifier.toString(modifier);
    return modifiers + (modifiers.isEmpty() ? "" : " ") + Types.className(type)
        + " " + name;
  }

  @Override public boolean equals(@Nullable Object o) {
    return this == o;
  }

  @Override public int hashCode() {
    return System.identityHashCode(this);
  }
}

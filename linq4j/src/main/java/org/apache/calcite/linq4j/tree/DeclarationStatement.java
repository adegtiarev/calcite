/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: DeclarationStatement.java
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
import java.util.Objects;

/**
 * Expression that declares and optionally initializes a variable.
 */
public class DeclarationStatement extends Statement {
  public final int modifiers;
  public final ParameterExpression parameter;
  public final @Nullable Expression initializer;

  public DeclarationStatement(int modifiers, ParameterExpression parameter,
      @Nullable Expression initializer) {
    super(ExpressionType.Declaration, Void.TYPE);
    assert parameter != null : "parameter should not be null";
    this.modifiers = modifiers;
    this.parameter = parameter;
    this.initializer = initializer;
  }

  @Override public DeclarationStatement accept(Shuttle shuttle) {
    shuttle = shuttle.preVisit(this);
    // do not visit parameter - visit may not return a ParameterExpression
    Expression initializer = this.initializer != null
        ? this.initializer.accept(shuttle)
        : null;
    return shuttle.visit(this, initializer);
  }

  @Override public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }

  @Override void accept0(ExpressionWriter writer) {
    final String modifiers = Modifier.toString(this.modifiers);
    if (!modifiers.isEmpty()) {
      writer.append(modifiers).append(' ');
    }
    writer.append(parameter.type).append(' ').append(parameter.name);
    if (initializer != null) {
      writer.append(" = ").append(initializer);
    }
    writer.append(';');
    writer.newlineAndIndent();
  }

  public void accept2(ExpressionWriter writer, boolean withType) {
    if (withType) {
      final String modifiers = Modifier.toString(this.modifiers);
      if (!modifiers.isEmpty()) {
        writer.append(modifiers).append(' ');
      }
      writer.append(parameter.type).append(' ');
    } else {
      writer.append(", ");
    }
    writer.append(parameter.name);
    if (initializer != null) {
      writer.append(" = ").append(initializer);
    }
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

    DeclarationStatement that = (DeclarationStatement) o;

    if (modifiers != that.modifiers) {
      return false;
    }
    if (initializer != null ? !initializer.equals(that.initializer) : that
        .initializer != null) {
      return false;
    }
    if (!parameter.equals(that.parameter)) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    return Objects.hash(nodeType, type, modifiers, parameter, initializer);
  }
}

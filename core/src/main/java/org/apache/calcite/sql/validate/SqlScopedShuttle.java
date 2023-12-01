/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlScopedShuttle.java
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
package org.apache.calcite.sql.validate;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.util.SqlShuttle;
import org.apache.calcite.sql.util.SqlVisitor;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.util.Objects.requireNonNull;

/**
 * Refinement to {@link SqlShuttle} which maintains a stack of scopes.
 *
 * <p>Derived class should override {@link #visitScoped(SqlCall)} rather than
 * {@link SqlVisitor#visit(SqlCall)}.
 */
public abstract class SqlScopedShuttle extends SqlShuttle {
  //~ Instance fields --------------------------------------------------------

  private final Deque<SqlValidatorScope> scopes = new ArrayDeque<>();

  //~ Constructors -----------------------------------------------------------

  protected SqlScopedShuttle(SqlValidatorScope initialScope) {
    scopes.push(initialScope);
  }

  //~ Methods ----------------------------------------------------------------

  @Override public final @Nullable SqlNode visit(SqlCall call) {
    SqlValidatorScope oldScope = getScope();
    SqlValidatorScope newScope = oldScope.getOperandScope(call);
    scopes.push(newScope);
    SqlNode result = visitScoped(call);
    scopes.pop();
    return result;
  }

  /**
   * Visits an operator call. If the call has entered a new scope, the base
   * class will have already modified the scope.
   */
  protected @Nullable SqlNode visitScoped(SqlCall call) {
    return super.visit(call);
  }

  /**
   * Returns the current scope.
   */
  protected SqlValidatorScope getScope() {
    return requireNonNull(scopes.peek(), "scopes.peek()");
  }
}

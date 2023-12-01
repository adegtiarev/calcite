/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.33.0
*    Source File: SetopNamespace.java
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

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.util.Util;

import org.checkerframework.checker.nullness.qual.Nullable;

import static org.apache.calcite.util.Static.RESOURCE;

import static java.util.Objects.requireNonNull;

/**
 * Namespace based upon a set operation (UNION, INTERSECT, EXCEPT).
 */
public class SetopNamespace extends AbstractNamespace {
  //~ Instance fields --------------------------------------------------------

  private final SqlCall call;

  //~ Constructors -----------------------------------------------------------

  /**
   * Creates a <code>SetopNamespace</code>.
   *
   * @param validator     Validator
   * @param call          Call to set operator
   * @param enclosingNode Enclosing node
   */
  protected SetopNamespace(
      SqlValidatorImpl validator,
      SqlCall call,
      SqlNode enclosingNode) {
    super(validator, enclosingNode);
    this.call = call;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public @Nullable SqlNode getNode() {
    return call;
  }

  @Override public SqlMonotonicity getMonotonicity(String columnName) {
    SqlMonotonicity monotonicity = null;
    int index = getRowType().getFieldNames().indexOf(columnName);
    if (index < 0) {
      return SqlMonotonicity.NOT_MONOTONIC;
    }
    for (SqlNode operand : call.getOperandList()) {
      final SqlValidatorNamespace namespace =
          requireNonNull(
              validator.getNamespace(operand),
              () -> "namespace for " + operand);
      monotonicity = combine(monotonicity,
          namespace.getMonotonicity(
              namespace.getRowType().getFieldNames().get(index)));
    }
    return Util.first(monotonicity, SqlMonotonicity.NOT_MONOTONIC);
  }

  private static SqlMonotonicity combine(@Nullable SqlMonotonicity m0,
      SqlMonotonicity m1) {
    if (m0 == null) {
      return m1;
    }
    if (m1 == null) {
      return m0;
    }
    if (m0 == m1) {
      return m0;
    }
    if (m0.unstrict() == m1) {
      return m1;
    }
    if (m1.unstrict() == m0) {
      return m0;
    }
    return SqlMonotonicity.NOT_MONOTONIC;
  }

  @Override public RelDataType validateImpl(RelDataType targetRowType) {
    switch (call.getKind()) {
    case UNION:
    case INTERSECT:
    case EXCEPT:
      final SqlValidatorScope scope = requireNonNull(
          validator.scopes.get(call),
          () -> "scope for " + call);
      for (SqlNode operand : call.getOperandList()) {
        if (!operand.isA(SqlKind.QUERY)) {
          throw validator.newValidationError(operand,
              RESOURCE.needQueryOp(operand.toString()));
        }
        validator.validateQuery(operand, scope, targetRowType);
      }
      return call.getOperator().deriveType(
          validator,
          scope,
          call);
    default:
      throw new AssertionError("Not a query: " + call.getKind());
    }
  }
}

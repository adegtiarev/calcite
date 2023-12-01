/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlAbstractGroupFunction.java
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
package org.apache.calcite.sql.fun;

import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.type.SqlOperandTypeChecker;
import org.apache.calcite.sql.type.SqlOperandTypeInference;
import org.apache.calcite.sql.type.SqlReturnTypeInference;
import org.apache.calcite.sql.validate.AggregatingSelectScope;
import org.apache.calcite.sql.validate.OrderByScope;
import org.apache.calcite.sql.validate.SelectScope;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorScope;
import org.apache.calcite.sql.validate.SqlValidatorUtil;
import org.apache.calcite.util.Optionality;
import org.apache.calcite.util.Static;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Base class for grouping functions {@code GROUP_ID}, {@code GROUPING_ID},
 * {@code GROUPING}.
 */
public class SqlAbstractGroupFunction extends SqlAggFunction {
  /**
   * Creates a SqlAbstractGroupFunction.
   *
   * @param name                 Name of builtin function
   * @param kind                 kind of operator implemented by function
   * @param returnTypeInference  strategy to use for return type inference
   * @param operandTypeInference strategy to use for parameter type inference
   * @param operandTypeChecker   strategy to use for parameter type checking
   * @param category             categorization for function
   */
  public SqlAbstractGroupFunction(String name,
      SqlKind kind,
      SqlReturnTypeInference returnTypeInference,
      @Nullable SqlOperandTypeInference operandTypeInference,
      SqlOperandTypeChecker operandTypeChecker,
      SqlFunctionCategory category) {
    super(name, null, kind, returnTypeInference, operandTypeInference,
        operandTypeChecker, category, false, false, Optionality.FORBIDDEN);
  }

  @Override public void validateCall(SqlCall call, SqlValidator validator,
      SqlValidatorScope scope, SqlValidatorScope operandScope) {
    super.validateCall(call, validator, scope, operandScope);
    final SelectScope selectScope =
        SqlValidatorUtil.getEnclosingSelectScope(scope);
    assert selectScope != null;
    final SqlSelect select = selectScope.getNode();
    if (!validator.isAggregate(select)) {
      throw validator.newValidationError(call,
          Static.RESOURCE.groupingInAggregate(getName()));
    }
    final AggregatingSelectScope aggregatingSelectScope =
        SqlValidatorUtil.getEnclosingAggregateSelectScope(scope);
    if (aggregatingSelectScope == null) {
      // We're probably in the GROUP BY clause
      throw validator.newValidationError(call,
          Static.RESOURCE.groupingInWrongClause(getName()));
    }
    for (SqlNode operand : call.getOperandList()) {
      if (scope instanceof OrderByScope) {
        operand = validator.expandOrderExpr(select, operand);
      } else {
        operand = validator.expand(operand, scope);
      }
      if (!aggregatingSelectScope.resolved.get().isGroupingExpr(operand)) {
        throw validator.newValidationError(operand,
            Static.RESOURCE.groupingArgument(getName()));
      }
    }
  }

  @Override public boolean isQuantifierAllowed() {
    return false;
  }

  @Override public boolean allowsFilter() {
    return false;
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlNullTreatmentOperator.java
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
package org.apache.calcite.sql;

import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorScope;
import org.apache.calcite.util.ImmutableNullableList;

import com.google.common.base.Preconditions;

import org.checkerframework.checker.nullness.qual.Nullable;

import static org.apache.calcite.util.Static.RESOURCE;

/**
 * An operator that decides how to handle null input
 * ({@code RESPECT NULLS} and {@code IGNORE NULLS}).
 *
 * <p>Currently, only the windowed aggregate functions {@code FIRST_VALUE},
 * {@code LAST_VALUE}, {@code LEAD} and {@code LAG} support it.
 *
 * @see SqlAggFunction#allowsNullTreatment()
 */
public class SqlNullTreatmentOperator extends SqlSpecialOperator {
  public SqlNullTreatmentOperator(SqlKind kind) {
    super(kind.sql, kind, 20, true, ReturnTypes.ARG0, null, OperandTypes.ANY);
    Preconditions.checkArgument(kind == SqlKind.RESPECT_NULLS
        || kind == SqlKind.IGNORE_NULLS);
  }

  @Override public SqlCall createCall(@Nullable SqlLiteral functionQualifier,
      SqlParserPos pos, @Nullable SqlNode... operands) {
    // As super.createCall, but don't union the positions
    return new SqlBasicCall(this, ImmutableNullableList.copyOf(operands), pos,
        functionQualifier);
  }

  @Override public void unparse(SqlWriter writer, SqlCall call, int leftPrec,
      int rightPrec) {
    assert call.operandCount() == 1;
    call.operand(0).unparse(writer, getLeftPrec(), getRightPrec());
    writer.keyword(getName());
  }

  @Override public void validateCall(
      SqlCall call,
      SqlValidator validator,
      SqlValidatorScope scope,
      SqlValidatorScope operandScope) {
    assert call.getOperator() == this;
    assert call.operandCount() == 1;
    SqlCall aggCall = call.operand(0);
    if (!aggCall.getOperator().isAggregator()
        || !((SqlAggFunction) aggCall.getOperator()).allowsNullTreatment()) {
      throw validator.newValidationError(aggCall,
          RESOURCE.disallowsNullTreatment(aggCall.getOperator().getName()));
    }
  }
}

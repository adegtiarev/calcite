/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: LiteralOperandTypeChecker.java
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
package org.apache.calcite.sql.type;

import org.apache.calcite.sql.SqlCallBinding;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperandCountRange;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlUtil;
import org.apache.calcite.util.Util;

import static org.apache.calcite.util.Static.RESOURCE;

/**
 * Parameter type-checking strategy type must be a literal (whether null is
 * allowed is determined by the constructor). <code>CAST(NULL as ...)</code> is
 * considered to be a NULL literal but not <code>CAST(CAST(NULL as ...) AS
 * ...)</code>
 */
public class LiteralOperandTypeChecker implements SqlSingleOperandTypeChecker {
  //~ Instance fields --------------------------------------------------------

  private boolean allowNull;

  //~ Constructors -----------------------------------------------------------

  public LiteralOperandTypeChecker(boolean allowNull) {
    this.allowNull = allowNull;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public boolean isOptional(int i) {
    return false;
  }

  @Override public boolean checkSingleOperandType(
      SqlCallBinding callBinding,
      SqlNode node,
      int iFormalOperand,
      boolean throwOnFailure) {
    Util.discard(iFormalOperand);

    if (SqlUtil.isNullLiteral(node, true)) {
      if (allowNull) {
        return true;
      }
      if (throwOnFailure) {
        throw callBinding.newError(
            RESOURCE.argumentMustNotBeNull(
                callBinding.getOperator().getName()));
      }
      return false;
    }
    if (!SqlUtil.isLiteral(node) && !SqlUtil.isLiteralChain(node)) {
      if (throwOnFailure) {
        throw callBinding.newError(
            RESOURCE.argumentMustBeLiteral(
                callBinding.getOperator().getName()));
      }
      return false;
    }

    return true;
  }

  @Override public boolean checkOperandTypes(
      SqlCallBinding callBinding,
      boolean throwOnFailure) {
    return checkSingleOperandType(
        callBinding,
        callBinding.operand(0),
        0,
        throwOnFailure);
  }

  @Override public SqlOperandCountRange getOperandCountRange() {
    return SqlOperandCountRanges.of(1);
  }

  @Override public String getAllowedSignatures(SqlOperator op, String opName) {
    return "<LITERAL>";
  }

  @Override public Consistency getConsistency() {
    return Consistency.NONE;
  }
}

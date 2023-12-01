/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: SqlPositionFunction.java
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

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlCallBinding;
import org.apache.calcite.sql.SqlFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.type.SqlOperandTypeChecker;

/**
 * The <code>POSITION</code> function.
 */
public class SqlPositionFunction extends SqlFunction {
  //~ Constructors -----------------------------------------------------------

  // FIXME jvs 25-Jan-2009:  POSITION should verify that
  // params are all same character set, like OVERLAY does implicitly
  // as part of rtiDyadicStringSumPrecision

  private static final SqlOperandTypeChecker OTC_CUSTOM =
      OperandTypes.or(OperandTypes.STRING_SAME_SAME,
          OperandTypes.STRING_SAME_SAME_INTEGER);

  public SqlPositionFunction() {
    super(
        "POSITION",
        SqlKind.POSITION,
        ReturnTypes.INTEGER_NULLABLE,
        null,
        OTC_CUSTOM,
        SqlFunctionCategory.NUMERIC);
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void unparse(
      SqlWriter writer,
      SqlCall call,
      int leftPrec,
      int rightPrec) {
    final SqlWriter.Frame frame = writer.startFunCall(getName());
    call.operand(0).unparse(writer, leftPrec, rightPrec);
    writer.sep("IN");
    call.operand(1).unparse(writer, leftPrec, rightPrec);
    if (3 == call.operandCount()) {
      writer.sep("FROM");
      call.operand(2).unparse(writer, leftPrec, rightPrec);
    }
    writer.endFunCall(frame);
  }

  @Override public String getSignatureTemplate(final int operandsCount) {
    switch (operandsCount) {
    case 2:
      return "{0}({1} IN {2})";
    case 3:
      return "{0}({1} IN {2} FROM {3})";
    default:
      throw new AssertionError();
    }
  }

  @Override public boolean checkOperandTypes(
      SqlCallBinding callBinding,
      boolean throwOnFailure) {
    // check that the two operands are of same type.
    switch (callBinding.getOperandCount()) {
    case 2:
      return OperandTypes.SAME_SAME.checkOperandTypes(
          callBinding, throwOnFailure)
          && super.checkOperandTypes(callBinding, throwOnFailure);

    case 3:
      return OperandTypes.SAME_SAME_INTEGER.checkOperandTypes(
          callBinding, throwOnFailure)
          && super.checkOperandTypes(callBinding, throwOnFailure);
    default:
      throw new AssertionError();
    }
  }
}

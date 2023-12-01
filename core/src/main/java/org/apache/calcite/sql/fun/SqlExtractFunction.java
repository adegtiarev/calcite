/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: SqlExtractFunction.java
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

import org.apache.calcite.avatica.util.TimeUnitRange;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlOperatorBinding;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.validate.SqlMonotonicity;
import org.apache.calcite.util.Util;

import static org.apache.calcite.sql.validate.SqlNonNullableAccessors.getOperandLiteralValueOrThrow;

/**
 * The SQL <code>EXTRACT</code> operator. Extracts a specified field value from
 * a DATETIME or an INTERVAL. E.g.<br>
 * <code>EXTRACT(HOUR FROM INTERVAL '364 23:59:59')</code> returns <code>
 * 23</code>
 */
public class SqlExtractFunction extends SqlFunction {
  //~ Constructors -----------------------------------------------------------

  // SQL2003, Part 2, Section 4.4.3 - extract returns a exact numeric
  // TODO: Return type should be decimal for seconds
  public SqlExtractFunction() {
    super("EXTRACT", SqlKind.EXTRACT, ReturnTypes.BIGINT_NULLABLE, null,
        OperandTypes.INTERVALINTERVAL_INTERVALDATETIME,
        SqlFunctionCategory.SYSTEM);
  }

  //~ Methods ----------------------------------------------------------------

  @Override public String getSignatureTemplate(int operandsCount) {
    Util.discard(operandsCount);
    return "{0}({1} FROM {2})";
  }

  @Override public void unparse(
      SqlWriter writer,
      SqlCall call,
      int leftPrec,
      int rightPrec) {
    final SqlWriter.Frame frame = writer.startFunCall(getName());
    call.operand(0).unparse(writer, 0, 0);
    writer.sep("FROM");
    call.operand(1).unparse(writer, 0, 0);
    writer.endFunCall(frame);
  }

  @Override public SqlMonotonicity getMonotonicity(SqlOperatorBinding call) {
    TimeUnitRange value = getOperandLiteralValueOrThrow(call, 0, TimeUnitRange.class);
    switch (value) {
    case YEAR:
      return call.getOperandMonotonicity(1).unstrict();
    default:
      return SqlMonotonicity.NOT_MONOTONIC;
    }
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.30.0
*    Source File: SqlRollupOperator.java
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
import org.apache.calcite.sql.SqlInternalOperator;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;

/**
 * Operator that appears in a {@code GROUP BY} clause: {@code CUBE},
 * {@code ROLLUP}, {@code GROUPING SETS}.
 */
class SqlRollupOperator extends SqlInternalOperator {
  SqlRollupOperator(String name, SqlKind kind) {
    super(name, kind, 4);
  }

  @Override public void unparse(SqlWriter writer, SqlCall call, int leftPrec,
      int rightPrec) {
    switch (kind) {
    case ROLLUP:
      if (!writer.getDialect().supportsAggregateFunction(kind)
          && writer.getDialect().supportsGroupByWithRollup()) {
        // MySQL version 5: generate "GROUP BY x, y WITH ROLLUP".
        // MySQL version 8 and higher is SQL-compliant,
        // so generate "GROUP BY ROLLUP(x, y)"
        unparseKeyword(writer, call, "WITH ROLLUP");
        return;
      }
      break;
    case CUBE:
      if (!writer.getDialect().supportsAggregateFunction(kind)
          && writer.getDialect().supportsGroupByWithCube()) {
        // Spark SQL: generate "GROUP BY x, y WITH CUBE".
        unparseKeyword(writer, call, "WITH CUBE");
        return;
      }
      break;
    default:
      break;
    }
    unparseCube(writer, call);
  }

  private static void unparseKeyword(SqlWriter writer, SqlCall call, String keyword) {
    final SqlWriter.Frame groupFrame =
        writer.startList(SqlWriter.FrameTypeEnum.GROUP_BY_LIST);
    for (SqlNode operand : call.getOperandList()) {
      writer.sep(",");
      operand.unparse(writer, 2, 3);
    }
    writer.endList(groupFrame);
    writer.keyword(keyword);
  }

  private static void unparseCube(SqlWriter writer, SqlCall call) {
    writer.keyword(call.getOperator().getName());
    final SqlWriter.Frame frame =
        writer.startList(SqlWriter.FrameTypeEnum.FUN_CALL, "(", ")");
    for (SqlNode operand : call.getOperandList()) {
      writer.sep(",");
      if (operand.getKind() == SqlKind.ROW) {
        final SqlWriter.Frame frame2 =
            writer.startList(SqlWriter.FrameTypeEnum.SIMPLE, "(", ")");
        for (SqlNode operand2 : ((SqlCall) operand).getOperandList()) {
          writer.sep(",");
          operand2.unparse(writer, 0, 0);
        }
        writer.endList(frame2);
      } else if (operand instanceof SqlNodeList
          && ((SqlNodeList) operand).size() == 0) {
        writer.keyword("()");
      } else {
        operand.unparse(writer, 0, 0);
      }
    }
    writer.endList(frame);
  }
}

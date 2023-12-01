/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: SqlJsonExistsFunction.java
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
import org.apache.calcite.sql.SqlFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.type.SqlTypeFamily;
import org.apache.calcite.sql.type.SqlTypeTransforms;

/**
 * The <code>JSON_EXISTS</code> function.
 */
public class SqlJsonExistsFunction extends SqlFunction {
  public SqlJsonExistsFunction() {
    super("JSON_EXISTS", SqlKind.OTHER_FUNCTION,
        ReturnTypes.BOOLEAN.andThen(SqlTypeTransforms.FORCE_NULLABLE), null,
        OperandTypes.or(
            OperandTypes.family(SqlTypeFamily.ANY, SqlTypeFamily.CHARACTER),
            OperandTypes.family(SqlTypeFamily.ANY, SqlTypeFamily.CHARACTER,
                SqlTypeFamily.ANY)),
        SqlFunctionCategory.SYSTEM);
  }

  @Override public String getAllowedSignatures(String opNameToUse) {
    return "JSON_EXISTS(json_doc, path [{TRUE | FALSE| UNKNOWN | ERROR} ON ERROR])";
  }

  @Override public void unparse(SqlWriter writer, SqlCall call, int leftPrec,
      int rightPrec) {
    final SqlWriter.Frame frame = writer.startFunCall(getName());
    call.operand(0).unparse(writer, 0, 0);
    writer.sep(",", true);
    for (int i = 1; i < call.operandCount(); i++) {
      call.operand(i).unparse(writer, leftPrec, rightPrec);
    }
    if (call.operandCount() == 3) {
      writer.keyword("ON ERROR");
    }
    writer.endFunCall(frame);
  }
}

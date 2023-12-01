/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlInfixOperator.java
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

import org.apache.calcite.linq4j.Ord;
import org.apache.calcite.sql.type.SqlOperandTypeChecker;
import org.apache.calcite.sql.type.SqlOperandTypeInference;
import org.apache.calcite.sql.type.SqlReturnTypeInference;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A generalization of a binary operator to involve several (two or more)
 * arguments, and keywords between each pair of arguments.
 *
 * <p>For example, the <code>BETWEEN</code> operator is ternary, and has syntax
 * <code><i>exp1</i> BETWEEN <i>exp2</i> AND <i>exp3</i></code>.
 */
public class SqlInfixOperator extends SqlSpecialOperator {
  //~ Instance fields --------------------------------------------------------

  private final String[] names;

  //~ Constructors -----------------------------------------------------------

  protected SqlInfixOperator(
      String[] names,
      SqlKind kind,
      int precedence,
      @Nullable SqlReturnTypeInference returnTypeInference,
      @Nullable SqlOperandTypeInference operandTypeInference,
      @Nullable SqlOperandTypeChecker operandTypeChecker) {
    super(
        names[0],
        kind,
        precedence,
        true,
        returnTypeInference,
        operandTypeInference,
        operandTypeChecker);
    assert names.length > 1;
    this.names = names;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void unparse(
      SqlWriter writer,
      SqlCall call,
      int leftPrec,
      int rightPrec) {
    assert call.operandCount() == names.length + 1;
    final boolean needWhitespace = needsSpace();
    for (Ord<SqlNode> operand : Ord.zip(call.getOperandList())) {
      if (operand.i > 0) {
        writer.setNeedWhitespace(needWhitespace);
        writer.keyword(names[operand.i - 1]);
        writer.setNeedWhitespace(needWhitespace);
      }
      operand.e.unparse(writer, leftPrec, getLeftPrec());
    }
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/cf-testorg/calcite-test/releases/tag/calcite-1.26.0-rc0
*    Source File: SqlArgumentAssignmentOperator.java
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

import org.apache.calcite.sql.SqlAsOperator;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.type.InferTypes;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;

/**
 * Operator that assigns an argument to a function call to a particular named
 * parameter.
 *
 * <p>Not an expression; just a holder to represent syntax until the validator
 * has chance to resolve arguments.
 *
 * <p>Sub-class of {@link SqlAsOperator} ("AS") out of convenience; to be
 * consistent with AS, we reverse the arguments.
 */
class SqlArgumentAssignmentOperator extends SqlAsOperator {
  SqlArgumentAssignmentOperator() {
    super("=>", SqlKind.ARGUMENT_ASSIGNMENT, 20, true, ReturnTypes.ARG0,
        InferTypes.RETURN_TYPE, OperandTypes.ANY_IGNORE);
  }

  @Override public void unparse(SqlWriter writer, SqlCall call, int leftPrec,
      int rightPrec) {
    // Arguments are held in reverse order to be consistent with base class (AS).
    call.operand(1).unparse(writer, leftPrec, getLeftPrec());
    writer.keyword(getName());
    call.operand(0).unparse(writer, getRightPrec(), rightPrec);
  }

  @Override public boolean argumentMustBeScalar(int ordinal) {
    return false;
  }
}

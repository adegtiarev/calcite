/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlSetOperator.java
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

import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.type.SqlOperandTypeChecker;
import org.apache.calcite.sql.type.SqlOperandTypeInference;
import org.apache.calcite.sql.type.SqlReturnTypeInference;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorScope;

/**
 * SqlSetOperator represents a relational set theory operator (UNION, INTERSECT,
 * MINUS). These are binary operators, but with an extra boolean attribute
 * tacked on for whether to remove duplicates (e.g. UNION ALL does not remove
 * duplicates).
 */
public class SqlSetOperator extends SqlBinaryOperator {
  //~ Instance fields --------------------------------------------------------

  private final boolean all;

  //~ Constructors -----------------------------------------------------------

  public SqlSetOperator(
      String name,
      SqlKind kind,
      int prec,
      boolean all) {
    super(
        name,
        kind,
        prec,
        true,
        ReturnTypes.LEAST_RESTRICTIVE,
        null,
        OperandTypes.SET_OP);
    this.all = all;
  }

  public SqlSetOperator(
      String name,
      SqlKind kind,
      int prec,
      boolean all,
      SqlReturnTypeInference returnTypeInference,
      SqlOperandTypeInference operandTypeInference,
      SqlOperandTypeChecker operandTypeChecker) {
    super(
        name,
        kind,
        prec,
        true,
        returnTypeInference,
        operandTypeInference,
        operandTypeChecker);
    this.all = all;
  }

  //~ Methods ----------------------------------------------------------------

  public boolean isAll() {
    return all;
  }

  public boolean isDistinct() {
    return !all;
  }

  @Override public void validateCall(
      SqlCall call,
      SqlValidator validator,
      SqlValidatorScope scope,
      SqlValidatorScope operandScope) {
    validator.validateQuery(call, operandScope, validator.getUnknownType());
  }
}

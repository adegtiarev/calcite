/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ComparableOperandTypeChecker.java
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

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeComparability;
import org.apache.calcite.sql.SqlCallBinding;
import org.apache.calcite.sql.SqlOperatorBinding;
import org.apache.calcite.sql.validate.implicit.TypeCoercion;

import java.util.Objects;

/**
 * Type checking strategy which verifies that types have the required attributes
 * to be used as arguments to comparison operators.
 */
public class ComparableOperandTypeChecker extends SameOperandTypeChecker {
  //~ Instance fields --------------------------------------------------------

  private final RelDataTypeComparability requiredComparability;
  private final Consistency consistency;

  //~ Constructors -----------------------------------------------------------

  @Deprecated // to be removed before 2.0
  public ComparableOperandTypeChecker(int nOperands,
      RelDataTypeComparability requiredComparability) {
    this(nOperands, requiredComparability, Consistency.NONE);
  }

  public ComparableOperandTypeChecker(int nOperands,
      RelDataTypeComparability requiredComparability, Consistency consistency) {
    super(nOperands);
    this.requiredComparability = requiredComparability;
    this.consistency = Objects.requireNonNull(consistency, "consistency");
  }

  //~ Methods ----------------------------------------------------------------

  @Override public boolean checkOperandTypes(
      SqlCallBinding callBinding,
      boolean throwOnFailure) {
    boolean b = true;
    for (int i = 0; i < nOperands; ++i) {
      RelDataType type = callBinding.getOperandType(i);
      if (!checkType(callBinding, throwOnFailure, type)) {
        b = false;
        break;
      }
    }
    if (b) {
      // Coerce type first.
      if (callBinding.isTypeCoercionEnabled()) {
        TypeCoercion typeCoercion = callBinding.getValidator().getTypeCoercion();
        // For comparison operators, i.e. >, <, =, >=, <=.
        typeCoercion.binaryComparisonCoercion(callBinding);
      }
      b = super.checkOperandTypes(callBinding, false);
    }
    if (!b && throwOnFailure) {
      throw callBinding.newValidationSignatureError();
    }
    return b;
  }

  private boolean checkType(
      SqlCallBinding callBinding,
      boolean throwOnFailure,
      RelDataType type) {
    if (type.getComparability().ordinal()
        < requiredComparability.ordinal()) {
      if (throwOnFailure) {
        throw callBinding.newValidationSignatureError();
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  /**
   * Similar functionality to
   * {@link #checkOperandTypes(SqlCallBinding, boolean)}, but not part of the
   * interface, and cannot throw an error.
   */
  @Override public boolean checkOperandTypes(
      SqlOperatorBinding operatorBinding, SqlCallBinding callBinding) {
    boolean b = true;
    for (int i = 0; i < nOperands; ++i) {
      RelDataType type = callBinding.getOperandType(i);
      if (type.getComparability().ordinal() < requiredComparability.ordinal()) {
        b = false;
        break;
      }
    }
    if (b) {
      b = super.checkOperandTypes(operatorBinding, callBinding);
    }
    return b;
  }

  @Override protected String getTypeName() {
    return "COMPARABLE_TYPE";
  }

  @Override public Consistency getConsistency() {
    return consistency;
  }
}

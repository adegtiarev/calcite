/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: ExplicitOperandTypeChecker.java
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
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.sql.SqlCallBinding;
import org.apache.calcite.sql.SqlOperandCountRange;
import org.apache.calcite.sql.SqlOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Parameter type-checking strategy for Explicit Type.
 */
public class ExplicitOperandTypeChecker implements SqlOperandTypeChecker {
  //~ Methods ----------------------------------------------------------------

  private final RelDataType type;

  public ExplicitOperandTypeChecker(RelDataType type) {
    this.type = Objects.requireNonNull(type, "type");
  }

  @Override public boolean isOptional(int i) {
    return false;
  }

  @Override public boolean checkOperandTypes(
      SqlCallBinding callBinding,
      boolean throwOnFailure) {
    List<SqlTypeFamily> families = new ArrayList<>();

    List<RelDataTypeField> fieldList = type.getFieldList();
    for (int i = 0; i < fieldList.size(); i++) {
      RelDataTypeField field = fieldList.get(i);
      SqlTypeName sqlTypeName = field.getType().getSqlTypeName();
      if (sqlTypeName == SqlTypeName.ROW) {
        if (field.getType().equals(callBinding.getOperandType(i))) {
          families.add(SqlTypeFamily.ANY);
        }
      } else {
        families.add(
            requireNonNull(sqlTypeName.getFamily(),
                () -> "keyType.getSqlTypeName().getFamily() null, type is " + sqlTypeName));
      }
    }
    return OperandTypes.family(families).checkOperandTypes(callBinding, throwOnFailure);
  }

  @Override public SqlOperandCountRange getOperandCountRange() {
    return SqlOperandCountRanges.of(type.getFieldCount());
  }

  @Override public String getAllowedSignatures(SqlOperator op, String opName) {
    return "<TYPE> " + opName + " <TYPE>";
  }

  @Override public Consistency getConsistency() {
    return Consistency.NONE;
  }
}

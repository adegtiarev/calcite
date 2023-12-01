/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: SqlLeadLagAggFunction.java
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

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlOperatorBinding;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.type.SameOperandTypeChecker;
import org.apache.calcite.sql.type.SqlReturnTypeInference;
import org.apache.calcite.sql.type.SqlSingleOperandTypeChecker;
import org.apache.calcite.sql.type.SqlTypeFamily;
import org.apache.calcite.sql.type.SqlTypeTransform;
import org.apache.calcite.sql.type.SqlTypeTransforms;
import org.apache.calcite.util.Optionality;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * <code>LEAD</code> and <code>LAG</code> aggregate functions
 * return the value of given expression evaluated at given offset.
 */
public class SqlLeadLagAggFunction extends SqlAggFunction {
  private static final SqlSingleOperandTypeChecker OPERAND_TYPES =
      OperandTypes.or(
          OperandTypes.ANY,
          OperandTypes.family(SqlTypeFamily.ANY, SqlTypeFamily.NUMERIC),
          OperandTypes.and(
              OperandTypes.family(SqlTypeFamily.ANY, SqlTypeFamily.NUMERIC,
                  SqlTypeFamily.ANY),
              // Arguments 1 and 3 must have same type
              new SameOperandTypeChecker(3) {
                @Override protected List<Integer>
                getOperandList(int operandCount) {
                  return ImmutableList.of(0, 2);
                }
              }));

  private static final SqlReturnTypeInference RETURN_TYPE =
      ReturnTypes.ARG0.andThen(SqlLeadLagAggFunction::transformType);

  public SqlLeadLagAggFunction(SqlKind kind) {
    super(kind.name(),
        null,
        kind,
        RETURN_TYPE,
        null,
        OPERAND_TYPES,
        SqlFunctionCategory.NUMERIC,
        false,
        true,
        Optionality.FORBIDDEN);
    Preconditions.checkArgument(kind == SqlKind.LEAD
        || kind == SqlKind.LAG);
  }

  @Deprecated // to be removed before 2.0
  public SqlLeadLagAggFunction(boolean isLead) {
    this(isLead ? SqlKind.LEAD : SqlKind.LAG);
  }

  // Result is NOT NULL if NOT NULL default value is provided
  private static RelDataType transformType(SqlOperatorBinding binding,
      RelDataType type) {
    SqlTypeTransform transform =
        binding.getOperandCount() < 3 || binding.getOperandType(2).isNullable()
            ? SqlTypeTransforms.FORCE_NULLABLE
            : SqlTypeTransforms.TO_NOT_NULLABLE;
    return transform.transformType(binding, type);
  }

  @Override public boolean allowsFraming() {
    return false;
  }

  @Override public boolean allowsNullTreatment() {
    return true;
  }

}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlFirstLastValueAggFunction.java
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
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Optionality;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * <code>FIRST_VALUE</code> and <code>LAST_VALUE</code> aggregate functions
 * return the first or the last value in a list of values that are input to the
 * function.
 */
public class SqlFirstLastValueAggFunction extends SqlAggFunction {
  //~ Constructors -----------------------------------------------------------

  public SqlFirstLastValueAggFunction(SqlKind kind) {
    super(
        kind.name(),
        null,
        kind,
        ReturnTypes.ARG0_NULLABLE_IF_EMPTY,
        null,
        OperandTypes.ANY,
        SqlFunctionCategory.NUMERIC,
        false,
        true,
        Optionality.FORBIDDEN);
    Preconditions.checkArgument(kind == SqlKind.FIRST_VALUE
        || kind == SqlKind.LAST_VALUE);
  }

  @Deprecated // to be removed before 2.0
  public SqlFirstLastValueAggFunction(boolean firstFlag) {
    this(firstFlag ? SqlKind.FIRST_VALUE : SqlKind.LAST_VALUE);
  }

  //~ Methods ----------------------------------------------------------------

  @SuppressWarnings("deprecation")
  @Override public List<RelDataType> getParameterTypes(RelDataTypeFactory typeFactory) {
    return ImmutableList.of(
        typeFactory.createTypeWithNullability(
            typeFactory.createSqlType(SqlTypeName.ANY), true));
  }

  @SuppressWarnings("deprecation")
  @Override public RelDataType getReturnType(RelDataTypeFactory typeFactory) {
    return typeFactory.createTypeWithNullability(
        typeFactory.createSqlType(SqlTypeName.ANY), true);
  }

  @Override public boolean allowsNullTreatment() {
    return true;
  }
}

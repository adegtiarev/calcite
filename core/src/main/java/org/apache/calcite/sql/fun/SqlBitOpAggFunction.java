/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: SqlBitOpAggFunction.java
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

import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlSplittableAggFunction;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.util.Optionality;

import com.google.common.base.Preconditions;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Definition of the <code>BIT_AND</code> and <code>BIT_OR</code> aggregate functions,
 * returning the bitwise AND/OR of all non-null input values, or null if none.
 *
 * <p>INTEGER and BINARY types are supported:
 * tinyint, smallint, int, bigint, binary, varbinary
 */
public class SqlBitOpAggFunction extends SqlAggFunction {

  //~ Constructors -----------------------------------------------------------

  /** Creates a SqlBitOpAggFunction. */
  public SqlBitOpAggFunction(SqlKind kind) {
    super(kind.name(),
        null,
        kind,
        ReturnTypes.ARG0_NULLABLE_IF_EMPTY,
        null,
        OperandTypes.or(OperandTypes.INTEGER, OperandTypes.BINARY),
        SqlFunctionCategory.NUMERIC,
        false,
        false,
        Optionality.FORBIDDEN);
    Preconditions.checkArgument(kind == SqlKind.BIT_AND
        || kind == SqlKind.BIT_OR
        || kind == SqlKind.BIT_XOR);
  }

  @Override public <T extends Object> @Nullable T unwrap(Class<T> clazz) {
    if (clazz == SqlSplittableAggFunction.class) {
      return clazz.cast(SqlSplittableAggFunction.SelfSplitter.INSTANCE);
    }
    return super.unwrap(clazz);
  }

  @Override public Optionality getDistinctOptionality() {
    final Optionality optionality;

    switch (kind) {
    case BIT_AND:
    case BIT_OR:
      optionality = Optionality.IGNORED;
      break;
    default:
      optionality = Optionality.OPTIONAL;
      break;
    }
    return optionality;
  }
}

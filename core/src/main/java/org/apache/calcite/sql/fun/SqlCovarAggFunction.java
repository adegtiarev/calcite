/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: SqlCovarAggFunction.java
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
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.util.Optionality;

import com.google.common.base.Preconditions;

/**
 * <code>Covar</code> is an aggregator which returns the Covariance of the
 * values which go into it. It has precisely two arguments of numeric type
 * (<code>int</code>, <code>long</code>, <code>float</code>, <code>
 * double</code>), and the result is the same type.
 */
public class SqlCovarAggFunction extends SqlAggFunction {
  //~ Instance fields --------------------------------------------------------

  //~ Constructors -----------------------------------------------------------

  /**
   * Creates a SqlCovarAggFunction.
   */
  public SqlCovarAggFunction(SqlKind kind) {
    super(kind.name(),
        null,
        kind,
        kind == SqlKind.REGR_COUNT ? ReturnTypes.BIGINT : ReturnTypes.COVAR_REGR_FUNCTION,
        null,
        OperandTypes.NUMERIC_NUMERIC,
        SqlFunctionCategory.NUMERIC,
        false,
        false,
        Optionality.FORBIDDEN);
    Preconditions.checkArgument(SqlKind.COVAR_AVG_AGG_FUNCTIONS.contains(kind),
        "unsupported sql kind: " + kind);
  }

  @Deprecated // to be removed before 2.0
  public SqlCovarAggFunction(RelDataType type, Subtype subtype) {
    this(SqlKind.valueOf(subtype.name()));
  }

  //~ Methods ----------------------------------------------------------------

  /**
   * Returns the specific function, e.g. COVAR_POP or COVAR_SAMP.
   *
   * @return Subtype
   */
  @Deprecated // to be removed before 2.0
  public Subtype getSubtype() {
    return Subtype.valueOf(kind.name());
  }

  /**
   * Enum for defining specific types.
   */
  @Deprecated // to be removed before 2.0
  public enum Subtype {
    COVAR_POP,
    COVAR_SAMP,
    REGR_SXX,
    REGR_SYY
  }
}

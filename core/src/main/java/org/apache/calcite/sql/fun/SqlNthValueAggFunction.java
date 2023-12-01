/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: http://www.mycat.org.cn/mycat2-index.html
*    Release: https://github.com/MyCATApache/Mycat2/releases/tag/v1.13-beta-2020-12-24
*    Source File: SqlNthValueAggFunction.java
*    
*    Copyrights:
*      copyright (c) <year>  <name of author>
*      copyright (c) <2020>  <chen junwen>
*      copyright (c) <2020>  <chenjunwen>
*      copyright (c) 2007 free software foundation, inc. <https://fsf.org/>
*      copyright treaty adopted on 20 december 1996, or
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
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.util.Optionality;

/**
 * <code>NTH_VALUE</code> windowed aggregate function
 * returns the value of an expression evaluated at the {@code n}th row of the
 * window frame.
 */
public class SqlNthValueAggFunction extends SqlAggFunction {
  public SqlNthValueAggFunction(SqlKind kind) {
    super(kind.name(), null, kind, ReturnTypes.ARG0_NULLABLE_IF_EMPTY,
        null, OperandTypes.ANY_NUMERIC, SqlFunctionCategory.NUMERIC, false,
        true, Optionality.FORBIDDEN);
  }

  @Override public boolean allowsNullTreatment() {
    return true;
  }
}

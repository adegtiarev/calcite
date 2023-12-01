/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: OracleSqlOperatorTable.java
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

import org.apache.calcite.sql.SqlFunction;
import org.apache.calcite.sql.util.ReflectiveSqlOperatorTable;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Operator table that contains only Oracle-specific functions and operators.
 *
 * @deprecated Use
 * {@link SqlLibraryOperatorTableFactory#getOperatorTable(SqlLibrary...)}
 * instead, passing {@link SqlLibrary#ORACLE} as argument.
 */
@Deprecated // to be removed before 2.0
public class OracleSqlOperatorTable extends ReflectiveSqlOperatorTable {
  //~ Static fields/initializers ---------------------------------------------

  /**
   * The table of contains Oracle-specific operators.
   */
  private static @Nullable OracleSqlOperatorTable instance;

  @Deprecated // to be removed before 2.0
  public static final SqlFunction DECODE = SqlLibraryOperators.DECODE;

  @Deprecated // to be removed before 2.0
  public static final SqlFunction NVL = SqlLibraryOperators.NVL;

  @Deprecated // to be removed before 2.0
  public static final SqlFunction LTRIM = SqlLibraryOperators.LTRIM;

  @Deprecated // to be removed before 2.0
  public static final SqlFunction RTRIM = SqlLibraryOperators.RTRIM;

  @Deprecated // to be removed before 2.0
  public static final SqlFunction SUBSTR = SqlLibraryOperators.SUBSTR_ORACLE;

  @Deprecated // to be removed before 2.0
  public static final SqlFunction GREATEST = SqlLibraryOperators.GREATEST;

  @Deprecated // to be removed before 2.0
  public static final SqlFunction LEAST = SqlLibraryOperators.LEAST;

  @Deprecated // to be removed before 2.0
  public static final SqlFunction TRANSLATE3 = SqlLibraryOperators.TRANSLATE3;

  /**
   * Returns the Oracle operator table, creating it if necessary.
   */
  public static synchronized OracleSqlOperatorTable instance() {
    OracleSqlOperatorTable instance = OracleSqlOperatorTable.instance;
    if (instance == null) {
      // Creates and initializes the standard operator table.
      // Uses two-phase construction, because we can't initialize the
      // table until the constructor of the sub-class has completed.
      instance = new OracleSqlOperatorTable();
      instance.init();
      OracleSqlOperatorTable.instance = instance;
    }
    return instance;
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: AnsiSqlDialect.java
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
package org.apache.calcite.sql.dialect;

import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWriter;

/**
 * A <code>SqlDialect</code> implementation for an unknown ANSI compatible database.
 */
public class AnsiSqlDialect extends SqlDialect {

  public static final Context DEFAULT_CONTEXT = SqlDialect.EMPTY_CONTEXT
      .withDatabaseProduct(SqlDialect.DatabaseProduct.UNKNOWN)
      .withIdentifierQuoteString("`");

  /**
   * A dialect useful for generating generic SQL. If you need to do something
   * database-specific like quoting identifiers, don't rely on this dialect to
   * do what you want.
   */
  public static final SqlDialect DEFAULT = new AnsiSqlDialect(DEFAULT_CONTEXT);

  /** Creates an AnsiSqlDialect. */
  public AnsiSqlDialect(Context context) {
    super(context);
  }

  /** Converts table scan hints.*/
  @Override public void unparseTableScanHints(SqlWriter writer,
      SqlNodeList hints, int leftPrec, int rightPrec) {
    writer.newlineAndIndent();
    writer.keyword("/*+");
    hints.unparse(writer, 0, 0);
    writer.keyword("*/");
  }

}

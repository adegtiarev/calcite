/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ChainedSqlOperatorTable.java
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
package org.apache.calcite.sql.util;

import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.SqlSyntax;
import org.apache.calcite.sql.validate.SqlNameMatcher;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * ChainedSqlOperatorTable implements the {@link SqlOperatorTable} interface by
 * chaining together any number of underlying operator table instances.
 *
 * <p>To create, call {@link SqlOperatorTables#chain}.
 */
public class ChainedSqlOperatorTable implements SqlOperatorTable {
  //~ Instance fields --------------------------------------------------------

  protected final List<SqlOperatorTable> tableList;

  //~ Constructors -----------------------------------------------------------

  @Deprecated // to be removed before 2.0
  public ChainedSqlOperatorTable(List<SqlOperatorTable> tableList) {
    this(ImmutableList.copyOf(tableList));
  }

  /** Internal constructor; call {@link SqlOperatorTables#chain}. */
  protected ChainedSqlOperatorTable(ImmutableList<SqlOperatorTable> tableList) {
    this.tableList = ImmutableList.copyOf(tableList);
  }

  //~ Methods ----------------------------------------------------------------

  @Deprecated // to be removed before 2.0
  public void add(SqlOperatorTable table) {
    if (!tableList.contains(table)) {
      tableList.add(table);
    }
  }

  @Override public void lookupOperatorOverloads(SqlIdentifier opName,
      @Nullable SqlFunctionCategory category, SqlSyntax syntax,
      List<SqlOperator> operatorList, SqlNameMatcher nameMatcher) {
    for (SqlOperatorTable table : tableList) {
      table.lookupOperatorOverloads(opName, category, syntax, operatorList,
          nameMatcher);
    }
  }

  @Override public List<SqlOperator> getOperatorList() {
    List<SqlOperator> list = new ArrayList<>();
    for (SqlOperatorTable table : tableList) {
      list.addAll(table.getOperatorList());
    }
    return list;
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Table.java
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
package org.apache.calcite.schema;

import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Table.
 *
 * <p>The typical way for a table to be created is when Calcite interrogates a
 * user-defined schema in order to validate names appearing in a SQL query.
 * Calcite finds the schema by calling {@link Schema#getSubSchema(String)} on
 * the connection's root schema, then gets a table by calling
 * {@link Schema#getTable(String)}.</p>
 *
 * <p>Note that a table does not know its name. It is in fact possible for
 * a table to be used more than once, perhaps under multiple names or under
 * multiple schemas. (Compare with the
 * <a href="http://en.wikipedia.org/wiki/Inode">i-node</a> concept in the UNIX
 * filesystem.)</p>
 *
 * <p>A particular table instance may also implement {@link Wrapper},
 * to give access to sub-objects.
 *
 * @see TableMacro
 */
public interface Table {
  /** Returns this table's row type.
   *
   * <p>This is a struct type whose
   * fields describe the names and types of the columns in this table.</p>
   *
   * <p>The implementer must use the type factory provided. This ensures that
   * the type is converted into a canonical form; other equal types in the same
   * query will use the same object.</p>
   *
   * @param typeFactory Type factory with which to create the type
   * @return Row type
   */
  RelDataType getRowType(RelDataTypeFactory typeFactory);

  /** Returns a provider of statistics about this table. */
  Statistic getStatistic();

  /** Type of table. */
  Schema.TableType getJdbcTableType();

  /**
   * Determines whether the given {@code column} has been rolled up.
   * */
  boolean isRolledUp(String column);

  /**
   * Determines whether the given rolled up column can be used inside the given aggregate function.
   * You can assume that {@code isRolledUp(column)} is {@code true}.
   *
   * @param column The column name for which {@code isRolledUp} is true
   * @param call The aggregate call
   * @param parent Parent node of {@code call} in the {@link SqlNode} tree
   * @param config Config settings. May be null
   * @return true iff the given aggregate call is valid
   */
  boolean rolledUpColumnValidInsideAgg(String column, SqlCall call,
      @Nullable SqlNode parent, @Nullable CalciteConnectionConfig config);
}

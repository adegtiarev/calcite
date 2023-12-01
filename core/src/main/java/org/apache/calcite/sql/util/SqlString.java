/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlString.java
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

import org.apache.calcite.sql.SqlDialect;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

/**
 * String that represents a kocher SQL statement, expression, or fragment.
 *
 * <p>A SqlString just contains a regular Java string, but the SqlString wrapper
 * indicates that the string has been created carefully guarding against all SQL
 * dialect and injection issues.
 *
 * <p>The easiest way to do build a SqlString is to use a {@link SqlBuilder}.
 */
public class SqlString {
  private final String sql;
  private SqlDialect dialect;
  private @Nullable ImmutableList<Integer> dynamicParameters;

  /**
   * Creates a SqlString.
   */
  public SqlString(SqlDialect dialect, String sql) {
    this(dialect, sql, ImmutableList.of());
  }

  /**
   * Creates a SqlString. The SQL might contain dynamic parameters, dynamicParameters
   * designate the order of the parameters.
   *
   * @param sql text
   * @param dynamicParameters indices
   */
  public SqlString(SqlDialect dialect, String sql,
      @Nullable ImmutableList<Integer> dynamicParameters) {
    this.dialect = dialect;
    this.sql = sql;
    this.dynamicParameters = dynamicParameters;
    assert sql != null : "sql must be NOT null";
    assert dialect != null : "dialect must be NOT null";
  }

  @Override public int hashCode() {
    return sql.hashCode();
  }

  @Override public boolean equals(@Nullable Object obj) {
    return obj == this
        || obj instanceof SqlString
        && sql.equals(((SqlString) obj).sql);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Returns the SQL string.
   *
   * @return SQL string
   * @see #getSql()
   */
  @Override public String toString() {
    return sql;
  }

  /**
   * Returns the SQL string.
   *
   * @return SQL string
   */
  public String getSql() {
    return sql;
  }

  /**
   * Returns indices of dynamic parameters.
   *
   * @return indices of dynamic parameters
   */
  @Pure
  public @Nullable ImmutableList<Integer> getDynamicParameters() {
    return dynamicParameters;
  }

  /**
   * Returns the dialect.
   */
  public SqlDialect getDialect() {
    return dialect;
  }
}

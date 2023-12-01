/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: AbstractTableQueryable.java
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
package org.apache.calcite.schema.impl;

import org.apache.calcite.linq4j.AbstractQueryable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.schema.QueryableTable;
import org.apache.calcite.schema.SchemaPlus;

import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Abstract implementation of {@link org.apache.calcite.linq4j.Queryable} for
 * {@link QueryableTable}.
 *
 * <p>Not to be confused with
 * {@link org.apache.calcite.adapter.java.AbstractQueryableTable}.</p>
 *
 * @param <T> element type
 */
public abstract class AbstractTableQueryable<T> extends AbstractQueryable<T> {
  public final QueryProvider queryProvider;
  public final SchemaPlus schema;
  public final QueryableTable table;
  public final String tableName;

  protected AbstractTableQueryable(QueryProvider queryProvider,
      SchemaPlus schema, QueryableTable table, String tableName) {
    this.queryProvider = queryProvider;
    this.schema = schema;
    this.table = table;
    this.tableName = tableName;
  }

  @Override public Expression getExpression() {
    return table.getExpression(schema, tableName, Queryable.class);
  }

  @Override public QueryProvider getProvider() {
    return queryProvider;
  }

  @Override public Type getElementType() {
    return table.getElementType();
  }

  @Override public Iterator<T> iterator() {
    return Linq4j.enumeratorIterator(enumerator());
  }
}

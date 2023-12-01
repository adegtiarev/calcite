/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SplunkTable.java
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
package org.apache.calcite.adapter.splunk;

import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.schema.impl.AbstractTableQueryable;

import java.util.List;

/**
 * Table based on Splunk.
 */
class SplunkTable extends AbstractQueryableTable implements TranslatableTable {
  public static final SplunkTable INSTANCE = new SplunkTable();

  private SplunkTable() {
    super(Object[].class);
  }

  @Override public String toString() {
    return "SplunkTable";
  }

  @Override public RelDataType getRowType(RelDataTypeFactory typeFactory) {
    RelDataType stringType =
        ((JavaTypeFactory) typeFactory).createType(String.class);
    return typeFactory.builder()
        .add("source", stringType)
        .add("sourcetype", stringType)
        .add("_extra", stringType)
        .build();
  }

  @Override public <T> Queryable<T> asQueryable(QueryProvider queryProvider,
      SchemaPlus schema, String tableName) {
    return new SplunkTableQueryable<>(queryProvider, schema, this, tableName);
  }

  @Override public RelNode toRel(
      RelOptTable.ToRelContext context,
      RelOptTable relOptTable) {
    return new SplunkTableScan(
        context.getCluster(),
        relOptTable,
        this,
        "search",
        null,
        null,
        relOptTable.getRowType().getFieldNames());
  }

  /** Implementation of {@link Queryable} backed by a {@link SplunkTable}.
   * Generated code uses this get a Splunk connection for executing arbitrary
   * Splunk queries.
   *
   * @param <T> element type */
  public static class SplunkTableQueryable<T>
      extends AbstractTableQueryable<T> {
    SplunkTableQueryable(QueryProvider queryProvider, SchemaPlus schema,
        SplunkTable table, String tableName) {
      super(queryProvider, schema, table, tableName);
    }

    @Override public Enumerator<T> enumerator() {
      final SplunkQuery<T> query = createQuery("search", null, null, null);
      return query.enumerator();
    }

    public SplunkQuery<T> createQuery(String search, String earliest,
        String latest, List<String> fieldList) {
      final SplunkSchema splunkSchema = schema.unwrap(SplunkSchema.class);
      return new SplunkQuery<>(splunkSchema.splunkConnection, search,
          earliest, latest, fieldList);
    }
  }
}

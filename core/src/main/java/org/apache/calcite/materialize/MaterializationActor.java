/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MaterializationActor.java
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
package org.apache.calcite.materialize;

import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.rel.type.RelDataType;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Actor that manages the state of materializations in the system.
 */
class MaterializationActor {
  // Not an actor yet -- TODO make members private and add request/response
  // queues

  final Map<MaterializationKey, Materialization> keyMap = new HashMap<>();

  final Map<QueryKey, MaterializationKey> keyBySql = new HashMap<>();

  final Map<TileKey, MaterializationKey> keyByTile = new HashMap<>();

  /** Tiles grouped by dimensionality. We use a
   *  {@link TileKey} with no measures to represent a
   *  dimensionality. */
  final Multimap<TileKey, TileKey> tilesByDimensionality =
      HashMultimap.create();

  /** A query materialized in a table, so that reading from the table gives the
   * same results as executing the query. */
  static class Materialization {
    final MaterializationKey key;
    final CalciteSchema rootSchema;
    CalciteSchema.@Nullable TableEntry materializedTable;
    final String sql;
    final RelDataType rowType;
    final @Nullable List<String> viewSchemaPath;

    /** Creates a materialization.
     *
     * @param key  Unique identifier of this materialization
     * @param materializedTable Table that currently materializes the query.
     *                          That is, executing "select * from table" will
     *                          give the same results as executing the query.
     *                          May be null when the materialization is created;
     *                          materialization service will change the value as
     * @param sql  Query that is materialized
     * @param rowType Row type
     */
    Materialization(MaterializationKey key,
        CalciteSchema rootSchema,
        CalciteSchema.@Nullable TableEntry materializedTable,
        String sql,
        RelDataType rowType,
        @Nullable List<String> viewSchemaPath) {
      this.key = key;
      this.rootSchema = Objects.requireNonNull(rootSchema, "rootSchema");
      Preconditions.checkArgument(rootSchema.isRoot(), "must be root schema");
      this.materializedTable = materializedTable; // may be null
      this.sql = sql;
      this.rowType = rowType;
      this.viewSchemaPath = viewSchemaPath;
    }
  }

  /** A materialization can be re-used if it is the same SQL, on the same
   * schema, with the same path for resolving functions. */
  static class QueryKey {
    final String sql;
    final CalciteSchema schema;
    final @Nullable List<String> path;

    QueryKey(String sql, CalciteSchema schema, @Nullable List<String> path) {
      this.sql = sql;
      this.schema = schema;
      this.path = path;
    }

    @Override public boolean equals(@Nullable Object obj) {
      return obj == this
          || obj instanceof QueryKey
          && sql.equals(((QueryKey) obj).sql)
          && schema.equals(((QueryKey) obj).schema)
          && Objects.equals(path, ((QueryKey) obj).path);
    }

    @Override public int hashCode() {
      return Objects.hash(sql, schema, path);
    }
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: CachingSqlStatisticProvider.java
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
package org.apache.calcite.statistic;

import org.apache.calcite.materialize.SqlStatisticProvider;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.util.ImmutableIntList;
import org.apache.calcite.util.Util;

import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of {@link SqlStatisticProvider} that reads and writes a
 * cache.
 */
public class CachingSqlStatisticProvider implements SqlStatisticProvider {
  private final SqlStatisticProvider provider;
  private final Cache<List, Object> cache;

  public CachingSqlStatisticProvider(SqlStatisticProvider provider,
      Cache<List, Object> cache) {
    super();
    this.provider = provider;
    this.cache = cache;
  }

  @Override public double tableCardinality(RelOptTable table) {
    try {
      final ImmutableList<Object> key =
          ImmutableList.of("tableCardinality",
              table.getQualifiedName());
      return (Double) cache.get(key,
          () -> provider.tableCardinality(table));
    } catch (UncheckedExecutionException | ExecutionException e) {
      throw Util.throwAsRuntime(Util.causeOrSelf(e));
    }
  }

  @Override public boolean isForeignKey(RelOptTable fromTable, List<Integer> fromColumns,
      RelOptTable toTable, List<Integer> toColumns) {
    try {
      final ImmutableList<Object> key =
          ImmutableList.of("isForeignKey",
              fromTable.getQualifiedName(),
              ImmutableIntList.copyOf(fromColumns),
              toTable.getQualifiedName(),
              ImmutableIntList.copyOf(toColumns));
      return (Boolean) cache.get(key,
          () -> provider.isForeignKey(fromTable, fromColumns, toTable,
              toColumns));
    } catch (UncheckedExecutionException | ExecutionException e) {
      throw Util.throwAsRuntime(Util.causeOrSelf(e));
    }
  }

  @Override public boolean isKey(RelOptTable table, List<Integer> columns) {
    try {
      final ImmutableList<Object> key =
          ImmutableList.of("isKey", table.getQualifiedName(),
              ImmutableIntList.copyOf(columns));
      return (Boolean) cache.get(key, () -> provider.isKey(table, columns));
    } catch (UncheckedExecutionException | ExecutionException e) {
      throw Util.throwAsRuntime(Util.causeOrSelf(e));
    }
  }
}

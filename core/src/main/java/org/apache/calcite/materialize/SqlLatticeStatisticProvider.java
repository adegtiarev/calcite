/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlLatticeStatisticProvider.java
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

import org.apache.calcite.DataContexts;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.MaterializedViewTable;
import org.apache.calcite.util.ImmutableBitSet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link LatticeStatisticProvider} that gets statistics by
 * executing "SELECT COUNT(DISTINCT ...) ..." SQL queries.
 */
class SqlLatticeStatisticProvider implements LatticeStatisticProvider {
  static final Factory FACTORY = SqlLatticeStatisticProvider::new;

  static final Factory CACHED_FACTORY = lattice -> {
    LatticeStatisticProvider provider = FACTORY.apply(lattice);
    return new CachingLatticeStatisticProvider(lattice, provider);
  };

  private final Lattice lattice;

  /** Creates a SqlLatticeStatisticProvider. */
  private SqlLatticeStatisticProvider(Lattice lattice) {
    this.lattice = requireNonNull(lattice, "lattice");
  }

  @Override public double cardinality(List<Lattice.Column> columns) {
    final List<Double> counts = new ArrayList<>();
    for (Lattice.Column column : columns) {
      counts.add(cardinality(lattice, column));
    }
    return (int) Lattice.getRowCount(lattice.getFactRowCount(), counts);
  }

  private static double cardinality(Lattice lattice, Lattice.Column column) {
    final String sql = lattice.countSql(ImmutableBitSet.of(column.ordinal));
    final Table table =
        new MaterializationService.DefaultTableFactory()
            .createTable(lattice.rootSchema, sql, ImmutableList.of());
    final @Nullable Object[] values =
        Iterables.getOnlyElement(
            ((ScannableTable) table).scan(
                DataContexts.of(MaterializedViewTable.MATERIALIZATION_CONNECTION,
                    lattice.rootSchema.plus())));
    Number value = (Number) values[0];
    requireNonNull(value, () -> "count(*) produced null in " + sql);
    return value.doubleValue();
  }
}

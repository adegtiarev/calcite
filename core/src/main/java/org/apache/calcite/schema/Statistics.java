/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Statistics.java
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

import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelReferentialConstraint;
import org.apache.calcite.util.ImmutableBitSet;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Utility functions regarding {@link Statistic}.
 */
public class Statistics {
  private Statistics() {
  }

  /** Returns a {@link Statistic} that knows nothing about a table. */
  public static final Statistic UNKNOWN =
      new Statistic() {
      };

  /** Returns a statistic with a given set of referential constraints. */
  public static Statistic of(@Nullable List<RelReferentialConstraint> referentialConstraints) {
    return of(null, null,
        referentialConstraints, null);
  }

  /** Returns a statistic with a given row count and set of unique keys. */
  public static Statistic of(final double rowCount,
      final @Nullable List<ImmutableBitSet> keys) {
    return of(rowCount, keys, null,
        null);
  }

  /** Returns a statistic with a given row count, set of unique keys,
   * and collations. */
  public static Statistic of(final double rowCount,
      final @Nullable List<ImmutableBitSet> keys,
      final @Nullable List<RelCollation> collations) {
    return of(rowCount, keys, null, collations);
  }

  /** Returns a statistic with a given row count, set of unique keys,
   * referential constraints, and collations. */
  public static Statistic of(final @Nullable Double rowCount,
      final @Nullable List<ImmutableBitSet> keys,
      final @Nullable List<RelReferentialConstraint> referentialConstraints,
      final @Nullable List<RelCollation> collations) {
    List<ImmutableBitSet> keysCopy = keys == null ? ImmutableList.of() : ImmutableList.copyOf(keys);
    List<RelReferentialConstraint> referentialConstraintsCopy =
        referentialConstraints == null ? null : ImmutableList.copyOf(referentialConstraints);
    List<RelCollation> collationsCopy =
        collations == null ? null : ImmutableList.copyOf(collations);

    return new Statistic() {
      @Override public @Nullable Double getRowCount() {
        return rowCount;
      }

      @Override public boolean isKey(ImmutableBitSet columns) {
        for (ImmutableBitSet key : keysCopy) {
          if (columns.contains(key)) {
            return true;
          }
        }
        return false;
      }

      @Override public @Nullable List<ImmutableBitSet> getKeys() {
        return keysCopy;
      }

      @Override public @Nullable List<RelReferentialConstraint> getReferentialConstraints() {
        return referentialConstraintsCopy;
      }

      @Override public @Nullable List<RelCollation> getCollations() {
        return collationsCopy;
      }
    };
  }
}

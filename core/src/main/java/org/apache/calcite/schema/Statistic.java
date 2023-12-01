/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Statistic.java
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
import org.apache.calcite.rel.RelDistribution;
import org.apache.calcite.rel.RelReferentialConstraint;
import org.apache.calcite.util.ImmutableBitSet;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Statistics about a {@link Table}.
 *
 * <p>Each of the methods may return {@code null} meaning "not known".</p>
 *
 * @see Statistics
 */
public interface Statistic {
  /** Returns the approximate number of rows in the table. */
  default @Nullable Double getRowCount() {
    return null;
  }

  /** Returns whether the given set of columns is a unique key, or a superset
   * of a unique key, of the table.
   */
  default boolean isKey(ImmutableBitSet columns) {
    return false;
  }

  /** Returns a list of unique keys, or null if no key exist. */
  default @Nullable List<ImmutableBitSet> getKeys() {
    return null;
  }

  /** Returns the collection of referential constraints (foreign-keys)
   * for this table. */
  default @Nullable List<RelReferentialConstraint> getReferentialConstraints() {
    return null;
  }

  /** Returns the collections of columns on which this table is sorted. */
  default @Nullable List<RelCollation> getCollations() {
    return null;
  }

  /** Returns the distribution of the data in this table. */
  default @Nullable RelDistribution getDistribution()  {
    return null;
  }
}

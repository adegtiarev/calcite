/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: CassandraTableScan.java
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
package org.apache.calcite.adapter.cassandra;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.type.RelDataType;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Relational expression representing a scan of a Cassandra collection.
 */
public class CassandraTableScan extends TableScan implements CassandraRel {
  final CassandraTable cassandraTable;
  final @Nullable RelDataType projectRowType;

  /**
   * Creates a CassandraTableScan.
   *
   * @param cluster        Cluster
   * @param traitSet       Traits
   * @param table          Table
   * @param cassandraTable Cassandra table
   * @param projectRowType Fields and types to project; null to project raw row
   */
  protected CassandraTableScan(RelOptCluster cluster, RelTraitSet traitSet,
      RelOptTable table, CassandraTable cassandraTable, @Nullable RelDataType projectRowType) {
    super(cluster, traitSet, ImmutableList.of(), table);
    this.cassandraTable = cassandraTable;
    this.projectRowType = projectRowType;

    assert getConvention() == CassandraRel.CONVENTION;
  }

  @Override public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
    assert inputs.isEmpty();
    return this;
  }

  @Override public RelDataType deriveRowType() {
    return projectRowType != null ? projectRowType : super.deriveRowType();
  }

  @Override public void register(RelOptPlanner planner) {
    planner.addRule(CassandraRules.TO_ENUMERABLE);
    for (RelOptRule rule : CassandraRules.RULES) {
      planner.addRule(rule);
    }
  }

  @Override public void implement(Implementor implementor) {
    implementor.cassandraTable = cassandraTable;
    implementor.table = table;
  }
}

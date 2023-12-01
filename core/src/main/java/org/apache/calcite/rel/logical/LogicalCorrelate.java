/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.29.0-rc0
*    Source File: LogicalCorrelate.java
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
package org.apache.calcite.rel.logical;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelInput;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelShuttle;
import org.apache.calcite.rel.core.Correlate;
import org.apache.calcite.rel.core.CorrelationId;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.util.ImmutableBitSet;

import static java.util.Objects.requireNonNull;

/**
 * A relational operator that performs nested-loop joins.
 *
 * <p>It behaves like a kind of {@link org.apache.calcite.rel.core.Join},
 * but works by setting variables in its environment and restarting its
 * right-hand input.
 *
 * <p>A LogicalCorrelate is used to represent a correlated query. One
 * implementation strategy is to de-correlate the expression.
 *
 * @see org.apache.calcite.rel.core.CorrelationId
 */
public final class LogicalCorrelate extends Correlate {
  //~ Instance fields --------------------------------------------------------

  //~ Constructors -----------------------------------------------------------

  /**
   * Creates a LogicalCorrelate.
   * @param cluster      cluster this relational expression belongs to
   * @param left         left input relational expression
   * @param right        right input relational expression
   * @param correlationId variable name for the row of left input
   * @param requiredColumns Required columns
   * @param joinType     join type
   */
  public LogicalCorrelate(
      RelOptCluster cluster,
      RelTraitSet traitSet,
      RelNode left,
      RelNode right,
      CorrelationId correlationId,
      ImmutableBitSet requiredColumns,
      JoinRelType joinType) {
    super(
        cluster,
        traitSet,
        left,
        right,
        correlationId,
        requiredColumns,
        joinType);
  }

  /**
   * Creates a LogicalCorrelate by parsing serialized output.
   */
  public LogicalCorrelate(RelInput input) {
    this(input.getCluster(), input.getTraitSet(), input.getInputs().get(0),
        input.getInputs().get(1),
        new CorrelationId(
            (Integer) requireNonNull(input.get("correlation"), "correlation")),
        input.getBitSet("requiredColumns"),
        requireNonNull(input.getEnum("joinType", JoinRelType.class), "joinType"));
  }

  /** Creates a LogicalCorrelate. */
  public static LogicalCorrelate create(RelNode left, RelNode right,
      CorrelationId correlationId, ImmutableBitSet requiredColumns,
      JoinRelType joinType) {
    final RelOptCluster cluster = left.getCluster();
    final RelTraitSet traitSet = cluster.traitSetOf(Convention.NONE);
    return new LogicalCorrelate(cluster, traitSet, left, right, correlationId,
        requiredColumns, joinType);
  }

  //~ Methods ----------------------------------------------------------------

  @Override public LogicalCorrelate copy(RelTraitSet traitSet,
      RelNode left, RelNode right, CorrelationId correlationId,
      ImmutableBitSet requiredColumns, JoinRelType joinType) {
    assert traitSet.containsIfApplicable(Convention.NONE);
    return new LogicalCorrelate(getCluster(), traitSet, left, right,
        correlationId, requiredColumns, joinType);
  }

  @Override public RelNode accept(RelShuttle shuttle) {
    return shuttle.visit(this);
  }
}

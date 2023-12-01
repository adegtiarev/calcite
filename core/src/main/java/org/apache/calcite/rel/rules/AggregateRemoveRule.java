/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: AggregateRemoveRule.java
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
package org.apache.calcite.rel.rules;

import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelRule;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.rel.logical.LogicalAggregate;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.runtime.SqlFunctions;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlSplittableAggFunction;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelBuilderFactory;

import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Planner rule that removes
 * a {@link org.apache.calcite.rel.core.Aggregate}
 * if it computes no aggregate functions
 * (that is, it is implementing {@code SELECT DISTINCT}),
 * or all the aggregate functions are splittable,
 * and the underlying relational expression is already distinct.
 *
 * @see CoreRules#AGGREGATE_REMOVE
 */
@Value.Enclosing
public class AggregateRemoveRule
    extends RelRule<AggregateRemoveRule.Config>
    implements SubstitutionRule {

  /** Creates an AggregateRemoveRule. */
  protected AggregateRemoveRule(Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public AggregateRemoveRule(Class<? extends Aggregate> aggregateClass) {
    this(aggregateClass, RelFactories.LOGICAL_BUILDER);
  }

  @Deprecated // to be removed before 2.0
  public AggregateRemoveRule(Class<? extends Aggregate> aggregateClass,
      RelBuilderFactory relBuilderFactory) {
    this(Config.DEFAULT
        .withRelBuilderFactory(relBuilderFactory)
        .as(Config.class)
        .withOperandFor(aggregateClass));
  }

  private static boolean isAggregateSupported(Aggregate aggregate) {
    if (aggregate.getGroupType() != Aggregate.Group.SIMPLE
        || aggregate.getGroupCount() == 0) {
      return false;
    }
    // If any aggregate functions do not support splitting, bail out.
    for (AggregateCall aggregateCall : aggregate.getAggCallList()) {
      if (aggregateCall.filterArg >= 0
          || !aggregateCall.getAggregation()
              .maybeUnwrap(SqlSplittableAggFunction.class).isPresent()) {
        return false;
      }
    }
    return true;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void onMatch(RelOptRuleCall call) {
    final Aggregate aggregate = call.rel(0);
    final RelNode input = aggregate.getInput();
    final RelMetadataQuery mq = call.getMetadataQuery();
    if (!SqlFunctions.isTrue(mq.areColumnsUnique(input, aggregate.getGroupSet()))) {
      return;
    }

    final RelBuilder relBuilder = call.builder();
    final RexBuilder rexBuilder = relBuilder.getRexBuilder();
    final List<RexNode> projects = new ArrayList<>();
    for (AggregateCall aggCall : aggregate.getAggCallList()) {
      final SqlAggFunction aggregation = aggCall.getAggregation();
      if (aggregation.getKind() == SqlKind.SUM0) {
        // Bail out for SUM0 to avoid potential infinite rule matching,
        // because it may be generated by transforming SUM aggregate
        // function to SUM0 and COUNT.
        return;
      }
      final SqlSplittableAggFunction splitter =
          aggregation.unwrapOrThrow(SqlSplittableAggFunction.class);
      final RexNode singleton =
          splitter.singleton(rexBuilder, input.getRowType(), aggCall);
      final RexNode cast =
          rexBuilder.ensureType(aggCall.type, singleton, false);
      projects.add(cast);
    }

    final RelNode newInput = convert(input, aggregate.getTraitSet().simplify());
    relBuilder.push(newInput);
    if (!projects.isEmpty()) {
      projects.addAll(0, relBuilder.fields(aggregate.getGroupSet()));
      relBuilder.project(projects);
    } else if (newInput.getRowType().getFieldCount()
        > aggregate.getRowType().getFieldCount()) {
      // If aggregate was projecting a subset of columns, and there were no
      // aggregate functions, add a project for the same effect.
      relBuilder.project(relBuilder.fields(aggregate.getGroupSet()));
    }
    call.getPlanner().prune(aggregate);
    call.transformTo(relBuilder.build());
  }

  /** Rule configuration. */
  @Value.Immutable
  public interface Config extends RelRule.Config {
    Config DEFAULT = ImmutableAggregateRemoveRule.Config.of()
        .withRelBuilderFactory(RelFactories.LOGICAL_BUILDER)
        .withOperandFor(LogicalAggregate.class);

    @Override default AggregateRemoveRule toRule() {
      return new AggregateRemoveRule(this);
    }

    /** Defines an operand tree for the given classes. */
    default Config withOperandFor(Class<? extends Aggregate> aggregateClass) {
      return withOperandSupplier(b ->
          b.operand(aggregateClass)
              .predicate(AggregateRemoveRule::isAggregateSupported)
              .anyInputs())
          .as(Config.class);
    }
  }
}

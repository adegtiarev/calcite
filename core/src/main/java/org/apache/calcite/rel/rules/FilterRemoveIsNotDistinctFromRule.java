/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: FilterRemoveIsNotDistinctFromRule.java
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
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.RelRule;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexShuttle;
import org.apache.calcite.rex.RexUtil;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelBuilderFactory;

import org.immutables.value.Value;

/**
 * Planner rule that replaces {@code IS NOT DISTINCT FROM}
 * in a {@link Filter} with logically equivalent operations.
 *
 * @see org.apache.calcite.sql.fun.SqlStdOperatorTable#IS_NOT_DISTINCT_FROM
 * @see CoreRules#FILTER_EXPAND_IS_NOT_DISTINCT_FROM
 * @see RelBuilder#isDistinctFrom
 * @see RelBuilder#isNotDistinctFrom
 */
@Value.Enclosing
public final class FilterRemoveIsNotDistinctFromRule
    extends RelRule<FilterRemoveIsNotDistinctFromRule.Config>
    implements TransformationRule {

  /** Creates a FilterRemoveIsNotDistinctFromRule. */
  FilterRemoveIsNotDistinctFromRule(Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public FilterRemoveIsNotDistinctFromRule(
      RelBuilderFactory relBuilderFactory) {
    this(Config.DEFAULT.withRelBuilderFactory(relBuilderFactory)
        .as(Config.class));
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void onMatch(RelOptRuleCall call) {
    Filter oldFilter = call.rel(0);
    RexNode oldFilterCond = oldFilter.getCondition();

    if (RexUtil.findOperatorCall(
        SqlStdOperatorTable.IS_NOT_DISTINCT_FROM,
        oldFilterCond)
        == null) {
      // no longer contains isNotDistinctFromOperator
      return;
    }

    // Now replace all the "a isNotDistinctFrom b"
    // with the RexNode given by RelOptUtil.isDistinctFrom() method

    RemoveIsNotDistinctFromRexShuttle rewriteShuttle =
        new RemoveIsNotDistinctFromRexShuttle(
            oldFilter.getCluster().getRexBuilder());

    final RelBuilder relBuilder = call.builder();
    final RelNode newFilterRel = relBuilder
        .push(oldFilter.getInput())
        .filter(oldFilterCond.accept(rewriteShuttle))
        .build();

    call.transformTo(newFilterRel);
  }

  //~ Inner Classes ----------------------------------------------------------

  /** Shuttle that removes 'x IS NOT DISTINCT FROM y' and converts it
   * to 'CASE WHEN x IS NULL THEN y IS NULL WHEN y IS NULL THEN x IS
   * NULL ELSE x = y END'. */
  private static class RemoveIsNotDistinctFromRexShuttle extends RexShuttle {
    final RexBuilder rexBuilder;

    RemoveIsNotDistinctFromRexShuttle(
        RexBuilder rexBuilder) {
      this.rexBuilder = rexBuilder;
    }

    @Override public RexNode visitCall(RexCall call) {
      RexNode newCall = super.visitCall(call);

      if (call.getOperator()
          == SqlStdOperatorTable.IS_NOT_DISTINCT_FROM) {
        RexCall tmpCall = (RexCall) newCall;
        newCall =
            RelOptUtil.isDistinctFrom(
                rexBuilder,
                tmpCall.operands.get(0),
                tmpCall.operands.get(1),
                true);
      }
      return newCall;
    }
  }

  /** Rule configuration. */
  @Value.Immutable
  public interface Config extends RelRule.Config {
    Config DEFAULT = ImmutableFilterRemoveIsNotDistinctFromRule.Config.of()
        .withOperandSupplier(b -> b.operand(Filter.class).anyInputs());

    @Override default FilterRemoveIsNotDistinctFromRule toRule() {
      return new FilterRemoveIsNotDistinctFromRule(this);
    }
  }
}

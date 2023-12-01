/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SemiJoinRemoveRule.java
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
import org.apache.calcite.rel.core.Join;
import org.apache.calcite.rel.logical.LogicalJoin;
import org.apache.calcite.tools.RelBuilderFactory;

import org.immutables.value.Value;

/**
 * Planner rule that removes a {@link Join#isSemiJoin semi-join} from a join
 * tree.
 *
 * <p>It is invoked after attempts have been made to convert a SemiJoin to an
 * indexed scan on a join factor have failed. Namely, if the join factor does
 * not reduce to a single table that can be scanned using an index.
 *
 * <p>It should only be enabled if all SemiJoins in the plan are advisory; that
 * is, they can be safely dropped without affecting the semantics of the query.
 *
 * @see CoreRules#SEMI_JOIN_REMOVE
 */
@Value.Enclosing
public class SemiJoinRemoveRule
    extends RelRule<SemiJoinRemoveRule.Config>
    implements TransformationRule {

  /** Creates a SemiJoinRemoveRule. */
  protected SemiJoinRemoveRule(Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public SemiJoinRemoveRule(RelBuilderFactory relBuilderFactory) {
    this(Config.DEFAULT.withRelBuilderFactory(relBuilderFactory)
        .as(Config.class));
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void onMatch(RelOptRuleCall call) {
    call.transformTo(call.rel(0).getInput(0));
  }

  /** Rule configuration. */
  @Value.Immutable
  public interface Config extends RelRule.Config {
    Config DEFAULT = ImmutableSemiJoinRemoveRule.Config.of()
        .withOperandFor(LogicalJoin.class);

    @Override default SemiJoinRemoveRule toRule() {
      return new SemiJoinRemoveRule(this);
    }

    /** Defines an operand tree for the given classes. */
    default Config withOperandFor(Class<? extends Join> joinClass) {
      return withOperandSupplier(b ->
          b.operand(joinClass).predicate(Join::isSemiJoin).anyInputs())
          .as(Config.class);
    }
  }
}

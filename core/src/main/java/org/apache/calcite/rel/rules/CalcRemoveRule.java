/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: CalcRemoveRule.java
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
import org.apache.calcite.rel.core.Calc;
import org.apache.calcite.rel.logical.LogicalCalc;
import org.apache.calcite.tools.RelBuilderFactory;

import org.immutables.value.Value;

/**
 * Planner rule that removes a trivial
 * {@link org.apache.calcite.rel.logical.LogicalCalc}.
 *
 * <p>A {@link org.apache.calcite.rel.logical.LogicalCalc}
 * is trivial if it projects its input fields in their
 * original order, and it does not filter.
 *
 * @see ProjectRemoveRule
 */
@Value.Enclosing
public class CalcRemoveRule extends RelRule<CalcRemoveRule.Config>
    implements SubstitutionRule {

  /** Creates a CalcRemoveRule. */
  protected CalcRemoveRule(Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public CalcRemoveRule(RelBuilderFactory relBuilderFactory) {
    this(Config.DEFAULT.withRelBuilderFactory(relBuilderFactory)
        .as(Config.class));
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void onMatch(RelOptRuleCall call) {
    final Calc calc = call.rel(0);
    assert calc.getProgram().isTrivial() : "rule predicate";
    RelNode input = calc.getInput();
    input = call.getPlanner().register(input, calc);
    call.transformTo(
        convert(
            input,
            calc.getTraitSet()));
  }

  /** Rule configuration. */
  @Value.Immutable
  public interface Config extends RelRule.Config {
    Config DEFAULT = ImmutableCalcRemoveRule.Config.of()
        .withOperandSupplier(b ->
            b.operand(LogicalCalc.class)
                .predicate(calc -> calc.getProgram().isTrivial())
                .anyInputs())
        .as(Config.class);

    @Override default CalcRemoveRule toRule() {
      return new CalcRemoveRule(this);
    }
  }
}

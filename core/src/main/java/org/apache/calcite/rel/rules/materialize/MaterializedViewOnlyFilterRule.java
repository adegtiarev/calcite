/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MaterializedViewOnlyFilterRule.java
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
package org.apache.calcite.rel.rules.materialize;

import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.tools.RelBuilderFactory;

import org.immutables.value.Value;

/** Rule that matches Filter. */
@Value.Enclosing
public class MaterializedViewOnlyFilterRule
    extends MaterializedViewJoinRule<MaterializedViewOnlyFilterRule.Config> {

  private MaterializedViewOnlyFilterRule(Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public MaterializedViewOnlyFilterRule(RelBuilderFactory relBuilderFactory,
      boolean generateUnionRewriting, HepProgram unionRewritingPullProgram,
      boolean fastBailOut) {
    this(Config.DEFAULT
        .withGenerateUnionRewriting(generateUnionRewriting)
        .withUnionRewritingPullProgram(unionRewritingPullProgram)
        .withFastBailOut(fastBailOut)
        .withRelBuilderFactory(relBuilderFactory)
        .as(Config.class));
  }

  @Override public void onMatch(RelOptRuleCall call) {
    final Filter filter = call.rel(0);
    perform(call, null, filter);
  }

  /** Rule configuration. */
  @Value.Immutable(singleton = false)
  public interface Config extends MaterializedViewRule.Config {
    Config DEFAULT = ImmutableMaterializedViewOnlyFilterRule.Config.builder()
        .withOperandSupplier(b -> b.operand(Filter.class).anyInputs())
        .withRelBuilderFactory(RelFactories.LOGICAL_BUILDER)
        .withDescription("MaterializedViewJoinRule(Filter)")
        .withGenerateUnionRewriting(true)
        .withUnionRewritingPullProgram(null)
        .withFastBailOut(true)
        .build();

    @Override default MaterializedViewOnlyFilterRule toRule() {
      return new MaterializedViewOnlyFilterRule(this);
    }
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/Abzhanov/piscine-go/releases/tag/calcite-1.26.0
*    Source File: LogicalExchange.java
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
import org.apache.calcite.rel.RelDistribution;
import org.apache.calcite.rel.RelDistributionTraitDef;
import org.apache.calcite.rel.RelInput;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelShuttle;
import org.apache.calcite.rel.core.Exchange;

/**
 * Sub-class of {@link Exchange} not
 * targeted at any particular engine or calling convention.
 */
public final class LogicalExchange extends Exchange {
  private LogicalExchange(RelOptCluster cluster, RelTraitSet traitSet,
      RelNode input, RelDistribution distribution) {
    super(cluster, traitSet, input, distribution);
    assert traitSet.containsIfApplicable(Convention.NONE);
  }

  /**
   * Creates a LogicalExchange by parsing serialized output.
   */
  public LogicalExchange(RelInput input) {
    super(input);
  }

  /**
   * Creates a LogicalExchange.
   *
   * @param input     Input relational expression
   * @param distribution Distribution specification
   */
  public static LogicalExchange create(RelNode input,
      RelDistribution distribution) {
    RelOptCluster cluster = input.getCluster();
    distribution = RelDistributionTraitDef.INSTANCE.canonize(distribution);
    RelTraitSet traitSet =
        input.getTraitSet().replace(Convention.NONE).replace(distribution);
    return new LogicalExchange(cluster, traitSet, input, distribution);
  }

  //~ Methods ----------------------------------------------------------------

  @Override public Exchange copy(RelTraitSet traitSet, RelNode newInput,
      RelDistribution newDistribution) {
    return new LogicalExchange(getCluster(), traitSet, newInput,
        newDistribution);
  }

  @Override public RelNode accept(RelShuttle shuttle) {
    return shuttle.visit(this);
  }
}

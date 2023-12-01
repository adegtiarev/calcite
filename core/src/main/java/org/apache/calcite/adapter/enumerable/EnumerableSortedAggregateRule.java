/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.33.0
*    Source File: EnumerableSortedAggregateRule.java
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
package org.apache.calcite.adapter.enumerable;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.logical.LogicalAggregate;
import org.apache.calcite.util.ImmutableIntList;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Rule to convert a {@link LogicalAggregate} to an {@link EnumerableSortedAggregate}.
 * You may provide a custom config to convert other nodes that extend {@link Aggregate}.
 *
 * @see EnumerableRules#ENUMERABLE_SORTED_AGGREGATE_RULE
 */
class EnumerableSortedAggregateRule extends ConverterRule {
  /** Default configuration. */
  public static final Config DEFAULT_CONFIG = Config.INSTANCE
      .withConversion(LogicalAggregate.class, Convention.NONE,
          EnumerableConvention.INSTANCE, "EnumerableSortedAggregateRule")
      .withRuleFactory(EnumerableSortedAggregateRule::new);

  /** Called from the Config. */
  protected EnumerableSortedAggregateRule(Config config) {
    super(config);
  }

  @Override public @Nullable RelNode convert(RelNode rel) {
    final Aggregate agg = (Aggregate) rel;
    if (!Aggregate.isSimple(agg)) {
      return null;
    }
    final RelTraitSet inputTraits = rel.getCluster()
        .traitSet().replace(EnumerableConvention.INSTANCE)
        .replace(
            RelCollations.of(
                ImmutableIntList.copyOf(
            agg.getGroupSet().asList())));
    final RelTraitSet selfTraits = inputTraits.replace(
        RelCollations.of(
        ImmutableIntList.identity(agg.getGroupSet().cardinality())));
    return new EnumerableSortedAggregate(
        rel.getCluster(),
        selfTraits,
        convert(agg.getInput(), inputTraits),
        agg.getGroupSet(),
        agg.getGroupSets(),
        agg.getAggCallList());
  }
}

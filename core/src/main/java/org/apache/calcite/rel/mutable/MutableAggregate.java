/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MutableAggregate.java
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
package org.apache.calcite.rel.mutable;

import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.util.ImmutableBitSet;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Objects;

/** Mutable equivalent of {@link org.apache.calcite.rel.core.Aggregate}. */
public class MutableAggregate extends MutableSingleRel {
  public final ImmutableBitSet groupSet;
  public final ImmutableList<ImmutableBitSet> groupSets;
  public final List<AggregateCall> aggCalls;

  private MutableAggregate(MutableRel input, RelDataType rowType,
      ImmutableBitSet groupSet,
      @Nullable List<ImmutableBitSet> groupSets, List<AggregateCall> aggCalls) {
    super(MutableRelType.AGGREGATE, rowType, input);
    this.groupSet = groupSet;
    this.groupSets = groupSets == null
        ? ImmutableList.of(groupSet)
        : ImmutableList.copyOf(groupSets);
    assert ImmutableBitSet.ORDERING.isStrictlyOrdered(
        this.groupSets) : this.groupSets;
    for (ImmutableBitSet set : this.groupSets) {
      assert groupSet.contains(set);
    }
    this.aggCalls = aggCalls;
  }

  /**
   * Creates a MutableAggregate.
   *
   * @param input     Input relational expression
   * @param groupSet  Bit set of grouping fields
   * @param groupSets List of all grouping sets; null for just {@code groupSet}
   * @param aggCalls  Collection of calls to aggregate functions
   */
  public static MutableAggregate of(MutableRel input, ImmutableBitSet groupSet,
      @Nullable ImmutableList<ImmutableBitSet> groupSets, List<AggregateCall> aggCalls) {
    RelDataType rowType =
        Aggregate.deriveRowType(input.cluster.getTypeFactory(),
            input.rowType, false, groupSet, groupSets, aggCalls);
    return new MutableAggregate(input, rowType, groupSet,
        groupSets, aggCalls);
  }

  @Override public boolean equals(@Nullable Object obj) {
    return obj == this
        || obj instanceof MutableAggregate
        && groupSet.equals(((MutableAggregate) obj).groupSet)
        && groupSets.equals(((MutableAggregate) obj).groupSets)
        && aggCalls.equals(((MutableAggregate) obj).aggCalls)
        && input.equals(((MutableAggregate) obj).input);
  }

  @Override public int hashCode() {
    return Objects.hash(input, groupSet, groupSets, aggCalls);
  }

  @Override public StringBuilder digest(StringBuilder buf) {
    return buf.append("Aggregate(groupSet: ").append(groupSet)
        .append(", groupSets: ").append(groupSets)
        .append(", calls: ").append(aggCalls).append(")");
  }

  public Aggregate.Group getGroupType() {
    return Aggregate.Group.induce(groupSet, groupSets);
  }

  @Override public MutableRel clone() {
    return MutableAggregate.of(input.clone(), groupSet, groupSets, aggCalls);
  }
}

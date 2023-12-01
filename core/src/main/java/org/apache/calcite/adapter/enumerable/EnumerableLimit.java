/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.33.0
*    Source File: EnumerableLimit.java
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

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.tree.BlockBuilder;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.linq4j.tree.Expressions;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollationTraitDef;
import org.apache.calcite.rel.RelDistributionTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelWriter;
import org.apache.calcite.rel.SingleRel;
import org.apache.calcite.rel.metadata.RelMdCollation;
import org.apache.calcite.rel.metadata.RelMdDistribution;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rex.RexDynamicParam;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.util.BuiltInMethod;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/** Relational expression that applies a limit and/or offset to its input. */
public class EnumerableLimit extends SingleRel implements EnumerableRel {
  public final @Nullable RexNode offset;
  public final @Nullable RexNode fetch;

  /** Creates an EnumerableLimit.
   *
   * <p>Use {@link #create} unless you know what you're doing. */
  public EnumerableLimit(
      RelOptCluster cluster,
      RelTraitSet traitSet,
      RelNode input,
      @Nullable RexNode offset,
      @Nullable RexNode fetch) {
    super(cluster, traitSet, input);
    this.offset = offset;
    this.fetch = fetch;
    assert getConvention() instanceof EnumerableConvention;
    assert getConvention() == input.getConvention();
  }

  /** Creates an EnumerableLimit. */
  public static EnumerableLimit create(final RelNode input, @Nullable RexNode offset,
      @Nullable RexNode fetch) {
    final RelOptCluster cluster = input.getCluster();
    final RelMetadataQuery mq = cluster.getMetadataQuery();
    final RelTraitSet traitSet =
        cluster.traitSetOf(EnumerableConvention.INSTANCE)
            .replaceIfs(
                RelCollationTraitDef.INSTANCE,
                () -> RelMdCollation.limit(mq, input))
            .replaceIf(RelDistributionTraitDef.INSTANCE,
                () -> RelMdDistribution.limit(mq, input));
    return new EnumerableLimit(cluster, traitSet, input, offset, fetch);
  }

  @Override public EnumerableLimit copy(
      RelTraitSet traitSet,
      List<RelNode> newInputs) {
    return new EnumerableLimit(
        getCluster(),
        traitSet,
        sole(newInputs),
        offset,
        fetch);
  }

  @Override public RelWriter explainTerms(RelWriter pw) {
    return super.explainTerms(pw)
        .itemIf("offset", offset, offset != null)
        .itemIf("fetch", fetch, fetch != null);
  }

  @Override public Result implement(EnumerableRelImplementor implementor, Prefer pref) {
    final BlockBuilder builder = new BlockBuilder();
    final EnumerableRel child = (EnumerableRel) getInput();
    final Result result = implementor.visitChild(this, 0, child, pref);
    final PhysType physType =
        PhysTypeImpl.of(
            implementor.getTypeFactory(),
            getRowType(),
            result.format);

    Expression v = builder.append("child", result.block);
    if (offset != null) {
      v = builder.append(
          "offset",
          Expressions.call(
              v,
              BuiltInMethod.SKIP.method,
              getExpression(offset)));
    }
    if (fetch != null) {
      v = builder.append(
          "fetch",
          Expressions.call(
              v,
              BuiltInMethod.TAKE.method,
              getExpression(fetch)));
    }

    builder.add(
        Expressions.return_(
            null,
            v));
    return implementor.result(physType, builder.toBlock());
  }

  static Expression getExpression(RexNode rexNode) {
    if (rexNode instanceof RexDynamicParam) {
      final RexDynamicParam param = (RexDynamicParam) rexNode;
      return Expressions.convert_(
          Expressions.call(DataContext.ROOT,
              BuiltInMethod.DATA_CONTEXT_GET.method,
              Expressions.constant("?" + param.getIndex())),
          Integer.class);
    } else {
      return Expressions.constant(RexLiteral.intValue(rexNode));
    }
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Sample.java
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
package org.apache.calcite.rel.core;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptSamplingParameters;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelInput;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelWriter;
import org.apache.calcite.rel.SingleRel;

import java.util.List;

/**
 * Relational expression that returns a sample of the rows from its input.
 *
 * <p>In SQL, a sample is expressed using the {@code TABLESAMPLE BERNOULLI} or
 * {@code SYSTEM} keyword applied to a table, view or sub-query.
 */
public class Sample extends SingleRel {
  //~ Instance fields --------------------------------------------------------

  private final RelOptSamplingParameters params;

  //~ Constructors -----------------------------------------------------------

  public Sample(RelOptCluster cluster, RelNode child,
      RelOptSamplingParameters params) {
    super(cluster, cluster.traitSetOf(Convention.NONE), child);
    this.params = params;
  }

  /**
   * Creates a Sample by parsing serialized output.
   */
  public Sample(RelInput input) {
    this(input.getCluster(), input.getInput(), getSamplingParameters(input));
  }

  //~ Methods ----------------------------------------------------------------

  private static RelOptSamplingParameters getSamplingParameters(
      RelInput input) {
    String mode = input.getString("mode");
    float percentage = input.getFloat("rate");
    Object repeatableSeed = input.get("repeatableSeed");
    boolean repeatable = repeatableSeed instanceof Number;
    return new RelOptSamplingParameters(
        "bernoulli".equals(mode), percentage, repeatable,
        repeatable && repeatableSeed != null ? ((Number) repeatableSeed).intValue() : 0);
  }

  @Override public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
    assert traitSet.containsIfApplicable(Convention.NONE);
    return new Sample(getCluster(), sole(inputs), params);
  }

  /**
   * Retrieve the sampling parameters for this Sample.
   */
  public RelOptSamplingParameters getSamplingParameters() {
    return params;
  }

  @Override public RelWriter explainTerms(RelWriter pw) {
    return super.explainTerms(pw)
        .item("mode", params.isBernoulli() ? "bernoulli" : "system")
        .item("rate", params.getSamplingPercentage())
        .item("repeatableSeed",
            params.isRepeatable() ? params.getRepeatableSeed() : "-");
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/cf-testorg/calcite-test/releases/tag/calcite-1.26.0-rc0
*    Source File: LogicalRepeatUnion.java
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

import org.apache.calcite.linq4j.function.Experimental;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.RepeatUnion;

import java.util.List;

/**
 * Sub-class of {@link org.apache.calcite.rel.core.RepeatUnion}
 * not targeted at any particular engine or calling convention.
 *
 * <p>NOTE: The current API is experimental and subject to change without
 * notice.
 */
@Experimental
public class LogicalRepeatUnion extends RepeatUnion {

  //~ Constructors -----------------------------------------------------------
  private LogicalRepeatUnion(RelOptCluster cluster, RelTraitSet traitSet,
      RelNode seed, RelNode iterative, boolean all, int iterationLimit) {
    super(cluster, traitSet, seed, iterative, all, iterationLimit);
  }

  /** Creates a LogicalRepeatUnion. */
  public static LogicalRepeatUnion create(RelNode seed, RelNode iterative,
      boolean all) {
    return create(seed, iterative, all, -1);
  }

  /** Creates a LogicalRepeatUnion. */
  public static LogicalRepeatUnion create(RelNode seed, RelNode iterative,
      boolean all, int iterationLimit) {
    RelOptCluster cluster = seed.getCluster();
    RelTraitSet traitSet = cluster.traitSetOf(Convention.NONE);
    return new LogicalRepeatUnion(cluster, traitSet, seed, iterative, all, iterationLimit);
  }

  //~ Methods ----------------------------------------------------------------

  @Override public LogicalRepeatUnion copy(RelTraitSet traitSet,
      List<RelNode> inputs) {
    assert traitSet.containsIfApplicable(Convention.NONE);
    assert inputs.size() == 2;
    return new LogicalRepeatUnion(getCluster(), traitSet,
        inputs.get(0), inputs.get(1), all, iterationLimit);
  }
}

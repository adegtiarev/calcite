/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RelMdLowerBoundCost.java
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
package org.apache.calcite.rel.metadata;

import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.volcano.RelSubset;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.metadata.BuiltInMetadata.LowerBoundCost;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Default implementations of the
 * {@link BuiltInMetadata.LowerBoundCost}
 * metadata provider for the standard algebra.
 */
public class RelMdLowerBoundCost implements MetadataHandler<LowerBoundCost> {

  public static final RelMetadataProvider SOURCE =
      ReflectiveRelMetadataProvider.reflectiveSource(
          new RelMdLowerBoundCost(), LowerBoundCost.Handler.class);

  //~ Constructors -----------------------------------------------------------

  protected RelMdLowerBoundCost() {}

  //~ Methods ----------------------------------------------------------------

  @Override public MetadataDef<LowerBoundCost> getDef() {
    return BuiltInMetadata.LowerBoundCost.DEF;
  }

  public @Nullable RelOptCost getLowerBoundCost(RelSubset subset,
      RelMetadataQuery mq, VolcanoPlanner planner) {

    if (planner.isLogical(subset)) {
      // currently only support physical, will improve in the future
      return null;
    }

    return subset.getWinnerCost();
  }

  public @Nullable RelOptCost getLowerBoundCost(RelNode node,
      RelMetadataQuery mq, VolcanoPlanner planner) {
    if (planner.isLogical(node)) {
      // currently only support physical, will improve in the future
      return null;
    }

    RelOptCost selfCost = mq.getNonCumulativeCost(node);
    if (selfCost != null && selfCost.isInfinite()) {
      selfCost = null;
    }
    for (RelNode input : node.getInputs()) {
      RelOptCost lb = mq.getLowerBoundCost(input, planner);
      if (lb != null) {
        selfCost = selfCost == null ? lb : selfCost.plus(lb);
      }
    }
    return selfCost;
  }
}

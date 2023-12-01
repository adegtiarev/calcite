/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: GeodeSort.java
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
package org.apache.calcite.adapter.geode.rel;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelFieldCollation;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of
 * {@link Sort}
 * relational expression in Geode.
 */
public class GeodeSort extends Sort implements GeodeRel {

  public static final String ASC = "ASC";
  public static final String DESC = "DESC";

  /** Creates a GeodeSort. */
  GeodeSort(RelOptCluster cluster, RelTraitSet traitSet,
      RelNode input, RelCollation collation, RexNode fetch) {
    super(cluster, traitSet, input, collation, null, fetch);

    assert getConvention() == GeodeRel.CONVENTION;
    assert getConvention() == input.getConvention();
  }

  @Override public @Nullable RelOptCost computeSelfCost(RelOptPlanner planner,
      RelMetadataQuery mq) {

    RelOptCost cost = super.computeSelfCost(planner, mq);

    if (fetch != null) {
      return cost.multiplyBy(0.05);
    } else {
      return cost.multiplyBy(0.9);
    }
  }

  @Override public Sort copy(RelTraitSet traitSet, RelNode input,
      RelCollation newCollation, RexNode offset, RexNode fetch) {
    return new GeodeSort(getCluster(), traitSet, input, collation, fetch);
  }

  @Override public void implement(GeodeImplementContext geodeImplementContext) {
    geodeImplementContext.visitChild(getInput());

    List<RelFieldCollation> sortCollations = collation.getFieldCollations();

    if (!sortCollations.isEmpty()) {

      List<String> orderByFields = new ArrayList<>();

      for (RelFieldCollation fieldCollation : sortCollations) {
        final String name = fieldName(fieldCollation.getFieldIndex());
        orderByFields.add(name + " " + direction(fieldCollation.getDirection()));
      }
      geodeImplementContext.addOrderByFields(orderByFields);
    }

    if (fetch != null) {
      geodeImplementContext.setLimit(((RexLiteral) fetch).getValueAs(Long.class));
    }
  }

  private String fieldName(int index) {
    return getRowType().getFieldList().get(index).getName();
  }

  private static String direction(RelFieldCollation.Direction relDirection) {
    if (relDirection == RelFieldCollation.Direction.DESCENDING) {
      return DESC;
    }
    return ASC;
  }
}

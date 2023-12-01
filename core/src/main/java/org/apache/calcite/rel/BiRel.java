/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: BiRel.java
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
package org.apache.calcite.rel;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.runtime.FlatLists;

import java.util.List;

/**
 * Abstract base class for relational expressions with a two inputs.
 *
 * <p>It is not required that two-input relational expressions use this
 * class as a base class. However, default implementations of methods make life
 * easier.
 */
public abstract class BiRel extends AbstractRelNode {
  protected RelNode left;
  protected RelNode right;

  protected BiRel(
      RelOptCluster cluster, RelTraitSet traitSet, RelNode left,
      RelNode right) {
    super(cluster, traitSet);
    this.left = left;
    this.right = right;
  }

  @Override public void childrenAccept(RelVisitor visitor) {
    visitor.visit(left, 0, this);
    visitor.visit(right, 1, this);
  }

  @Override public List<RelNode> getInputs() {
    return FlatLists.of(left, right);
  }

  public RelNode getLeft() {
    return left;
  }

  public RelNode getRight() {
    return right;
  }

  @Override public void replaceInput(
      int ordinalInParent,
      RelNode p) {
    switch (ordinalInParent) {
    case 0:
      this.left = p;
      break;
    case 1:
      this.right = p;
      break;
    default:
      throw new IndexOutOfBoundsException("Input " + ordinalInParent);
    }
    recomputeDigest();
  }

  @Override public RelWriter explainTerms(RelWriter pw) {
    return super.explainTerms(pw)
        .input("left", left)
        .input("right", right);
  }
}

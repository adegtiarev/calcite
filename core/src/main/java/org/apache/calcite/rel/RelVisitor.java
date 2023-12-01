/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RelVisitor.java
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

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A <code>RelVisitor</code> is a Visitor role in the
 * {@link org.apache.calcite.util.Glossary#VISITOR_PATTERN visitor pattern} and
 * visits {@link RelNode} objects as the role of Element. Other components in
 * the pattern: {@link RelNode#childrenAccept(RelVisitor)}.
 */
public abstract class RelVisitor {
  //~ Instance fields --------------------------------------------------------

  private @Nullable RelNode root;

  //~ Methods ----------------------------------------------------------------

  /**
   * Visits a node during a traversal.
   *
   * @param node    Node to visit
   * @param ordinal Ordinal of node within its parent
   * @param parent  Parent of the node, or null if it is the root of the
   *                traversal
   */
  public void visit(
      RelNode node,
      int ordinal,
      @Nullable RelNode parent) {
    node.childrenAccept(this);
  }

  /**
   * Replaces the root node of this traversal.
   *
   * @param node The new root node
   */
  public void replaceRoot(@Nullable RelNode node) {
    this.root = node;
  }

  /**
   * Starts an iteration.
   */
  public @Nullable RelNode go(RelNode p) {
    this.root = p;
    visit(p, 0, null);
    return root;
  }
}

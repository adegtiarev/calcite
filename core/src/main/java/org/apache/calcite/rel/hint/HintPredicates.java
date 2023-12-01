/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.29.0-rc0
*    Source File: HintPredicates.java
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
package org.apache.calcite.rel.hint;

/**
 * A collection of hint predicates.
 */
public abstract class HintPredicates {
  /** A hint predicate that indicates a hint can only be used to
   * the whole query(no specific nodes). */
  public static final HintPredicate SET_VAR =
      new NodeTypeHintPredicate(NodeTypeHintPredicate.NodeType.SET_VAR);

  /** A hint predicate that indicates a hint can only be used to
   * {@link org.apache.calcite.rel.core.Join} nodes. */
  public static final HintPredicate JOIN =
      new NodeTypeHintPredicate(NodeTypeHintPredicate.NodeType.JOIN);

  /** A hint predicate that indicates a hint can only be used to
   * {@link org.apache.calcite.rel.core.TableScan} nodes. */
  public static final HintPredicate TABLE_SCAN =
      new NodeTypeHintPredicate(NodeTypeHintPredicate.NodeType.TABLE_SCAN);

  /** A hint predicate that indicates a hint can only be used to
   * {@link org.apache.calcite.rel.core.Project} nodes. */
  public static final HintPredicate PROJECT =
      new NodeTypeHintPredicate(NodeTypeHintPredicate.NodeType.PROJECT);

  /** A hint predicate that indicates a hint can only be used to
   * {@link org.apache.calcite.rel.core.Aggregate} nodes. */
  public static final HintPredicate AGGREGATE =
      new NodeTypeHintPredicate(NodeTypeHintPredicate.NodeType.AGGREGATE);

  /** A hint predicate that indicates a hint can only be used to
   * {@link org.apache.calcite.rel.core.Calc} nodes. */
  public static final HintPredicate CALC =
      new NodeTypeHintPredicate(NodeTypeHintPredicate.NodeType.CALC);

  /**
   * Returns a composed hint predicate that represents a short-circuiting logical
   * AND of an array of hint predicates {@code hintPredicates}.  When evaluating the composed
   * predicate, if a predicate is {@code false}, then all the left
   * predicates are not evaluated.
   *
   * <p>The predicates are evaluated in sequence.
   */
  public static HintPredicate and(HintPredicate... hintPredicates) {
    return new CompositeHintPredicate(CompositeHintPredicate.Composition.AND, hintPredicates);
  }

  /**
   * Returns a composed hint predicate that represents a short-circuiting logical
   * OR of an array of hint predicates {@code hintPredicates}.  When evaluating the composed
   * predicate, if a predicate is {@code true}, then all the left
   * predicates are not evaluated.
   *
   * <p>The predicates are evaluated in sequence.
   */
  public static HintPredicate or(HintPredicate... hintPredicates) {
    return new CompositeHintPredicate(CompositeHintPredicate.Composition.OR, hintPredicates);
  }
}

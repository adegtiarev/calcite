/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: RexBiVisitor.java
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
package org.apache.calcite.rex;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor pattern for traversing a tree of {@link RexNode} objects
 * and passing a payload to each.
 *
 * @see RexVisitor
 *
 * @param <R> Return type
 * @param <P> Payload type
 */
public interface RexBiVisitor<R, P> {
  //~ Methods ----------------------------------------------------------------

  R visitInputRef(RexInputRef inputRef, P arg);

  R visitLocalRef(RexLocalRef localRef, P arg);

  R visitLiteral(RexLiteral literal, P arg);

  R visitCall(RexCall call, P arg);

  R visitOver(RexOver over, P arg);

  R visitCorrelVariable(RexCorrelVariable correlVariable, P arg);

  R visitDynamicParam(RexDynamicParam dynamicParam, P arg);

  R visitRangeRef(RexRangeRef rangeRef, P arg);

  R visitFieldAccess(RexFieldAccess fieldAccess, P arg);

  R visitSubQuery(RexSubQuery subQuery, P arg);

  R visitTableInputRef(RexTableInputRef ref, P arg);

  R visitPatternFieldRef(RexPatternFieldRef ref, P arg);

  /** Visits a list and writes the results to another list. */
  default void visitList(Iterable<? extends RexNode> exprs, P arg,
      List<R> out) {
    for (RexNode expr : exprs) {
      out.add(expr.accept(this, arg));
    }
  }

  /** Visits a list and returns a list of the results.
   * The resulting list is immutable and does not contain nulls. */
  default List<R> visitList(Iterable<? extends RexNode> exprs, P arg) {
    final List<R> out = new ArrayList<>();
    visitList(exprs, arg, out);
    return ImmutableList.copyOf(out);
  }

  /** Visits a list of expressions. */
  default void visitEach(Iterable<? extends RexNode> exprs, P arg) {
    for (RexNode expr : exprs) {
      expr.accept(this, arg);
    }
  }

  /** Visits a list of expressions, passing the 0-based index of the expression
   * in the list.
   *
   * <p>Assumes that the payload type {@code P} is {@code Integer}. */
  default void visitEachIndexed(Iterable<? extends RexNode> exprs) {
    int i = 0;
    for (RexNode expr : exprs) {
      //noinspection unchecked
      expr.accept(this, (P) (Integer) i++);
    }
  }
}

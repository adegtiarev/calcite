/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/ember-codemods/ember-tracked-properties-codemod/releases/tag/calcite-1.26.0
*    Source File: CompositeHintPredicate.java
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

import org.apache.calcite.rel.RelNode;

import com.google.common.collect.ImmutableList;

/**
 * A {@link HintPredicate} to combine multiple hint predicates into one.
 *
 * <p>The composition can be {@code AND} or {@code OR}.
 */
public class CompositeHintPredicate implements HintPredicate {
  //~ Enums ------------------------------------------------------------------

  /** How hint predicates are composed. */
  public enum Composition {
    AND, OR
  }

  //~ Instance fields --------------------------------------------------------

  private ImmutableList<HintPredicate> predicates;
  private Composition composition;

  /**
   * Creates a {@link CompositeHintPredicate} with a {@link Composition}
   * and an array of hint predicates.
   *
   * <p>Make this constructor package-protected intentionally.
   * Use utility methods in {@link HintPredicates}
   * to create a {@link CompositeHintPredicate}.</p>
   */
  CompositeHintPredicate(Composition composition, HintPredicate... predicates) {
    assert predicates != null;
    assert predicates.length > 1;
    for (HintPredicate predicate : predicates) {
      assert predicate != null;
    }
    this.predicates = ImmutableList.copyOf(predicates);
    this.composition = composition;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public boolean apply(RelHint hint, RelNode rel) {
    return apply(composition, hint, rel);
  }

  private boolean apply(Composition composition, RelHint hint, RelNode rel) {
    switch (composition) {
    case AND:
      for (HintPredicate predicate: predicates) {
        if (!predicate.apply(hint, rel)) {
          return false;
        }
      }
      return true;
    case OR:
    default:
      for (HintPredicate predicate: predicates) {
        if (predicate.apply(hint, rel)) {
          return true;
        }
      }
      return false;
    }
  }
}

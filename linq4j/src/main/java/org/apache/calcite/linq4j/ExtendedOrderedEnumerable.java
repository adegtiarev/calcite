/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/ember-codemods/ember-tracked-properties-codemod/releases/tag/calcite-1.26.0
*    Source File: ExtendedOrderedEnumerable.java
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
package org.apache.calcite.linq4j;

import org.apache.calcite.linq4j.function.Function1;

import java.util.Comparator;

/**
 * Extension methods for {@link OrderedEnumerable}.
 *
 * @param <T> Element type
 */
public interface ExtendedOrderedEnumerable<T> extends Enumerable<T> {
  /**
   * Performs a subsequent ordering of the elements in an
   * {@link OrderedEnumerable} according to a key, using a specified
   * comparator.
   *
   * <p>The functionality provided by this method is like that provided by
   * {@link #thenBy(org.apache.calcite.linq4j.function.Function1, java.util.Comparator) thenBy}
   * or
   * {@link #thenByDescending(org.apache.calcite.linq4j.function.Function1, java.util.Comparator) thenByDescending},
   * depending on whether descending is true or false. They both perform a
   * subordinate ordering of an already sorted sequence of type
   * {@link OrderedEnumerable}.</p>
   */
  <TKey> OrderedEnumerable<T> createOrderedEnumerable(
      Function1<T, TKey> keySelector, Comparator<TKey> comparator,
      boolean descending);

  /**
   * Performs a subsequent ordering of the elements in a sequence in
   * ascending order according to a key.
   */
  <TKey extends Comparable<TKey>> OrderedEnumerable<T> thenBy(
      Function1<T, TKey> keySelector);

  /**
   * Performs a subsequent ordering of the elements in a sequence in
   * ascending order according to a key, using a specified comparator.
   */
  <TKey> OrderedEnumerable<T> thenBy(Function1<T, TKey> keySelector,
      Comparator<TKey> comparator);

  /**
   * Performs a subsequent ordering of the elements in a sequence in
   * descending order according to a key.
   */
  <TKey extends Comparable<TKey>> OrderedEnumerable<T> thenByDescending(
      Function1<T, TKey> keySelector);

  /**
   * Performs a subsequent ordering of the elements in a sequence in
   * descending order according to a key, using a specified comparator.
   */
  <TKey> OrderedEnumerable<T> thenByDescending(Function1<T, TKey> keySelector,
      Comparator<TKey> comparator);
}

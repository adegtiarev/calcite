/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/cf-testorg/calcite-test/releases/tag/calcite-1.26.0-rc0
*    Source File: SourceSorter.java
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
package org.apache.calcite.adapter.enumerable;

import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.function.Function1;
import org.apache.calcite.linq4j.function.Function2;

import java.util.Comparator;
import java.util.List;

/**
 * Helper that combines the sorting process and accumulating process against the
 * aggregate execution, used with {@link LazyAggregateLambdaFactory}.
 *
 * @param <TAccumulate> Type of the accumulator
 * @param <TSource>     Type of the enumerable input source
 * @param <TSortKey>    Type of the sort key
 */
public class SourceSorter<TAccumulate, TSource, TSortKey>
    implements LazyAggregateLambdaFactory.LazyAccumulator<TAccumulate, TSource> {

  private final Function2<TAccumulate, TSource, TAccumulate> accumulatorAdder;
  private final Function1<TSource, TSortKey> keySelector;
  private final Comparator<TSortKey> comparator;

  public SourceSorter(
      Function2<TAccumulate, TSource, TAccumulate> accumulatorAdder,
      Function1<TSource, TSortKey> keySelector,
      Comparator<TSortKey> comparator) {
    this.accumulatorAdder = accumulatorAdder;
    this.keySelector = keySelector;
    this.comparator = comparator;
  }

  @Override public void accumulate(Iterable<TSource> sourceIterable,
      TAccumulate accumulator) {
    sortAndAccumulate(sourceIterable, accumulator);
  }

  private void sortAndAccumulate(Iterable<TSource> sourceIterable,
      TAccumulate accumulator) {
    List<TSource> sorted = Linq4j.asEnumerable(sourceIterable)
        .orderBy(keySelector, comparator)
        .toList();
    TAccumulate accumulator1 = accumulator;
    for (TSource source : sorted) {
      accumulator1 = accumulatorAdder.apply(accumulator1, source);
    }
  }
}

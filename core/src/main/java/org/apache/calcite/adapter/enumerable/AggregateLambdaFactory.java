/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/Abzhanov/piscine-go/releases/tag/calcite-1.26.0
*    Source File: AggregateLambdaFactory.java
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

import org.apache.calcite.linq4j.function.Function0;
import org.apache.calcite.linq4j.function.Function1;
import org.apache.calcite.linq4j.function.Function2;

/**
 * Generates lambda functions used in {@link EnumerableAggregate}.
 *
 * <p>This interface allows a implicit accumulator type variation.
 * ({@code OAccumulate} {@literal ->} {@code TAccumulate})
 *
 * @param <TSource> Type of the enumerable input source
 * @param <TOrigAccumulate> Type of the original accumulator
 * @param <TAccumulate> Type of the varied accumulator
 * @param <TResult> Type of the enumerable output result
 * @param <TKey> Type of the group-by key
 */
public interface AggregateLambdaFactory<TSource, TOrigAccumulate, TAccumulate,
    TResult, TKey> {
  Function0<TAccumulate> accumulatorInitializer();

  Function2<TAccumulate, TSource, TAccumulate> accumulatorAdder();

  Function1<TAccumulate, TResult> singleGroupResultSelector(
      Function1<TOrigAccumulate, TResult> resultSelector);

  Function2<TKey, TAccumulate, TResult> resultSelector(
      Function2<TKey, TOrigAccumulate, TResult> resultSelector);
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: AggResultContext.java
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

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.rel.core.AggregateCall;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Information for a call to
 * {@link AggImplementor#implementResult(AggContext, AggResultContext)}
 *
 * <p>Typically, the aggregation implementation will convert
 * {@link #accumulator()} to the resulting value of the aggregation.  The
 * implementation MUST NOT destroy the contents of {@link #accumulator()}.
 */
public interface AggResultContext extends NestedBlockBuilder, AggResetContext {
  /** Expression by which to reference the key upon which the values in the
   * accumulator were aggregated. Most aggregate functions depend on only the
   * accumulator, but quasi-aggregate functions such as GROUPING access at the
   * key. */
  @Nullable Expression key();

  /** Returns an expression that references the {@code i}th field of the key,
   * cast to the appropriate type. */
  Expression keyField(int i);

  AggregateCall call();
}

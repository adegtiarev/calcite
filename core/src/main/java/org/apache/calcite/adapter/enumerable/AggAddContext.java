/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: AggAddContext.java
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
import org.apache.calcite.rex.RexNode;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Information for a call to
 * {@link org.apache.calcite.adapter.enumerable.AggImplementor#implementAdd(AggContext, AggAddContext)}.
 *
 * <p>Typically, the aggregation implementation will use {@link #arguments()}
 * or {@link #rexArguments()} to update aggregate value.
 */
public interface AggAddContext extends AggResultContext {
  /**
   * Returns {@link org.apache.calcite.rex.RexNode} representation of arguments.
   * This can be useful for manual translation of required arguments with
   * different {@link NullPolicy}.
   * @return {@link org.apache.calcite.rex.RexNode} representation of arguments
   */
  List<RexNode> rexArguments();

  /**
   * Returns {@link org.apache.calcite.rex.RexNode} representation of the
   * filter, or null.
   */
  @Nullable RexNode rexFilterArgument();

  /**
   * Returns Linq4j form of arguments.
   * The resulting value is equivalent to
   * {@code rowTranslator().translateList(rexArguments())}.
   * This is handy if you need just operate on argument.
   * @return Linq4j form of arguments.
   */
  List<Expression> arguments();

  /**
   * Returns a
   * {@link org.apache.calcite.adapter.enumerable.RexToLixTranslator}
   * suitable to transform the arguments.
   *
   * @return {@link RexToLixTranslator} suitable to transform the arguments
   */
  RexToLixTranslator rowTranslator();
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: WinAggAddContextImpl.java
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
package org.apache.calcite.adapter.enumerable.impl;

import org.apache.calcite.adapter.enumerable.RexToLixTranslator;
import org.apache.calcite.adapter.enumerable.WinAggAddContext;
import org.apache.calcite.adapter.enumerable.WinAggFrameResultContext;
import org.apache.calcite.adapter.enumerable.WinAggImplementor;
import org.apache.calcite.linq4j.tree.BlockBuilder;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.linq4j.tree.Expressions;

import java.util.List;
import java.util.function.Function;

/**
 * Implementation of
 * {@link org.apache.calcite.adapter.enumerable.WinAggAddContext}.
 */
public abstract class WinAggAddContextImpl extends WinAggResultContextImpl
    implements WinAggAddContext {
  protected WinAggAddContextImpl(BlockBuilder block, List<Expression> accumulator,
      Function<BlockBuilder, WinAggFrameResultContext> frame) {
    super(block, accumulator, frame);
  }

  @SuppressWarnings("Guava")
  @Deprecated // to be removed before 2.0
  protected WinAggAddContextImpl(BlockBuilder block, List<Expression> accumulator,
      com.google.common.base.Function<BlockBuilder, WinAggFrameResultContext> frame) {
    this(block, accumulator, (Function<BlockBuilder, WinAggFrameResultContext>) frame::apply);
  }

  @Override public final RexToLixTranslator rowTranslator() {
    return rowTranslator(
        computeIndex(Expressions.constant(0),
            WinAggImplementor.SeekType.AGG_INDEX));
  }

  @Override public final List<Expression> arguments() {
    return rowTranslator().translateList(rexArguments());
  }
}

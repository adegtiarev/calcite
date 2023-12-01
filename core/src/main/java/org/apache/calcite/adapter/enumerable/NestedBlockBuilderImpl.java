/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: NestedBlockBuilderImpl.java
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

import org.apache.calcite.linq4j.tree.BlockBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows to build nested code blocks with tracking of current context.
 *
 * @see org.apache.calcite.adapter.enumerable.StrictAggImplementor#implementAdd(AggContext, AggAddContext)
 */
public class NestedBlockBuilderImpl implements NestedBlockBuilder {
  private final List<BlockBuilder> blocks = new ArrayList<>();

  /**
   * Constructs nested block builders starting of a given code block.
   * @param block root code block
   */
  @SuppressWarnings("method.invocation.invalid")
  public NestedBlockBuilderImpl(BlockBuilder block) {
    nestBlock(block);
  }

  /**
   * Starts nested code block. The resulting block can optimize expressions
   * and reuse already calculated values from the parent blocks.
   * @return new code block that can optimize expressions and reuse already
   * calculated values from the parent blocks.
   */
  @Override public final BlockBuilder nestBlock() {
    BlockBuilder block = new BlockBuilder(true, currentBlock());
    nestBlock(block);
    return block;
  }

  /**
   * Uses given block as the new code context.
   * The current block will be restored after {@link #exitBlock()} call.
   * @param block new code block
   * @see #exitBlock()
   */
  @Override public final void nestBlock(BlockBuilder block) {
    blocks.add(block);
  }

  /**
   * Returns the current code block.
   */
  @Override public final BlockBuilder currentBlock() {
    return blocks.get(blocks.size() - 1);
  }

  /**
   * Leaves the current code block.
   * @see #nestBlock()
   */
  @Override public final void exitBlock() {
    blocks.remove(blocks.size() - 1);
  }
}

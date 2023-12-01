/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: AggResetContextImpl.java
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

import org.apache.calcite.adapter.enumerable.AggResetContext;
import org.apache.calcite.adapter.enumerable.NestedBlockBuilderImpl;
import org.apache.calcite.linq4j.tree.BlockBuilder;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.rel.core.AggregateCall;

import java.util.List;

/**
 * Implementation of
 * {@link org.apache.calcite.adapter.enumerable.AggResetContext}.
 */
public abstract class AggResetContextImpl extends NestedBlockBuilderImpl
    implements AggResetContext {
  private final List<Expression> accumulator;

  /**
   * Creates aggregate reset context.
   *
   * @param block Code block that will contain the added initialization
   * @param accumulator Accumulator variables that store the intermediate
   *                    aggregate state
   */
  protected AggResetContextImpl(BlockBuilder block, List<Expression> accumulator) {
    super(block);
    this.accumulator = accumulator;
  }

  @Override public List<Expression> accumulator() {
    return accumulator;
  }

  public AggregateCall call() {
    throw new UnsupportedOperationException();
  }
}

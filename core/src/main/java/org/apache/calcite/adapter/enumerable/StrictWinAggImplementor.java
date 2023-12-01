/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: StrictWinAggImplementor.java
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

import java.lang.reflect.Type;
import java.util.List;

/**
 * The base implementation of strict window aggregate function.
 * @see org.apache.calcite.adapter.enumerable.RexImpTable.FirstLastValueImplementor
 * @see org.apache.calcite.adapter.enumerable.RexImpTable.RankImplementor
 * @see org.apache.calcite.adapter.enumerable.RexImpTable.RowNumberImplementor
 */
public abstract class StrictWinAggImplementor extends StrictAggImplementor
    implements WinAggImplementor {
  protected abstract void implementNotNullAdd(WinAggContext info,
      WinAggAddContext add);

  protected boolean nonDefaultOnEmptySet(WinAggContext info) {
    return super.nonDefaultOnEmptySet(info);
  }

  public List<Type> getNotNullState(WinAggContext info) {
    return super.getNotNullState(info);
  }

  protected void implementNotNullReset(WinAggContext info,
      WinAggResetContext reset) {
    super.implementNotNullReset(info, reset);
  }

  protected Expression implementNotNullResult(WinAggContext info,
      WinAggResultContext result) {
    return super.implementNotNullResult(info, result);
  }

  @Override protected final void implementNotNullAdd(AggContext info,
      AggAddContext add) {
    implementNotNullAdd((WinAggContext) info, (WinAggAddContext) add);
  }

  @Override protected boolean nonDefaultOnEmptySet(AggContext info) {
    return nonDefaultOnEmptySet((WinAggContext) info);
  }

  @Override public final List<Type> getNotNullState(AggContext info) {
    return getNotNullState((WinAggContext) info);
  }

  @Override protected final void implementNotNullReset(AggContext info,
      AggResetContext reset) {
    implementNotNullReset((WinAggContext) info, (WinAggResetContext) reset);
  }

  @Override protected final Expression implementNotNullResult(AggContext info,
      AggResultContext result) {
    return implementNotNullResult((WinAggContext) info,
        (WinAggResultContext) result);
  }

  @Override public boolean needCacheWhenFrameIntact() {
    return true;
  }
}

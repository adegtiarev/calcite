/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/googleinterns/calcite/releases/tag/master-e94c866
*    Source File: WinAggFrameResultContext.java
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

/**
 * Provides information on the current window when computing the result of
 * the aggregation.
 */
public interface WinAggFrameResultContext extends WinAggFrameContext {
  /**
   * Converts absolute index position of the given relative position.
   * @param offset offset of the requested row
   * @param seekType the type of offset (start of window, end of window, etc)
   * @return absolute position of the requested row
   */
  Expression computeIndex(Expression offset,
      WinAggImplementor.SeekType seekType);

  /**
   * Returns boolean the expression that checks if the given index is in
   * the frame bounds.
   * @param rowIndex index if the row to check
   * @return expression that validates frame bounds for the given index
   */
  Expression rowInFrame(Expression rowIndex);

  /**
   * Returns boolean the expression that checks if the given index is in
   * the partition bounds.
   * @param rowIndex index if the row to check
   * @return expression that validates partition bounds for the given index
   */
  Expression rowInPartition(Expression rowIndex);

  /**
   * Returns row translator for given absolute row position.
   * @param rowIndex absolute index of the row.
   * @return translator for the requested row
   */
  RexToLixTranslator rowTranslator(Expression rowIndex);

  /**
   * Compares two rows given by absolute positions according to the order
   * collation of the current window.
   * @param a absolute index of the first row
   * @param b absolute index of the second row
   * @return result of comparison as as in {@link Comparable#compareTo}
   */
  Expression compareRows(Expression a, Expression b);
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: IntList.java
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
package org.apache.calcite.util;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Extension to {@link ArrayList} to help build an array of <code>int</code>
 * values.
 */
@Deprecated // to be removed before 2.0
public class IntList extends ArrayList<Integer> {
  //~ Methods ----------------------------------------------------------------

  public int[] toIntArray() {
    return Ints.toArray(this);
  }

  /**
   * Converts a list of {@link Integer} objects to an array of primitive
   * <code>int</code>s.
   *
   * @param integers List of Integer objects
   * @return Array of primitive <code>int</code>s
   *
   * @deprecated Use {@link Ints#toArray(java.util.Collection)}
   */
  @Deprecated // to be removed before 2.0
  public static int[] toArray(List<Integer> integers) {
    return Ints.toArray(integers);
  }

  /**
   * Returns a list backed by an array of primitive <code>int</code> values.
   *
   * <p>The behavior is analogous to {@link Arrays#asList(Object[])}. Changes
   * to the list are reflected in the array. The list cannot be extended.
   *
   * @param args Array of primitive <code>int</code> values
   * @return List backed by array
   */
  @Deprecated // to be removed before 2.0
  public static List<Integer> asList(final int[] args) {
    return Ints.asList(args);
  }

  public ImmutableIntList asImmutable() {
    return ImmutableIntList.copyOf(this);
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/ember-codemods/ember-tracked-properties-codemod/releases/tag/calcite-1.26.0
*    Source File: NullCollation.java
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
package org.apache.calcite.config;

/** Strategy for how NULL values are to be sorted if NULLS FIRST or NULLS LAST
 * are not specified in an item in the ORDER BY clause. */
public enum NullCollation {
  /** Nulls first for DESC, nulls last for ASC. */
  HIGH,
  /** Nulls last for DESC, nulls first for ASC. */
  LOW,
  /** Nulls first for DESC and ASC. */
  FIRST,
  /** Nulls last for DESC and ASC. */
  LAST;

  /** Returns whether NULL values should appear last.
   *
   * @param desc Whether sort is descending
   */
  public boolean last(boolean desc) {
    switch (this) {
    case FIRST:
      return false;
    case LAST:
      return true;
    case LOW:
      return desc;
    case HIGH:
    default:
      return !desc;
    }
  }

  /** Returns whether a given combination of null direction and sort order is
   * the default order of nulls returned in the ORDER BY clause. */
  public boolean isDefaultOrder(boolean nullsFirst, boolean desc) {
    final boolean asc = !desc;
    final boolean nullsLast = !nullsFirst;

    switch (this) {
    case FIRST:
      return nullsFirst;
    case LAST:
      return nullsLast;
    case LOW:
      return (asc && nullsFirst) || (desc && nullsLast);
    case HIGH:
      return (asc && nullsLast) || (desc && nullsFirst);
    default:
      throw new IllegalArgumentException("Unrecognized Null Collation");
    }
  }
}

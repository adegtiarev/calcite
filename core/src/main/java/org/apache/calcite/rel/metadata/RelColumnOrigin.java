/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RelColumnOrigin.java
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
package org.apache.calcite.rel.metadata;

import org.apache.calcite.plan.RelOptTable;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * RelColumnOrigin is a data structure describing one of the origins of an
 * output column produced by a relational expression.
 */
public class RelColumnOrigin {
  //~ Instance fields --------------------------------------------------------

  private final RelOptTable originTable;

  private final int iOriginColumn;

  private final boolean isDerived;

  //~ Constructors -----------------------------------------------------------

  public RelColumnOrigin(
      RelOptTable originTable,
      int iOriginColumn,
      boolean isDerived) {
    this.originTable = originTable;
    this.iOriginColumn = iOriginColumn;
    this.isDerived = isDerived;
  }

  //~ Methods ----------------------------------------------------------------

  /** Returns table of origin. */
  public RelOptTable getOriginTable() {
    return originTable;
  }

  /** Returns the 0-based index of column in origin table; whether this ordinal
   * is flattened or unflattened depends on whether UDT flattening has already
   * been performed on the relational expression which produced this
   * description. */
  public int getOriginColumnOrdinal() {
    return iOriginColumn;
  }

  /**
   * Consider the query <code>select a+b as c, d as e from t</code>. The
   * output column c has two origins (a and b), both of them derived. The
   * output column d as one origin (c), which is not derived.
   *
   * @return false if value taken directly from column in origin table; true
   * otherwise
   */
  public boolean isDerived() {
    return isDerived;
  }

  // override Object
  @Override public boolean equals(@Nullable Object obj) {
    if (!(obj instanceof RelColumnOrigin)) {
      return false;
    }
    RelColumnOrigin other = (RelColumnOrigin) obj;
    return originTable.getQualifiedName().equals(
        other.originTable.getQualifiedName())
        && (iOriginColumn == other.iOriginColumn)
        && (isDerived == other.isDerived);
  }

  // override Object
  @Override public int hashCode() {
    return originTable.getQualifiedName().hashCode()
        + iOriginColumn + (isDerived ? 313 : 0);
  }
}

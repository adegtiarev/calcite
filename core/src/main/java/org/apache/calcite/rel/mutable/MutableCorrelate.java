/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MutableCorrelate.java
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
package org.apache.calcite.rel.mutable;

import org.apache.calcite.rel.core.CorrelationId;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.util.ImmutableBitSet;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/** Mutable equivalent of {@link org.apache.calcite.rel.core.Correlate}. */
public class MutableCorrelate extends MutableBiRel {
  public final CorrelationId correlationId;
  public final ImmutableBitSet requiredColumns;
  public final JoinRelType joinType;

  private MutableCorrelate(
      RelDataType rowType,
      MutableRel left,
      MutableRel right,
      CorrelationId correlationId,
      ImmutableBitSet requiredColumns,
      JoinRelType joinType) {
    super(MutableRelType.CORRELATE, left.cluster, rowType, left, right);
    this.correlationId = correlationId;
    this.requiredColumns = requiredColumns;
    this.joinType = joinType;
  }

  /**
   * Creates a MutableCorrelate.
   *
   * @param rowType         Row type
   * @param left            Left input relational expression
   * @param right           Right input relational expression
   * @param correlationId   Variable name for the row of left input
   * @param requiredColumns Required columns
   * @param joinType        Join type
   */
  public static MutableCorrelate of(RelDataType rowType, MutableRel left,
      MutableRel right, CorrelationId correlationId,
      ImmutableBitSet requiredColumns, JoinRelType joinType) {
    return new MutableCorrelate(rowType, left, right, correlationId,
        requiredColumns, joinType);
  }

  @Override public boolean equals(@Nullable Object obj) {
    return obj == this
        || obj instanceof MutableCorrelate
        && correlationId.equals(
            ((MutableCorrelate) obj).correlationId)
        && requiredColumns.equals(
            ((MutableCorrelate) obj).requiredColumns)
        && joinType == ((MutableCorrelate) obj).joinType
        && left.equals(((MutableCorrelate) obj).left)
        && right.equals(((MutableCorrelate) obj).right);
  }

  @Override public int hashCode() {
    return Objects.hash(left, right, correlationId, requiredColumns, joinType);
  }

  @Override public StringBuilder digest(StringBuilder buf) {
    return buf.append("Correlate(correlationId: ").append(correlationId)
        .append(", requiredColumns: ").append(requiredColumns)
        .append(", joinType: ").append(joinType)
        .append(")");
  }

  @Override public MutableRel clone() {
    return MutableCorrelate.of(rowType, left.clone(),
        right.clone(), correlationId, requiredColumns, joinType);
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MutableSort.java
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

import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rex.RexNode;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/** Mutable equivalent of {@link org.apache.calcite.rel.core.Sort}. */
public class MutableSort extends MutableSingleRel {
  public final RelCollation collation;
  public final @Nullable RexNode offset;
  public final @Nullable RexNode fetch;

  private MutableSort(MutableRel input, RelCollation collation,
      @Nullable RexNode offset, @Nullable RexNode fetch) {
    super(MutableRelType.SORT, input.rowType, input);
    this.collation = collation;
    this.offset = offset;
    this.fetch = fetch;
  }

  /**
   * Creates a MutableSort.
   *
   * @param input     Input relational expression
   * @param collation Array of sort specifications
   * @param offset    Expression for number of rows to discard before returning
   *                  first row
   * @param fetch     Expression for number of rows to fetch
   */
  public static MutableSort of(MutableRel input, RelCollation collation,
      @Nullable RexNode offset, @Nullable RexNode fetch) {
    return new MutableSort(input, collation, offset, fetch);
  }

  @Override public boolean equals(@Nullable Object obj) {
    return obj == this
        || obj instanceof MutableSort
        && collation.equals(((MutableSort) obj).collation)
        && Objects.equals(offset, ((MutableSort) obj).offset)
        && Objects.equals(fetch, ((MutableSort) obj).fetch)
        && input.equals(((MutableSort) obj).input);
  }

  @Override public int hashCode() {
    return Objects.hash(input, collation, offset, fetch);
  }

  @Override public StringBuilder digest(StringBuilder buf) {
    buf.append("Sort(collation: ").append(collation);
    if (offset != null) {
      buf.append(", offset: ").append(offset);
    }
    if (fetch != null) {
      buf.append(", fetch: ").append(fetch);
    }
    return buf.append(")");
  }

  @Override public MutableRel clone() {
    return MutableSort.of(input.clone(), collation, offset, fetch);
  }
}

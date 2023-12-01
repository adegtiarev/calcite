/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MutableUncollect.java
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

import org.apache.calcite.rel.type.RelDataType;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/** Mutable equivalent of {@link org.apache.calcite.rel.core.Uncollect}. */
public class MutableUncollect extends MutableSingleRel {
  public final boolean withOrdinality;

  private MutableUncollect(RelDataType rowType,
      MutableRel input, boolean withOrdinality) {
    super(MutableRelType.UNCOLLECT, rowType, input);
    this.withOrdinality = withOrdinality;
  }

  /**
   * Creates a MutableUncollect.
   *
   * @param rowType         Row type
   * @param input           Input relational expression
   * @param withOrdinality  Whether the output contains an extra
   *                        {@code ORDINALITY} column
   */
  public static MutableUncollect of(RelDataType rowType,
      MutableRel input, boolean withOrdinality) {
    return new MutableUncollect(rowType, input, withOrdinality);
  }

  @Override public boolean equals(@Nullable Object obj) {
    return obj == this
        || obj instanceof MutableUncollect
        && withOrdinality == ((MutableUncollect) obj).withOrdinality
        && input.equals(((MutableUncollect) obj).input);
  }

  @Override public int hashCode() {
    return Objects.hash(input, withOrdinality);
  }

  @Override public StringBuilder digest(StringBuilder buf) {
    return buf.append("Uncollect(withOrdinality: ")
        .append(withOrdinality).append(")");
  }

  @Override public MutableRel clone() {
    return MutableUncollect.of(rowType, input.clone(), withOrdinality);
  }
}

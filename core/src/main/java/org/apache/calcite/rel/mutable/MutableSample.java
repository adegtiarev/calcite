/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MutableSample.java
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

import org.apache.calcite.plan.RelOptSamplingParameters;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/** Mutable equivalent of {@link org.apache.calcite.rel.core.Sample}. */
public class MutableSample extends MutableSingleRel {
  public final RelOptSamplingParameters params;

  private MutableSample(MutableRel input, RelOptSamplingParameters params) {
    super(MutableRelType.SAMPLE, input.rowType, input);
    this.params = params;
  }

  /**
   * Creates a MutableSample.
   *
   * @param input   Input relational expression
   * @param params  parameters necessary to produce a sample of a relation
   */
  public static MutableSample of(
      MutableRel input, RelOptSamplingParameters params) {
    return new MutableSample(input, params);
  }

  @Override public boolean equals(@Nullable Object obj) {
    return obj == this
        || obj instanceof MutableSample
        && params.equals(((MutableSample) obj).params)
        && input.equals(((MutableSample) obj).input);
  }

  @Override public int hashCode() {
    return Objects.hash(input, params);
  }

  @Override public StringBuilder digest(StringBuilder buf) {
    return buf.append("Sample(mode: ")
        .append(params.isBernoulli() ? "bernoulli" : "system")
        .append("rate")
        .append(params.getSamplingPercentage())
        .append("repeatableSeed")
        .append(params.isRepeatable() ? params.getRepeatableSeed() : "-")
        .append(")");
  }

  @Override public MutableRel clone() {
    return MutableSample.of(input.clone(), params);
  }
}

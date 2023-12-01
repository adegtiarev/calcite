/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: RelReferentialConstraintImpl.java
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
package org.apache.calcite.rel;

import org.apache.calcite.util.mapping.IntPair;

import com.google.common.collect.ImmutableList;

import java.util.List;

/** RelOptReferentialConstraint base implementation. */
public class RelReferentialConstraintImpl implements RelReferentialConstraint {

  private final List<String> sourceQualifiedName;
  private final List<String> targetQualifiedName;
  private final List<IntPair> columnPairs;

  private RelReferentialConstraintImpl(List<String> sourceQualifiedName,
      List<String> targetQualifiedName, List<IntPair> columnPairs) {
    this.sourceQualifiedName = ImmutableList.copyOf(sourceQualifiedName);
    this.targetQualifiedName = ImmutableList.copyOf(targetQualifiedName);
    this.columnPairs = ImmutableList.copyOf(columnPairs);
  }

  @Override public List<String> getSourceQualifiedName() {
    return sourceQualifiedName;
  }

  @Override public List<String> getTargetQualifiedName() {
    return targetQualifiedName;
  }

  @Override public List<IntPair> getColumnPairs() {
    return columnPairs;
  }

  public static RelReferentialConstraintImpl of(List<String> sourceQualifiedName,
      List<String> targetQualifiedName, List<IntPair> columnPairs) {
    return new RelReferentialConstraintImpl(
        sourceQualifiedName, targetQualifiedName, columnPairs);
  }

  @Override public String toString() {
    return "{ " + sourceQualifiedName + ", " + targetQualifiedName + ", "
        + columnPairs + " }";
  }

}

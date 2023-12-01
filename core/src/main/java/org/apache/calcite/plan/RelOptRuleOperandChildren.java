/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/ember-codemods/ember-tracked-properties-codemod/releases/tag/calcite-1.26.0
*    Source File: RelOptRuleOperandChildren.java
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
package org.apache.calcite.plan;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Children of a {@link org.apache.calcite.plan.RelOptRuleOperand} and the
 * policy for matching them.
 *
 * <p>Often created by calling one of the following methods:
 * {@link RelOptRule#some},
 * {@link RelOptRule#none},
 * {@link RelOptRule#any},
 * {@link RelOptRule#unordered}.
 *
 * @deprecated Use {@link RelRule.OperandBuilder}
 */
@Deprecated // to be removed before 2.0
public class RelOptRuleOperandChildren {
  static final RelOptRuleOperandChildren ANY_CHILDREN =
      new RelOptRuleOperandChildren(
          RelOptRuleOperandChildPolicy.ANY,
          ImmutableList.of());

  static final RelOptRuleOperandChildren LEAF_CHILDREN =
      new RelOptRuleOperandChildren(
          RelOptRuleOperandChildPolicy.LEAF,
          ImmutableList.of());

  final RelOptRuleOperandChildPolicy policy;
  final ImmutableList<RelOptRuleOperand> operands;

  public RelOptRuleOperandChildren(
      RelOptRuleOperandChildPolicy policy,
      List<RelOptRuleOperand> operands) {
    this.policy = policy;
    this.operands = ImmutableList.copyOf(operands);
  }
}

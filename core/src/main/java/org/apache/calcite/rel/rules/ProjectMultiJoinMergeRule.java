/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ProjectMultiJoinMergeRule.java
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
package org.apache.calcite.rel.rules;

import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.RelRule;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.logical.LogicalProject;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelBuilderFactory;

import org.immutables.value.Value;

/**
 * Planner rule that pushes
 * {@link org.apache.calcite.rel.core.Project}
 * into a {@link MultiJoin},
 * creating a richer {@code MultiJoin}.
 *
 * @see org.apache.calcite.rel.rules.FilterMultiJoinMergeRule
 * @see CoreRules#PROJECT_MULTI_JOIN_MERGE
 */
@Value.Enclosing
public class ProjectMultiJoinMergeRule
    extends RelRule<ProjectMultiJoinMergeRule.Config>
    implements TransformationRule {

  /** Creates a ProjectMultiJoinMergeRule. */
  protected ProjectMultiJoinMergeRule(Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public ProjectMultiJoinMergeRule(RelBuilderFactory relBuilderFactory) {
    this(Config.DEFAULT.withRelBuilderFactory(relBuilderFactory)
        .as(Config.class));
  }

  @Deprecated // to be removed before 2.0
  public ProjectMultiJoinMergeRule(Class<? extends Project> projectClass,
      RelBuilderFactory relBuilderFactory) {
    this(Config.DEFAULT.withRelBuilderFactory(relBuilderFactory)
        .as(Config.class)
        .withOperandFor(projectClass, MultiJoin.class));
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void onMatch(RelOptRuleCall call) {
    Project project = call.rel(0);
    MultiJoin multiJoin = call.rel(1);

    // if all inputs have their projFields set, then projection information
    // has already been pushed into each input
    boolean allSet = true;
    for (int i = 0; i < multiJoin.getInputs().size(); i++) {
      if (multiJoin.getProjFields().get(i) == null) {
        allSet = false;
        break;
      }
    }
    if (allSet) {
      return;
    }

    // create a new MultiJoin that reflects the columns in the projection
    // above the MultiJoin
    final RelBuilder relBuilder = call.builder();
    MultiJoin newMultiJoin =
        RelOptUtil.projectMultiJoin(multiJoin, project);
    relBuilder.push(newMultiJoin)
        .project(project.getProjects(), project.getRowType().getFieldNames());

    call.transformTo(relBuilder.build());
  }

  /** Rule configuration. */
  @Value.Immutable
  public interface Config extends RelRule.Config {
    Config DEFAULT = ImmutableProjectMultiJoinMergeRule.Config.of()
        .withOperandFor(LogicalProject.class, MultiJoin.class);

    @Override default ProjectMultiJoinMergeRule toRule() {
      return new ProjectMultiJoinMergeRule(this);
    }

    /** Defines an operand tree for the given classes. */
    default Config withOperandFor(Class<? extends Project> projectClass,
        Class<MultiJoin> multiJoinClass) {
      return withOperandSupplier(b0 ->
          b0.operand(projectClass).oneInput(b1 ->
              b1.operand(multiJoinClass).anyInputs()))
          .as(Config.class);
    }
  }
}

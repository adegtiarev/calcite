/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ProjectMergeRule.java
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
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.core.RelFactories.ProjectFactory;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexUtil;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelBuilderFactory;
import org.apache.calcite.util.Permutation;

import org.immutables.value.Value;

import java.util.List;

/**
 * ProjectMergeRule merges a {@link org.apache.calcite.rel.core.Project} into
 * another {@link org.apache.calcite.rel.core.Project},
 * provided the projects aren't projecting identical sets of input references.
 *
 * @see CoreRules#PROJECT_MERGE
 */
@Value.Enclosing
public class ProjectMergeRule
    extends RelRule<ProjectMergeRule.Config>
    implements TransformationRule {
  /** Default amount by which complexity is allowed to increase.
   *
   * @see Config#bloat() */
  public static final int DEFAULT_BLOAT = 100;

  /** Creates a ProjectMergeRule. */
  protected ProjectMergeRule(Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public ProjectMergeRule(boolean force, int bloat,
      RelBuilderFactory relBuilderFactory) {
    this(CoreRules.PROJECT_MERGE.config.withRelBuilderFactory(relBuilderFactory)
        .as(Config.class)
        .withForce(force)
        .withBloat(bloat));
  }

  @Deprecated // to be removed before 2.0
  public ProjectMergeRule(boolean force, RelBuilderFactory relBuilderFactory) {
    this(CoreRules.PROJECT_MERGE.config.withRelBuilderFactory(relBuilderFactory)
        .as(Config.class)
        .withForce(force));
  }

  @Deprecated // to be removed before 2.0
  public ProjectMergeRule(boolean force, ProjectFactory projectFactory) {
    this(CoreRules.PROJECT_MERGE.config.withRelBuilderFactory(RelBuilder.proto(projectFactory))
        .as(Config.class)
        .withForce(force));
  }

  //~ Methods ----------------------------------------------------------------

  @Override public boolean matches(RelOptRuleCall call) {
    final Project topProject = call.rel(0);
    final Project bottomProject = call.rel(1);
    return topProject.getConvention() == bottomProject.getConvention();
  }

  @Override public void onMatch(RelOptRuleCall call) {
    final Project topProject = call.rel(0);
    final Project bottomProject = call.rel(1);
    final RelBuilder relBuilder = call.builder();

    // If one or both projects are permutations, short-circuit the complex logic
    // of building a RexProgram.
    final Permutation topPermutation = topProject.getPermutation();
    if (topPermutation != null) {
      if (topPermutation.isIdentity()) {
        // Let ProjectRemoveRule handle this.
        return;
      }
      final Permutation bottomPermutation = bottomProject.getPermutation();
      if (bottomPermutation != null) {
        if (bottomPermutation.isIdentity()) {
          // Let ProjectRemoveRule handle this.
          return;
        }
        final Permutation product = topPermutation.product(bottomPermutation);
        relBuilder.push(bottomProject.getInput());
        relBuilder.project(relBuilder.fields(product),
            topProject.getRowType().getFieldNames());
        call.transformTo(relBuilder.build());
        return;
      }
    }

    // If we're not in force mode and the two projects reference identical
    // inputs, then return and let ProjectRemoveRule replace the projects.
    if (!config.force()) {
      if (RexUtil.isIdentity(topProject.getProjects(),
          topProject.getInput().getRowType())) {
        return;
      }
    }

    final List<RexNode> newProjects =
        RelOptUtil.pushPastProjectUnlessBloat(topProject.getProjects(),
            bottomProject, config.bloat());
    if (newProjects == null) {
      // Merged projects are significantly more complex. Do not merge.
      return;
    }
    final RelNode input = bottomProject.getInput();
    if (RexUtil.isIdentity(newProjects, input.getRowType())) {
      if (config.force()
          || input.getRowType().getFieldNames()
              .equals(topProject.getRowType().getFieldNames())) {
        call.transformTo(input);
        return;
      }
    }

    // replace the two projects with a combined projection
    relBuilder.push(bottomProject.getInput());
    relBuilder.project(newProjects, topProject.getRowType().getFieldNames());
    call.transformTo(relBuilder.build());
  }

  /** Rule configuration. */
  @Value.Immutable
  public interface Config extends RelRule.Config {
    Config DEFAULT = ImmutableProjectMergeRule.Config.of()
        .withOperandFor(Project.class);

    @Override default ProjectMergeRule toRule() {
      return new ProjectMergeRule(this);
    }

    /** Limit how much complexity can increase during merging.
     * Default is {@link #DEFAULT_BLOAT} (100). */
    @Value.Default default int bloat() {
      return ProjectMergeRule.DEFAULT_BLOAT;
    }

    /** Sets {@link #bloat()}. */
    Config withBloat(int bloat);

    /** Whether to always merge projects, default true. */
    @Value.Default default boolean force() {
      return true;
    }

    /** Sets {@link #force()}. */
    Config withForce(boolean force);

    /** Defines an operand tree for the given classes. */
    default Config withOperandFor(Class<? extends Project> projectClass) {
      return withOperandSupplier(b0 ->
          b0.operand(projectClass).oneInput(b1 ->
              b1.operand(projectClass).anyInputs()))
          .as(Config.class);
    }
  }
}

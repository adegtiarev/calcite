/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: EnumerableProjectRule.java
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
package org.apache.calcite.adapter.enumerable;

import org.apache.calcite.plan.Convention;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.logical.LogicalProject;

/**
 * Rule to convert a {@link LogicalProject} to an {@link EnumerableProject}.
 * You may provide a custom config to convert other nodes that extend {@link Project}.
 *
 * @see EnumerableRules#ENUMERABLE_PROJECT_RULE
 */
class EnumerableProjectRule extends ConverterRule {
  /** Default configuration. */
  static final Config DEFAULT_CONFIG = Config.INSTANCE
      .as(Config.class)
      .withConversion(LogicalProject.class, p -> !p.containsOver(),
          Convention.NONE, EnumerableConvention.INSTANCE,
          "EnumerableProjectRule")
      .withRuleFactory(EnumerableProjectRule::new);

  /** Creates an EnumerableProjectRule. */
  protected EnumerableProjectRule(Config config) {
    super(config);
  }

  @Override public RelNode convert(RelNode rel) {
    final Project project = (Project) rel;
    return EnumerableProject.create(
        convert(project.getInput(),
            project.getInput().getTraitSet()
                .replace(EnumerableConvention.INSTANCE)),
        project.getProjects(),
        project.getRowType());
  }
}

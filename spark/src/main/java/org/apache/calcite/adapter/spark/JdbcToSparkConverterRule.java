/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/ember-codemods/ember-tracked-properties-codemod/releases/tag/calcite-1.26.0
*    Source File: JdbcToSparkConverterRule.java
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
package org.apache.calcite.adapter.spark;

import org.apache.calcite.adapter.jdbc.JdbcConvention;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.tools.RelBuilderFactory;

/**
 * Rule to convert a relational expression from
 * {@link org.apache.calcite.adapter.jdbc.JdbcConvention} to
 * {@link org.apache.calcite.adapter.spark.SparkRel#CONVENTION Spark convention}.
 */
public class JdbcToSparkConverterRule extends ConverterRule {
  /** Creates a JdbcToSparkConverterRule. */
  public static JdbcToSparkConverterRule create(JdbcConvention out) {
    return Config.INSTANCE
        .withConversion(RelNode.class, out, SparkRel.CONVENTION,
            "JdbcToSparkConverterRule")
        .withRuleFactory(JdbcToSparkConverterRule::new)
        .toRule(JdbcToSparkConverterRule.class);
  }

  @Deprecated // to be removed before 2.0
  public JdbcToSparkConverterRule(JdbcConvention out,
      RelBuilderFactory relBuilderFactory) {
    this(create(out).config.withRelBuilderFactory(relBuilderFactory)
        .as(Config.class));
  }

  /** Called from the Config. */
  protected JdbcToSparkConverterRule(Config config) {
    super(config);
  }

  @Override public RelNode convert(RelNode rel) {
    RelTraitSet newTraitSet = rel.getTraitSet().replace(getOutTrait());
    return new JdbcToSparkConverter(rel.getCluster(), newTraitSet, rel);
  }
}

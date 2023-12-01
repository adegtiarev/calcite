/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: ElasticsearchToEnumerableConverterRule.java
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
package org.apache.calcite.adapter.elasticsearch;

import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;

/**
 * Rule to convert a relational expression from
 * {@link ElasticsearchRel#CONVENTION} to {@link EnumerableConvention}.
 */
public class ElasticsearchToEnumerableConverterRule extends ConverterRule {
  /** Singleton instance of ElasticsearchToEnumerableConverterRule. */
  static final ConverterRule INSTANCE = Config.INSTANCE
      .withConversion(RelNode.class, ElasticsearchRel.CONVENTION,
          EnumerableConvention.INSTANCE,
          "ElasticsearchToEnumerableConverterRule")
      .withRuleFactory(ElasticsearchToEnumerableConverterRule::new)
      .toRule(ElasticsearchToEnumerableConverterRule.class);

  /** Called from the Config. */
  protected ElasticsearchToEnumerableConverterRule(Config config) {
    super(config);
  }

  @Override public RelNode convert(RelNode relNode) {
    RelTraitSet newTraitSet = relNode.getTraitSet().replace(getOutConvention());
    return new ElasticsearchToEnumerableConverter(relNode.getCluster(), newTraitSet, relNode);
  }
}

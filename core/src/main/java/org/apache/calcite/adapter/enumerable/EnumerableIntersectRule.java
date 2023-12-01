/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: EnumerableIntersectRule.java
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
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.Intersect;
import org.apache.calcite.rel.logical.LogicalIntersect;

/**
 * Rule to convert a {@link LogicalIntersect} to an {@link EnumerableIntersect}.
 * You may provide a custom config to convert other nodes that extend {@link Intersect}.
 *
 * @see EnumerableRules#ENUMERABLE_INTERSECT_RULE
 */
class EnumerableIntersectRule extends ConverterRule {
  /** Default configuration. */
  public static final Config DEFAULT_CONFIG = Config.INSTANCE
      .withConversion(LogicalIntersect.class, Convention.NONE,
          EnumerableConvention.INSTANCE, "EnumerableIntersectRule")
      .withRuleFactory(EnumerableIntersectRule::new);

  /** Called from the Config. */
  protected EnumerableIntersectRule(Config config) {
    super(config);
  }

  @Override public RelNode convert(RelNode rel) {
    final Intersect intersect = (Intersect) rel;
    final EnumerableConvention out = EnumerableConvention.INSTANCE;
    final RelTraitSet traitSet = intersect.getTraitSet().replace(out);
    return new EnumerableIntersect(rel.getCluster(), traitSet,
        convertList(intersect.getInputs(), out), intersect.all);
  }
}

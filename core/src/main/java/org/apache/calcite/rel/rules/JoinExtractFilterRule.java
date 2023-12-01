/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: JoinExtractFilterRule.java
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

import org.apache.calcite.rel.core.Join;
import org.apache.calcite.rel.logical.LogicalJoin;
import org.apache.calcite.tools.RelBuilderFactory;

import org.immutables.value.Value;

/**
 * Rule to convert an
 * {@link org.apache.calcite.rel.logical.LogicalJoin inner join} to a
 * {@link org.apache.calcite.rel.logical.LogicalFilter filter} on top of a
 * {@link org.apache.calcite.rel.logical.LogicalJoin cartesian inner join}.
 *
 * <p>One benefit of this transformation is that after it, the join condition
 * can be combined with conditions and expressions above the join. It also makes
 * the <code>FennelCartesianJoinRule</code> applicable.
 *
 * <p>Can be configured to match any sub-class of
 * {@link org.apache.calcite.rel.core.Join}, not just
 * {@link org.apache.calcite.rel.logical.LogicalJoin}.
 *
 * @see CoreRules#JOIN_EXTRACT_FILTER
 */
@Value.Enclosing
public final class JoinExtractFilterRule extends AbstractJoinExtractFilterRule {

  /** Creates a JoinExtractFilterRule. */
  JoinExtractFilterRule(Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public JoinExtractFilterRule(Class<? extends Join> clazz,
      RelBuilderFactory relBuilderFactory) {
    this(Config.DEFAULT
        .withRelBuilderFactory(relBuilderFactory)
        .withOperandSupplier(b ->
            b.operand(clazz).anyInputs())
        .as(Config.class));
  }

  /** Rule configuration. */
  @Value.Immutable
  public interface Config extends AbstractJoinExtractFilterRule.Config {
    Config DEFAULT = ImmutableJoinExtractFilterRule.Config.of()
        .withOperandSupplier(b -> b.operand(LogicalJoin.class).anyInputs());

    @Override default JoinExtractFilterRule toRule() {
      return new JoinExtractFilterRule(this);
    }
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: TableScanRule.java
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
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.RelRule;
import org.apache.calcite.plan.ViewExpanders;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalTableScan;
import org.apache.calcite.tools.RelBuilderFactory;

import org.immutables.value.Value;

/**
 * Planner rule that converts a
 * {@link org.apache.calcite.rel.logical.LogicalTableScan} to the result
 * of calling {@link RelOptTable#toRel}.
 *
 * @deprecated {@code org.apache.calcite.rel.core.RelFactories.TableScanFactoryImpl}
 * has called {@link RelOptTable#toRel(RelOptTable.ToRelContext)}.
 */
@Deprecated // to be removed before 2.0
@Value.Enclosing
@SuppressWarnings("deprecation")
public class TableScanRule extends RelRule<RelRule.Config>
    implements TransformationRule {
  //~ Static fields/initializers ---------------------------------------------

  public static final TableScanRule INSTANCE =
      Config.DEFAULT.toRule();

  //~ Constructors -----------------------------------------------------------

  /** Creates a TableScanRule. */
  protected TableScanRule(RelRule.Config config) {
    super(config);
  }

  @Deprecated // to be removed before 2.0
  public TableScanRule(RelBuilderFactory relBuilderFactory) {
    this(Config.DEFAULT.withRelBuilderFactory(relBuilderFactory));
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void onMatch(RelOptRuleCall call) {
    final LogicalTableScan oldRel = call.rel(0);
    RelNode newRel =
        oldRel.getTable().toRel(
            ViewExpanders.simpleContext(oldRel.getCluster()));
    call.transformTo(newRel);
  }

  /** Rule configuration. */
  @Value.Immutable
  @SuppressWarnings("deprecation")
  public interface Config extends RelRule.Config {
    Config DEFAULT = ImmutableTableScanRule.Config.of()
        .withOperandSupplier(b -> b.operand(LogicalTableScan.class).noInputs());

    @Override default TableScanRule toRule() {
      return new TableScanRule(this);
    }
  }

}

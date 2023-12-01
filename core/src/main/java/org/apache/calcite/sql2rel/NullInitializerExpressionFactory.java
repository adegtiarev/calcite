/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: NullInitializerExpressionFactory.java
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
package org.apache.calcite.sql2rel;

import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.schema.ColumnStrategy;
import org.apache.calcite.sql.SqlFunction;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.BiFunction;

/**
 * An implementation of {@link InitializerExpressionFactory} that always supplies NULL.
 */
public class NullInitializerExpressionFactory implements InitializerExpressionFactory {

  public static final InitializerExpressionFactory INSTANCE =
      new NullInitializerExpressionFactory();

  public NullInitializerExpressionFactory() {
  }

  @SuppressWarnings("deprecation")
  @Override public boolean isGeneratedAlways(RelOptTable table, int iColumn) {
    switch (generationStrategy(table, iColumn)) {
    case VIRTUAL:
    case STORED:
      return true;
    default:
      return false;
    }
  }

  @Override public ColumnStrategy generationStrategy(RelOptTable table, int iColumn) {
    return table.getRowType().getFieldList().get(iColumn).getType().isNullable()
        ? ColumnStrategy.NULLABLE
        : ColumnStrategy.NOT_NULLABLE;
  }

  @Override public RexNode newColumnDefaultValue(RelOptTable table, int iColumn,
      InitializerContext context) {
    final RelDataType fieldType =
        table.getRowType().getFieldList().get(iColumn).getType();
    return context.getRexBuilder().makeNullLiteral(fieldType);
  }

  @Override public @Nullable BiFunction<
      InitializerContext, RelNode, RelNode> postExpressionConversionHook() {
    return null;
  }

  @Override public RexNode newAttributeInitializer(RelDataType type,
      SqlFunction constructor, int iAttribute, List<RexNode> constructorArgs,
      InitializerContext context) {
    final RelDataType fieldType =
        type.getFieldList().get(iAttribute).getType();
    return context.getRexBuilder().makeNullLiteral(fieldType);
  }
}

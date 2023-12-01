/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.33.0
*    Source File: NaryOperatorConverter.java
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
package org.apache.calcite.adapter.druid;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlOperator;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Converts Calcite n-ary operators to Druid expressions, for example
 * {@code arg1 Op arg2 Op arg3}.
 */
public class NaryOperatorConverter implements DruidSqlOperatorConverter {
  private final SqlOperator operator;
  private final String druidOperatorName;

  public NaryOperatorConverter(SqlOperator operator, String druidOperatorName) {
    this.operator = Objects.requireNonNull(operator, "operator");
    this.druidOperatorName = Objects.requireNonNull(druidOperatorName, "druidOperatorName");
  }

  @Override public SqlOperator calciteOperator() {
    return operator;
  }

  @Override public @Nullable String toDruidExpression(RexNode rexNode, RelDataType rowType,
      DruidQuery druidQuery) {
    final RexCall call = (RexCall) rexNode;
    final List<String> druidExpressions = DruidExpressions.toDruidExpressions(
        druidQuery, rowType,
        call.getOperands());
    if (druidExpressions == null) {
      return null;
    }
    return DruidExpressions.nAryOperatorCall(druidOperatorName, druidExpressions);
  }
}

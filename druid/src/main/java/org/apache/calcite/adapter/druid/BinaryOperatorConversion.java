/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: BinaryOperatorConversion.java
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

import java.util.List;

/**
 * Binary operator conversion utility class; used to convert expressions like
 * {@code exp1 Operator exp2}.
 */
public class BinaryOperatorConversion implements DruidSqlOperatorConverter {
  private final SqlOperator operator;
  private final String druidOperator;

  public BinaryOperatorConversion(final SqlOperator operator, final String druidOperator) {
    this.operator = operator;
    this.druidOperator = druidOperator;
  }

  @Override public SqlOperator calciteOperator() {
    return operator;
  }

  @Override public String toDruidExpression(RexNode rexNode, RelDataType rowType,
      DruidQuery druidQuery) {

    final RexCall call = (RexCall) rexNode;

    final List<String> druidExpressions = DruidExpressions.toDruidExpressions(
        druidQuery, rowType,
        call.getOperands());
    if (druidExpressions == null) {
      return null;
    }
    if (druidExpressions.size() != 2) {
      throw new IllegalStateException(
          DruidQuery.format("Got binary operator[%s] with %s args?", operator.getName(),
              druidExpressions.size()));
    }

    return DruidQuery
        .format("(%s %s %s)", druidExpressions.get(0), druidOperator, druidExpressions.get(1));
  }
}

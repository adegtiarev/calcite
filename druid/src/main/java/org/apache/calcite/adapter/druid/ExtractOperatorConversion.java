/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/cf-testorg/calcite-test/releases/tag/calcite-1.26.0-rc0
*    Source File: ExtractOperatorConversion.java
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

import org.apache.calcite.avatica.util.DateTimeUtils;
import org.apache.calcite.avatica.util.TimeUnitRange;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeName;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.TimeZone;

/**
 * Time extract operator conversion for expressions like
 * {@code EXTRACT(timeUnit FROM arg)}.
 *
 * <p>Unit can be SECOND, MINUTE, HOUR, DAY (day of month), DOW (day of week),
 * DOY (day of year), WEEK (week of week year), MONTH (1 through 12), QUARTER (1
 * through 4), or YEAR.
 */
public class ExtractOperatorConversion implements DruidSqlOperatorConverter {
  private static final Map<TimeUnitRange, String> EXTRACT_UNIT_MAP =
      ImmutableMap.<TimeUnitRange, String>builder()
          .put(TimeUnitRange.SECOND, "SECOND")
          .put(TimeUnitRange.MINUTE, "MINUTE")
          .put(TimeUnitRange.HOUR, "HOUR")
          .put(TimeUnitRange.DAY, "DAY")
          .put(TimeUnitRange.DOW, "DOW")
          .put(TimeUnitRange.DOY, "DOY")
          .put(TimeUnitRange.WEEK, "WEEK")
          .put(TimeUnitRange.MONTH, "MONTH")
          .put(TimeUnitRange.QUARTER, "QUARTER")
          .put(TimeUnitRange.YEAR, "YEAR")
          .build();

  @Override public SqlOperator calciteOperator() {
    return SqlStdOperatorTable.EXTRACT;
  }

  @Override public String toDruidExpression(
      RexNode rexNode, RelDataType rowType, DruidQuery query) {

    final RexCall call = (RexCall) rexNode;
    final RexLiteral flag = (RexLiteral) call.getOperands().get(0);
    final TimeUnitRange calciteUnit = (TimeUnitRange) flag.getValue();
    final RexNode arg = call.getOperands().get(1);

    final String input = DruidExpressions.toDruidExpression(arg, rowType, query);
    if (input == null) {
      return null;
    }

    final String druidUnit = EXTRACT_UNIT_MAP.get(calciteUnit);
    if (druidUnit == null) {
      return null;
    }

    final TimeZone tz =
        arg.getType().getSqlTypeName() == SqlTypeName.TIMESTAMP_WITH_LOCAL_TIME_ZONE
            ? TimeZone.getTimeZone(query.getConnectionConfig().timeZone())
            : DateTimeUtils.UTC_ZONE;
    return DruidExpressions.applyTimeExtract(input, druidUnit, tz);
  }
}

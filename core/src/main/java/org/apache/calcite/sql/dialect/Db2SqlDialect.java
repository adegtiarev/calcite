/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Db2SqlDialect.java
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
package org.apache.calcite.sql.dialect;

import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlIntervalLiteral;
import org.apache.calcite.sql.SqlIntervalQualifier;
import org.apache.calcite.sql.SqlWriter;

/**
 * A <code>SqlDialect</code> implementation for the IBM DB2 database.
 */
public class Db2SqlDialect extends SqlDialect {
  public static final SqlDialect.Context DEFAULT_CONTEXT = SqlDialect.EMPTY_CONTEXT
      .withDatabaseProduct(SqlDialect.DatabaseProduct.DB2);

  public static final SqlDialect DEFAULT = new Db2SqlDialect(DEFAULT_CONTEXT);

  /** Creates a Db2SqlDialect. */
  public Db2SqlDialect(Context context) {
    super(context);
  }

  @Override public boolean supportsCharSet() {
    return false;
  }

  @Override public boolean hasImplicitTableAlias() {
    return false;
  }

  @Override public void unparseSqlIntervalQualifier(SqlWriter writer,
      SqlIntervalQualifier qualifier, RelDataTypeSystem typeSystem) {

    // DB2 supported qualifiers. Singular form of these keywords are also acceptable.
    // YEAR/YEARS
    // MONTH/MONTHS
    // DAY/DAYS
    // HOUR/HOURS
    // MINUTE/MINUTES
    // SECOND/SECONDS

    switch (qualifier.timeUnitRange) {
    case YEAR:
    case MONTH:
    case DAY:
    case HOUR:
    case MINUTE:
    case SECOND:
    case MICROSECOND:
      final String timeUnit = qualifier.timeUnitRange.startUnit.name();
      writer.keyword(timeUnit);
      break;
    default:
      throw new AssertionError("Unsupported type: " + qualifier.timeUnitRange);
    }

    if (null != qualifier.timeUnitRange.endUnit) {
      throw new AssertionError("Unsupported end unit: "
          + qualifier.timeUnitRange.endUnit);
    }
  }

  @Override public void unparseSqlIntervalLiteral(SqlWriter writer,
      SqlIntervalLiteral literal, int leftPrec, int rightPrec) {
    // A duration is a positive or negative number representing an interval of time.
    // If one operand is a date, the other labeled duration of YEARS, MONTHS, or DAYS.
    // If one operand is a time, the other must be labeled duration of HOURS, MINUTES, or SECONDS.
    // If one operand is a timestamp, the other operand can be any of teh duration.

    SqlIntervalLiteral.IntervalValue interval =
        literal.getValueAs(SqlIntervalLiteral.IntervalValue.class);
    if (interval.getSign() == -1) {
      writer.print("-");
    }
    writer.literal(interval.getIntervalLiteral());
    unparseSqlIntervalQualifier(writer, interval.getIntervalQualifier(),
        RelDataTypeSystem.DEFAULT);
  }

}

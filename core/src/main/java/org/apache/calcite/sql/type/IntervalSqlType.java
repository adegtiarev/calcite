/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: IntervalSqlType.java
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
package org.apache.calcite.sql.type;

import org.apache.calcite.avatica.util.TimeUnit;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactoryImpl;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlIntervalQualifier;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.dialect.AnsiSqlDialect;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.apache.calcite.sql.util.SqlString;

import java.util.Objects;

/**
 * IntervalSqlType represents a standard SQL datetime interval type.
 */
public class IntervalSqlType extends AbstractSqlType {
  //~ Instance fields --------------------------------------------------------

  private final RelDataTypeSystem typeSystem;
  private final SqlIntervalQualifier intervalQualifier;

  //~ Constructors -----------------------------------------------------------

  /**
   * Constructs an IntervalSqlType. This should only be called from a factory
   * method.
   */
  public IntervalSqlType(RelDataTypeSystem typeSystem,
      SqlIntervalQualifier intervalQualifier,
      boolean isNullable) {
    super(intervalQualifier.typeName(), isNullable, null);
    this.typeSystem = Objects.requireNonNull(typeSystem, "typeSystem");
    this.intervalQualifier = Objects.requireNonNull(intervalQualifier, "intervalQualifier");
    computeDigest();
  }

  //~ Methods ----------------------------------------------------------------

  @Override protected void generateTypeString(StringBuilder sb, boolean withDetail) {
    sb.append("INTERVAL ");
    final SqlDialect dialect = AnsiSqlDialect.DEFAULT;
    final SqlWriterConfig config = SqlPrettyWriter.config()
        .withAlwaysUseParentheses(false)
        .withSelectListItemsOnSeparateLines(false)
        .withIndentation(0)
        .withDialect(dialect);
    final SqlPrettyWriter writer = new SqlPrettyWriter(config);
    intervalQualifier.unparse(writer, 0, 0);
    final String sql = writer.toString();
    sb.append(new SqlString(dialect, sql).getSql());
  }

  @Override public SqlIntervalQualifier getIntervalQualifier() {
    return intervalQualifier;
  }

  /**
   * Combines two IntervalTypes and returns the result. E.g. the result of
   * combining<br>
   * <code>INTERVAL DAY TO HOUR</code><br>
   * with<br>
   * <code>INTERVAL SECOND</code> is<br>
   * <code>INTERVAL DAY TO SECOND</code>
   */
  public IntervalSqlType combine(
      RelDataTypeFactoryImpl typeFactory,
      IntervalSqlType that) {
    assert this.typeName.isYearMonth() == that.typeName.isYearMonth();
    boolean nullable = isNullable || that.isNullable;
    TimeUnit thisStart = Objects.requireNonNull(typeName.getStartUnit());
    TimeUnit thisEnd = typeName.getEndUnit();
    final TimeUnit thatStart =
        Objects.requireNonNull(that.typeName.getStartUnit());
    final TimeUnit thatEnd = that.typeName.getEndUnit();

    int secondPrec =
        this.intervalQualifier.getStartPrecisionPreservingDefault();
    final int fracPrec =
        SqlIntervalQualifier.combineFractionalSecondPrecisionPreservingDefault(
            typeSystem,
            this.intervalQualifier,
            that.intervalQualifier);

    if (thisStart.ordinal() > thatStart.ordinal()) {
      thisEnd = thisStart;
      thisStart = thatStart;
      secondPrec =
          that.intervalQualifier.getStartPrecisionPreservingDefault();
    } else if (thisStart.ordinal() == thatStart.ordinal()) {
      secondPrec =
          SqlIntervalQualifier.combineStartPrecisionPreservingDefault(
              typeFactory.getTypeSystem(),
              this.intervalQualifier,
              that.intervalQualifier);
    } else if (null == thisEnd || thisEnd.ordinal() < thatStart.ordinal()) {
      thisEnd = thatStart;
    }

    if (null != thatEnd) {
      if (null == thisEnd || thisEnd.ordinal() < thatEnd.ordinal()) {
        thisEnd = thatEnd;
      }
    }

    RelDataType intervalType =
        typeFactory.createSqlIntervalType(
            new SqlIntervalQualifier(
                thisStart,
                secondPrec,
                thisEnd,
                fracPrec,
                SqlParserPos.ZERO));
    intervalType =
        typeFactory.createTypeWithNullability(
            intervalType,
            nullable);
    return (IntervalSqlType) intervalType;
  }

  @Override public int getPrecision() {
    return intervalQualifier.getStartPrecision(typeSystem);
  }

  @Override public int getScale() {
    return intervalQualifier.getFractionalSecondPrecision(typeSystem);
  }
}

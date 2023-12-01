/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlOrderBy.java
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
package org.apache.calcite.sql;

import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Parse tree node that represents an {@code ORDER BY} on a query other than a
 * {@code SELECT} (e.g. {@code VALUES} or {@code UNION}).
 *
 * <p>It is a purely syntactic operator, and is eliminated by
 * {@link org.apache.calcite.sql.validate.SqlValidatorImpl#performUnconditionalRewrites}
 * and replaced with the ORDER_OPERAND of SqlSelect.</p>
 */
public class SqlOrderBy extends SqlCall {
  public static final SqlSpecialOperator OPERATOR = new Operator() {
    @SuppressWarnings("argument.type.incompatible")
    @Override public SqlCall createCall(@Nullable SqlLiteral functionQualifier,
        SqlParserPos pos, @Nullable SqlNode... operands) {
      return new SqlOrderBy(pos, operands[0], (SqlNodeList) operands[1],
          operands[2], operands[3]);
    }
  };

  public final SqlNode query;
  public final SqlNodeList orderList;
  public final @Nullable SqlNode offset;
  public final @Nullable SqlNode fetch;

  //~ Constructors -----------------------------------------------------------

  public SqlOrderBy(SqlParserPos pos, SqlNode query, SqlNodeList orderList,
      @Nullable SqlNode offset, @Nullable SqlNode fetch) {
    super(pos);
    this.query = query;
    this.orderList = orderList;
    this.offset = offset;
    this.fetch = fetch;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public SqlKind getKind() {
    return SqlKind.ORDER_BY;
  }

  @Override public SqlOperator getOperator() {
    return OPERATOR;
  }

  @SuppressWarnings("nullness")
  @Override public List<SqlNode> getOperandList() {
    return ImmutableNullableList.of(query, orderList, offset, fetch);
  }

  /** Definition of {@code ORDER BY} operator. */
  private static class Operator extends SqlSpecialOperator {
    private Operator() {
      // NOTE:  make precedence lower then SELECT to avoid extra parens
      super("ORDER BY", SqlKind.ORDER_BY, 0);
    }

    @Override public SqlSyntax getSyntax() {
      return SqlSyntax.POSTFIX;
    }

    @Override public void unparse(
        SqlWriter writer,
        SqlCall call,
        int leftPrec,
        int rightPrec) {
      SqlOrderBy orderBy = (SqlOrderBy) call;
      final SqlWriter.Frame frame =
          writer.startList(SqlWriter.FrameTypeEnum.ORDER_BY);
      orderBy.query.unparse(writer, getLeftPrec(), getRightPrec());
      if (orderBy.orderList != SqlNodeList.EMPTY) {
        writer.sep(getName());
        writer.list(SqlWriter.FrameTypeEnum.ORDER_BY_LIST, SqlWriter.COMMA,
            orderBy.orderList);
      }
      if (orderBy.offset != null || orderBy.fetch != null) {
        writer.fetchOffset(orderBy.fetch, orderBy.offset);
      }
      writer.endList(frame);
    }
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: SqlWith.java
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
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorScope;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * The WITH clause of a query. It wraps a SELECT, UNION, or INTERSECT.
 */
public class SqlWith extends SqlCall {
  public SqlNodeList withList;
  public SqlNode body;

  //~ Constructors -----------------------------------------------------------

  public SqlWith(SqlParserPos pos, SqlNodeList withList, SqlNode body) {
    super(pos);
    this.withList = withList;
    this.body = body;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public SqlKind getKind() {
    return SqlKind.WITH;
  }

  @Override public SqlOperator getOperator() {
    return SqlWithOperator.INSTANCE;
  }

  @Override public List<SqlNode> getOperandList() {
    return ImmutableList.of(withList, body);
  }

  @SuppressWarnings("assignment.type.incompatible")
  @Override public void setOperand(int i, @Nullable SqlNode operand) {
    switch (i) {
    case 0:
      withList = (SqlNodeList) operand;
      break;
    case 1:
      body = operand;
      break;
    default:
      throw new AssertionError(i);
    }
  }

  @Override public void validate(SqlValidator validator,
      SqlValidatorScope scope) {
    validator.validateWith(this, scope);
  }

  /**
   * SqlWithOperator is used to represent a WITH clause of a query. It wraps
   * a SELECT, UNION, or INTERSECT.
   */
  private static class SqlWithOperator extends SqlSpecialOperator {
    private static final SqlWithOperator INSTANCE = new SqlWithOperator();

    private SqlWithOperator() {
      // NOTE:  make precedence lower then SELECT to avoid extra parens
      super("WITH", SqlKind.WITH, 2);
    }

    //~ Methods ----------------------------------------------------------------

    @Override public void unparse(
        SqlWriter writer,
        SqlCall call,
        int leftPrec,
        int rightPrec) {
      final SqlWith with = (SqlWith) call;
      final SqlWriter.Frame frame =
          writer.startList(SqlWriter.FrameTypeEnum.WITH, "WITH", "");
      final SqlWriter.Frame frame1 = writer.startList("", "");
      for (SqlNode node : with.withList) {
        writer.sep(",");
        node.unparse(writer, 0, 0);
      }
      writer.endList(frame1);
      final SqlWriter.Frame frame2 =
          writer.startList(SqlWriter.FrameTypeEnum.SIMPLE);
      with.body.unparse(writer, 100, 100);
      writer.endList(frame2);
      writer.endList(frame);
    }


    @SuppressWarnings("argument.type.incompatible")
    @Override public SqlCall createCall(@Nullable SqlLiteral functionQualifier,
        SqlParserPos pos, @Nullable SqlNode... operands) {
      return new SqlWith(pos, (SqlNodeList) operands[0], operands[1]);
    }

    @Override public void validateCall(SqlCall call,
        SqlValidator validator,
        SqlValidatorScope scope,
        SqlValidatorScope operandScope) {
      validator.validateWith((SqlWith) call, scope);
    }
  }
}

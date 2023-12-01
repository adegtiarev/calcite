/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlCreateFunction.java
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
package org.apache.calcite.sql.ddl;

import org.apache.calcite.sql.SqlCreate;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.Pair;
import org.apache.calcite.util.Util;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Parse tree for {@code CREATE FUNCTION} statement.
 */
public class SqlCreateFunction extends SqlCreate {
  private final SqlIdentifier name;
  private final SqlNode className;
  private final SqlNodeList usingList;

  private static final SqlSpecialOperator OPERATOR =
      new SqlSpecialOperator("CREATE FUNCTION", SqlKind.CREATE_FUNCTION);

  /** Creates a SqlCreateFunction. */
  public SqlCreateFunction(SqlParserPos pos, boolean replace,
      boolean ifNotExists, SqlIdentifier name,
      SqlNode className, SqlNodeList usingList) {
    super(OPERATOR, pos, replace, ifNotExists);
    this.name = Objects.requireNonNull(name, "name");
    this.className = className;
    this.usingList = Objects.requireNonNull(usingList, "usingList");
    Preconditions.checkArgument(usingList.size() % 2 == 0);
  }

  @Override public void unparse(SqlWriter writer, int leftPrec,
      int rightPrec) {
    writer.keyword(getReplace() ? "CREATE OR REPLACE" : "CREATE");
    writer.keyword("FUNCTION");
    if (ifNotExists) {
      writer.keyword("IF NOT EXISTS");
    }
    name.unparse(writer, 0, 0);
    writer.keyword("AS");
    className.unparse(writer, 0, 0);
    if (usingList.size() > 0) {
      writer.keyword("USING");
      final SqlWriter.Frame frame =
          writer.startList(SqlWriter.FrameTypeEnum.SIMPLE);
      for (Pair<SqlLiteral, SqlLiteral> using : pairs()) {
        writer.sep(",");
        using.left.unparse(writer, 0, 0); // FILE, URL or ARCHIVE
        using.right.unparse(writer, 0, 0); // e.g. 'file:foo/bar.jar'
      }
      writer.endList(frame);
    }
  }

  @SuppressWarnings("unchecked")
  private List<Pair<SqlLiteral, SqlLiteral>> pairs() {
    return Util.pairs((List) usingList);
  }

  @Override public SqlOperator getOperator() {
    return OPERATOR;
  }

  @Override public List<SqlNode> getOperandList() {
    return Arrays.asList(name, className, usingList);
  }
}

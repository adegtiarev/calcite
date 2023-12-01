/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: ContextSqlValidator.java
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
package org.apache.calcite.jdbc;

import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.validate.SqlValidatorImpl;

import com.google.common.collect.ImmutableList;

/**
 * A SqlValidator with schema and type factory of the given
 * {@link org.apache.calcite.jdbc.CalcitePrepare.Context}.
 *
 * <p>This class is only used to derive data type for DDL sql node.
 * Usually we deduce query sql node data type(i.e. the {@code SqlSelect})
 * during the validation phrase. DDL nodes don't have validation,
 * they can be executed directly through
 * {@link org.apache.calcite.server.DdlExecutor}.
 *
 * <p>During the execution, {@link org.apache.calcite.sql.SqlDataTypeSpec} uses
 * this validator to derive its type.
 */
public class ContextSqlValidator extends SqlValidatorImpl {

  /**
   * Create a {@code ContextSqlValidator}.
   * @param context Prepare context.
   * @param mutable Whether to get the mutable schema.
   */
  public ContextSqlValidator(CalcitePrepare.Context context, boolean mutable) {
    super(SqlStdOperatorTable.instance(), getCatalogReader(context, mutable),
        context.getTypeFactory(), Config.DEFAULT);
  }

  private static CalciteCatalogReader getCatalogReader(
      CalcitePrepare.Context context, boolean mutable) {
    return new CalciteCatalogReader(
        mutable ? context.getMutableRootSchema() : context.getRootSchema(),
        ImmutableList.of(),
        context.getTypeFactory(),
        CalciteConnectionConfig.DEFAULT);
  }
}

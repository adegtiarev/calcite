/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ExplicitOperatorBinding.java
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

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.runtime.CalciteException;
import org.apache.calcite.runtime.Resources;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.validate.SqlValidatorException;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * <code>ExplicitOperatorBinding</code> implements {@link SqlOperatorBinding}
 * via an underlying array of known operand types.
 */
public class ExplicitOperatorBinding extends SqlOperatorBinding {
  //~ Instance fields --------------------------------------------------------

  private final List<RelDataType> types;
  private final @Nullable SqlOperatorBinding delegate;

  //~ Constructors -----------------------------------------------------------

  public ExplicitOperatorBinding(
      SqlOperatorBinding delegate,
      List<RelDataType> types) {
    this(
        delegate,
        delegate.getTypeFactory(),
        delegate.getOperator(),
        types);
  }

  public ExplicitOperatorBinding(
      RelDataTypeFactory typeFactory,
      SqlOperator operator,
      List<RelDataType> types) {
    this(null, typeFactory, operator, types);
  }

  private ExplicitOperatorBinding(
      @Nullable SqlOperatorBinding delegate,
      RelDataTypeFactory typeFactory,
      SqlOperator operator,
      List<RelDataType> types) {
    super(typeFactory, operator);
    this.types = types;
    this.delegate = delegate;
  }

  //~ Methods ----------------------------------------------------------------

  // implement SqlOperatorBinding
  @Override public int getOperandCount() {
    return types.size();
  }

  // implement SqlOperatorBinding
  @Override public RelDataType getOperandType(int ordinal) {
    return types.get(ordinal);
  }

  @Override public CalciteException newError(
      Resources.ExInst<SqlValidatorException> e) {
    if (delegate != null) {
      return delegate.newError(e);
    } else {
      return SqlUtil.newContextException(SqlParserPos.ZERO, e);
    }
  }

  @Override public boolean isOperandNull(int ordinal, boolean allowCast) {
    // NOTE jvs 1-May-2006:  This call is only relevant
    // for SQL validation, so anywhere else, just say
    // everything's OK.
    return false;
  }
}

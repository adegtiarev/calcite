/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlUserDefinedFunction.java
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
package org.apache.calcite.sql.validate;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.schema.Function;
import org.apache.calcite.schema.FunctionParameter;
import org.apache.calcite.sql.SqlFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.type.SqlOperandMetadata;
import org.apache.calcite.sql.type.SqlOperandTypeChecker;
import org.apache.calcite.sql.type.SqlOperandTypeInference;
import org.apache.calcite.sql.type.SqlReturnTypeInference;
import org.apache.calcite.util.Util;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
* User-defined scalar function.
 *
 * <p>Created by the validator, after resolving a function call to a function
 * defined in a Calcite schema.</p>
*/
public class SqlUserDefinedFunction extends SqlFunction {
  public final Function function;

  @Deprecated // to be removed before 2.0
  public SqlUserDefinedFunction(SqlIdentifier opName,
      SqlReturnTypeInference returnTypeInference,
      SqlOperandTypeInference operandTypeInference,
      @Nullable SqlOperandTypeChecker operandTypeChecker,
      List<RelDataType> paramTypes,
      Function function) {
    this(opName, SqlKind.OTHER_FUNCTION, returnTypeInference,
        operandTypeInference,
        operandTypeChecker instanceof SqlOperandMetadata
            ? (SqlOperandMetadata) operandTypeChecker : null, function);
    Util.discard(paramTypes); // no longer used
  }

  /** Creates a {@link SqlUserDefinedFunction}. */
  public SqlUserDefinedFunction(SqlIdentifier opName, SqlKind kind,
      SqlReturnTypeInference returnTypeInference,
      SqlOperandTypeInference operandTypeInference,
      @Nullable SqlOperandMetadata operandMetadata,
      Function function) {
    this(opName, kind, returnTypeInference, operandTypeInference,
        operandMetadata, function, SqlFunctionCategory.USER_DEFINED_FUNCTION);
  }

  /** Constructor used internally and by derived classes. */
  protected SqlUserDefinedFunction(SqlIdentifier opName, SqlKind kind,
      SqlReturnTypeInference returnTypeInference,
      SqlOperandTypeInference operandTypeInference,
      @Nullable SqlOperandMetadata operandMetadata,
      Function function,
      SqlFunctionCategory category) {
    super(Util.last(opName.names), opName, kind, returnTypeInference,
        operandTypeInference, operandMetadata, category);
    this.function = function;
  }

  @Override public @Nullable SqlOperandMetadata getOperandTypeChecker() {
    return (@Nullable SqlOperandMetadata) super.getOperandTypeChecker();
  }

  /**
   * Returns function that implements given operator call.
   * @return function that implements given operator call
   */
  public Function getFunction() {
    return function;
  }

  @SuppressWarnings("deprecation")
  @Override public List<String> getParamNames() {
    return Util.transform(function.getParameters(), FunctionParameter::getName);
  }
}

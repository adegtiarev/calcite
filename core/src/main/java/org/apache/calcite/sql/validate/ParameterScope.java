/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ParameterScope.java
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
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * A scope which contains nothing besides a few parameters. Like
 * {@link EmptyScope} (which is its base class), it has no parent scope.
 *
 * @see ParameterNamespace
 */
public class ParameterScope extends EmptyScope {
  //~ Instance fields --------------------------------------------------------

  /**
   * Map from the simple names of the parameters to types of the parameters
   * ({@link RelDataType}).
   */
  private final Map<String, RelDataType> nameToTypeMap;

  //~ Constructors -----------------------------------------------------------

  public ParameterScope(
      SqlValidatorImpl validator,
      Map<String, RelDataType> nameToTypeMap) {
    super(validator);
    this.nameToTypeMap = nameToTypeMap;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public SqlQualified fullyQualify(SqlIdentifier identifier) {
    return SqlQualified.create(this, 1, null, identifier);
  }

  @Override public SqlValidatorScope getOperandScope(SqlCall call) {
    return this;
  }

  @Override public @Nullable RelDataType resolveColumn(String name, SqlNode ctx) {
    return nameToTypeMap.get(name);
  }
}

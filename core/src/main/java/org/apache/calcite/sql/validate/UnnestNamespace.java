/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: UnnestNamespace.java
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
import org.apache.calcite.sql.SqlUnnestOperator;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Namespace for UNNEST.
 */
class UnnestNamespace extends AbstractNamespace {
  //~ Instance fields --------------------------------------------------------

  private final SqlCall unnest;
  private final SqlValidatorScope scope;

  //~ Constructors -----------------------------------------------------------

  UnnestNamespace(
      SqlValidatorImpl validator,
      SqlCall unnest,
      SqlValidatorScope scope,
      SqlNode enclosingNode) {
    super(validator, enclosingNode);
    assert scope != null;
    assert unnest.getOperator() instanceof SqlUnnestOperator;
    this.unnest = unnest;
    this.scope = scope;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public @Nullable SqlValidatorTable getTable() {
    final SqlNode toUnnest = unnest.operand(0);
    if (toUnnest instanceof SqlIdentifier) {
      // When operand of SqlIdentifier type does not have struct, fake a table
      // for UnnestNamespace
      final SqlIdentifier id = (SqlIdentifier) toUnnest;
      final SqlQualified qualified = this.scope.fullyQualify(id);
      if (qualified.namespace == null) {
        return null;
      }
      return qualified.namespace.getTable();
    }
    return null;
  }

  @Override protected RelDataType validateImpl(RelDataType targetRowType) {
    // Validate the call and its arguments, and infer the return type.
    validator.validateCall(unnest, scope);
    RelDataType type =
        unnest.getOperator().validateOperands(validator, scope, unnest);

    return toStruct(type, unnest);
  }

  @Override public @Nullable SqlNode getNode() {
    return unnest;
  }
}

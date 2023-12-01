/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: TableScope.java
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

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * The name-resolution scope of a LATERAL TABLE clause.
 *
 * <p>The objects visible are those in the parameters found on the left side of
 * the LATERAL TABLE clause, and objects inherited from the parent scope.
 */
class TableScope extends ListScope {
  //~ Instance fields --------------------------------------------------------

  private final SqlNode node;

  //~ Constructors -----------------------------------------------------------

  /**
   * Creates a scope corresponding to a LATERAL TABLE clause.
   *
   * @param parent  Parent scope
   */
  TableScope(SqlValidatorScope parent, SqlNode node) {
    super(Objects.requireNonNull(parent, "parent"));
    this.node = Objects.requireNonNull(node, "node");
  }

  //~ Methods ----------------------------------------------------------------

  @Override public SqlNode getNode() {
    return node;
  }

  @Override public boolean isWithin(@Nullable SqlValidatorScope scope2) {
    if (this == scope2) {
      return true;
    }
    SqlValidatorScope s = getValidator().getSelectScope((SqlSelect) node);
    return s.isWithin(scope2);
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: FieldNamespace.java
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
import org.apache.calcite.sql.SqlNode;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link SqlValidatorNamespace} for a field of a record.
 *
 * <p>A field is not a very interesting namespace - except if the field has a
 * record or multiset type - but this class exists to make fields behave
 * similarly to other records for purposes of name resolution.
 */
class FieldNamespace extends AbstractNamespace {
  //~ Constructors -----------------------------------------------------------

  /**
   * Creates a FieldNamespace.
   *
   * @param validator Validator
   * @param dataType  Data type of field
   */
  FieldNamespace(
      SqlValidatorImpl validator,
      RelDataType dataType) {
    super(validator, null);
    assert dataType != null;
    this.rowType = dataType;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public void setType(RelDataType type) {
    throw new UnsupportedOperationException();
  }

  @Override protected RelDataType validateImpl(RelDataType targetRowType) {
    return requireNonNull(rowType, "rowType");
  }

  @Override public @Nullable SqlNode getNode() {
    return null;
  }

  @Override public @Nullable SqlValidatorNamespace lookupChild(String name) {
    if (requireNonNull(rowType, "rowType").isStruct()) {
      return validator.lookupFieldNamespace(
          rowType,
          name);
    }
    return null;
  }

  @Override public boolean fieldExists(String name) {
    return false;
  }
}

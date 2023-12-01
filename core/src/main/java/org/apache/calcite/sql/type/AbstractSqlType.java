/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: AbstractSqlType.java
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
package org.apache.calcite.sql.type;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFamily;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.RelDataTypeImpl;
import org.apache.calcite.rel.type.RelDataTypePrecedenceList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for SQL implementations of {@link RelDataType}.
 */
public abstract class AbstractSqlType
    extends RelDataTypeImpl
    implements Cloneable, Serializable {
  //~ Instance fields --------------------------------------------------------

  protected final SqlTypeName typeName;
  protected boolean isNullable;

  //~ Constructors -----------------------------------------------------------

  /**
   * Creates an AbstractSqlType.
   *
   * @param typeName   Type name
   * @param isNullable Whether nullable
   * @param fields     Fields of type, or null if not a record type
   */
  protected AbstractSqlType(
      SqlTypeName typeName,
      boolean isNullable,
      @Nullable List<? extends RelDataTypeField> fields) {
    super(fields);
    this.typeName = Objects.requireNonNull(typeName, "typeName");
    this.isNullable = isNullable || (typeName == SqlTypeName.NULL);
  }

  //~ Methods ----------------------------------------------------------------

  @Override public SqlTypeName getSqlTypeName() {
    return typeName;
  }

  @Override public boolean isNullable() {
    return isNullable;
  }

  @Override public RelDataTypeFamily getFamily() {
    SqlTypeFamily family = typeName.getFamily();
    // If typename does not have family, treat the current type as the only member its family
    return family != null ? family : this;
  }

  @Override public RelDataTypePrecedenceList getPrecedenceList() {
    RelDataTypePrecedenceList list =
        SqlTypeExplicitPrecedenceList.getListForType(this);
    if (list != null) {
      return list;
    }
    return super.getPrecedenceList();
  }
}

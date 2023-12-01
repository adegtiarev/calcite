/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: DynamicRecordTypeImpl.java
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
package org.apache.calcite.rel.type;

import org.apache.calcite.sql.type.SqlTypeExplicitPrecedenceList;
import org.apache.calcite.sql.type.SqlTypeFamily;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Implementation of {@link RelDataType} for a dynamic table.
 *
 * <p>It's used during SQL validation, where the field list is mutable for
 * the getField() call. After SQL validation, a normal {@link RelDataTypeImpl}
 * with an immutable field list takes the place of the DynamicRecordTypeImpl
 * instance.
 */
public class DynamicRecordTypeImpl extends DynamicRecordType {
  private final RelDataTypeHolder holder;

  /** Creates a DynamicRecordTypeImpl. */
  @SuppressWarnings("method.invocation.invalid")
  public DynamicRecordTypeImpl(RelDataTypeFactory typeFactory) {
    this.holder = new RelDataTypeHolder(typeFactory);
    computeDigest();
  }

  @Override public List<RelDataTypeField> getFieldList() {
    return holder.getFieldList();
  }

  @Override public int getFieldCount() {
    return holder.getFieldCount();
  }

  @Override public @Nullable RelDataTypeField getField(String fieldName,
      boolean caseSensitive, boolean elideRecord) {
    final Pair<RelDataTypeField, Boolean> pair =
        holder.getFieldOrInsert(fieldName, caseSensitive);
    // If a new field is added, we should re-compute the digest.
    if (pair.right) {
      computeDigest();
    }

    return pair.left;
  }

  @Override public List<String> getFieldNames() {
    return holder.getFieldNames();
  }

  @Override public SqlTypeName getSqlTypeName() {
    return SqlTypeName.ROW;
  }

  @Override public RelDataTypePrecedenceList getPrecedenceList() {
    return new SqlTypeExplicitPrecedenceList(ImmutableList.of());
  }

  @Override protected void generateTypeString(StringBuilder sb, boolean withDetail) {
    sb.append("(DynamicRecordRow").append(getFieldNames()).append(")");
  }

  @Override public boolean isStruct() {
    return true;
  }

  @Override public RelDataTypeFamily getFamily() {
    SqlTypeFamily family = getSqlTypeName().getFamily();
    return family != null ? family : this;
  }

}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.33.0
*    Source File: RelDataTypeHolder.java
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

import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;
import org.apache.calcite.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Holding the expandable list of fields for dynamic table.
 */
class RelDataTypeHolder {
  private final List<RelDataTypeField> fields = new ArrayList<>();
  private final RelDataTypeFactory typeFactory;

  RelDataTypeHolder(RelDataTypeFactory typeFactory) {
    this.typeFactory = typeFactory;
  }

  public List<RelDataTypeField> getFieldList() {
    return fields;
  }

  public int getFieldCount() {
    return fields.size();
  }

  /**
   * Get field if exists, otherwise inserts a new field. The new field by default will have "any"
   * type, except for the dynamic star field.
   *
   * @param fieldName Request field name
   * @param caseSensitive Case Sensitive
   * @return A pair of RelDataTypeField and Boolean. Boolean indicates whether a new field is added
   * to this holder.
   */
  Pair<RelDataTypeField, Boolean> getFieldOrInsert(String fieldName, boolean caseSensitive) {
    // First check if this field name exists in our field list
    for (RelDataTypeField f : fields) {
      if (Util.matches(caseSensitive, f.getName(), fieldName)) {
        return Pair.of(f, false);
      }
      // A dynamic star field matches any field
      if (f.getType().getSqlTypeName() == SqlTypeName.DYNAMIC_STAR) {
        return Pair.of(f, false);
      }
    }

    final SqlTypeName typeName = DynamicRecordType.isDynamicStarColName(fieldName)
        ? SqlTypeName.DYNAMIC_STAR : SqlTypeName.ANY;

    // This field does not exist in our field list; add it
    RelDataTypeField newField = new RelDataTypeFieldImpl(
        fieldName,
        fields.size(),
        typeFactory.createTypeWithNullability(typeFactory.createSqlType(typeName), true));

    // Add the name to our list of field names
    fields.add(newField);

    return Pair.of(newField, true);
  }

  public List<String> getFieldNames() {
    return Pair.left(fields);
  }

}

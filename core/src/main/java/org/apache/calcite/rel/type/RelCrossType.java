/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: RelCrossType.java
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

import org.apache.calcite.linq4j.Ord;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Type of the cartesian product of two or more sets of records.
 *
 * <p>Its fields are those of its constituent records, but unlike a
 * {@link RelRecordType}, those fields' names are not necessarily distinct.</p>
 */
public class RelCrossType extends RelDataTypeImpl {
  //~ Instance fields --------------------------------------------------------

  public final ImmutableList<RelDataType> types;

  //~ Constructors -----------------------------------------------------------

  /**
   * Creates a cartesian product type. This should only be called from a
   * factory method.
   */
  @SuppressWarnings("method.invocation.invalid")
  public RelCrossType(
      List<RelDataType> types,
      List<RelDataTypeField> fields) {
    super(fields);
    this.types = ImmutableList.copyOf(types);
    assert types.size() >= 1;
    for (RelDataType type : types) {
      assert !(type instanceof RelCrossType);
    }
    computeDigest();
  }

  //~ Methods ----------------------------------------------------------------

  @Override public boolean isStruct() {
    return false;
  }

  @Override protected void generateTypeString(StringBuilder sb, boolean withDetail) {
    sb.append("CrossType(");
    for (Ord<RelDataType> type : Ord.zip(types)) {
      if (type.i > 0) {
        sb.append(", ");
      }
      if (withDetail) {
        sb.append(type.e.getFullTypeString());
      } else {
        sb.append(type.e.toString());
      }
    }
    sb.append(")");
  }
}

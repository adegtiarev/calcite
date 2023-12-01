/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ArrayLengthRecordField.java
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
package org.apache.calcite.linq4j.tree;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Length field of a RecordType.
 */
public class ArrayLengthRecordField implements Types.RecordField {
  private final String fieldName;
  private final Class clazz;

  public ArrayLengthRecordField(String fieldName, Class clazz) {
    assert fieldName != null : "fieldName should not be null";
    assert clazz != null : "clazz should not be null";
    this.fieldName = fieldName;
    this.clazz = clazz;
  }

  @Override public boolean nullable() {
    return false;
  }

  @Override public String getName() {
    return fieldName;
  }

  @Override public Type getType() {
    return int.class;
  }

  @Override public int getModifiers() {
    return 0;
  }

  @Override public Object get(@Nullable Object o) throws IllegalAccessException {
    return Array.getLength(requireNonNull(o, "o"));
  }

  @Override public Type getDeclaringClass() {
    return clazz;
  }

  @Override public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ArrayLengthRecordField that = (ArrayLengthRecordField) o;

    if (!clazz.equals(that.clazz)) {
      return false;
    }
    if (!fieldName.equals(that.fieldName)) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    return Objects.hash(fieldName, clazz);
  }
}

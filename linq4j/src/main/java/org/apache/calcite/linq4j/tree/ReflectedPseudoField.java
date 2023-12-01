/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ReflectedPseudoField.java
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

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Represents a PseudoField that is implemented via a Java reflection
 * {@link Field}.
 */
public class ReflectedPseudoField implements PseudoField {
  private final Field field;

  public ReflectedPseudoField(Field field) {
    assert field != null : "field should not be null";
    this.field = field;
  }

  @Override public String getName() {
    return field.getName();
  }

  @Override public Type getType() {
    return field.getType();
  }

  @Override public int getModifiers() {
    return field.getModifiers();
  }

  @Override public @Nullable Object get(@Nullable Object o) throws IllegalAccessException {
    return field.get(o);
  }

  @Override public Class<?> getDeclaringClass() {
    return field.getDeclaringClass();
  }

  @Override public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ReflectedPseudoField that = (ReflectedPseudoField) o;

    if (!field.equals(that.field)) {
      return false;
    }

    return true;
  }

  @Override public int hashCode() {
    return field.hashCode();
  }
}

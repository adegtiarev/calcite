/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: DelegatingTypeSystem.java
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

import org.checkerframework.checker.nullness.qual.Nullable;

/** Implementation of {@link org.apache.calcite.rel.type.RelDataTypeSystem}
 * that sends all methods to an underlying object. */
public class DelegatingTypeSystem implements RelDataTypeSystem {
  private final RelDataTypeSystem typeSystem;

  /** Creates a {@code DelegatingTypeSystem}. */
  protected DelegatingTypeSystem(RelDataTypeSystem typeSystem) {
    this.typeSystem = typeSystem;
  }

  @Override public int getMaxScale(SqlTypeName typeName) {
    return typeSystem.getMaxScale(typeName);
  }

  @Override public int getDefaultPrecision(SqlTypeName typeName) {
    return typeSystem.getDefaultPrecision(typeName);
  }

  @Override public int getMaxPrecision(SqlTypeName typeName) {
    return typeSystem.getMaxPrecision(typeName);
  }

  @Override public int getMaxNumericScale() {
    return typeSystem.getMaxNumericScale();
  }

  @Override public int getMaxNumericPrecision() {
    return typeSystem.getMaxNumericPrecision();
  }

  @Override public @Nullable String getLiteral(SqlTypeName typeName, boolean isPrefix) {
    return typeSystem.getLiteral(typeName, isPrefix);
  }

  @Override public boolean isCaseSensitive(SqlTypeName typeName) {
    return typeSystem.isCaseSensitive(typeName);
  }

  @Override public boolean isAutoincrement(SqlTypeName typeName) {
    return typeSystem.isAutoincrement(typeName);
  }

  @Override public int getNumTypeRadix(SqlTypeName typeName) {
    return typeSystem.getNumTypeRadix(typeName);
  }

  @Override public RelDataType deriveSumType(RelDataTypeFactory typeFactory,
      RelDataType argumentType) {
    return typeSystem.deriveSumType(typeFactory, argumentType);
  }

  @Override public RelDataType deriveAvgAggType(RelDataTypeFactory typeFactory,
      RelDataType argumentType) {
    return typeSystem.deriveAvgAggType(typeFactory, argumentType);
  }

  @Override public RelDataType deriveCovarType(RelDataTypeFactory typeFactory,
      RelDataType arg0Type, RelDataType arg1Type) {
    return typeSystem.deriveCovarType(typeFactory, arg0Type, arg1Type);
  }

  @Override public RelDataType deriveFractionalRankType(RelDataTypeFactory typeFactory) {
    return typeSystem.deriveFractionalRankType(typeFactory);
  }

  @Override public RelDataType deriveRankType(RelDataTypeFactory typeFactory) {
    return typeSystem.deriveRankType(typeFactory);
  }

  @Override public boolean isSchemaCaseSensitive() {
    return typeSystem.isSchemaCaseSensitive();
  }

  @Override public boolean shouldConvertRaggedUnionTypesToVarying() {
    return typeSystem.shouldConvertRaggedUnionTypesToVarying();
  }
}

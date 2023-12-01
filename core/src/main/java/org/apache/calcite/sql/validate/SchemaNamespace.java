/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.33.0
*    Source File: SchemaNamespace.java
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
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.util.Util;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/** Namespace based on a schema.
 *
 * <p>The visible names are tables and sub-schemas.
 */
class SchemaNamespace extends AbstractNamespace {
  /** The path of this schema. */
  private final ImmutableList<String> names;

  /** Creates a SchemaNamespace. */
  SchemaNamespace(SqlValidatorImpl validator, ImmutableList<String> names) {
    super(validator, null);
    this.names = Objects.requireNonNull(names, "names");
  }

  @Override protected RelDataType validateImpl(RelDataType targetRowType) {
    final RelDataTypeFactory.Builder builder =
        validator.getTypeFactory().builder();
    for (SqlMoniker moniker
        : validator.catalogReader.getAllSchemaObjectNames(names)) {
      final List<String> names1 = moniker.getFullyQualifiedNames();
      final SqlValidatorTable table = requireNonNull(
          validator.catalogReader.getTable(names1),
          () -> "table " + names1 + " is not found in scope " + names);
      builder.add(Util.last(names1), table.getRowType());
    }
    return builder.build();
  }

  @Override public @Nullable SqlNode getNode() {
    return null;
  }
}

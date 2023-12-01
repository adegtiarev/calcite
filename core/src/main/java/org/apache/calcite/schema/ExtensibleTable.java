/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/Abzhanov/piscine-go/releases/tag/calcite-1.26.0
*    Source File: ExtensibleTable.java
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
package org.apache.calcite.schema;

import org.apache.calcite.rel.type.RelDataTypeField;

import java.util.List;

/**
 * Table whose row type can be extended to include extra fields.
 *
 * <p>In some storage systems, especially those with "late schema", there may
 * exist columns that have values in the table but which are not declared in
 * the table schema. However, a particular query may wish to reference these
 * columns as if they were defined in the schema. Calling the {@link #extend}
 * method creates a temporarily extended table schema.
 *
 * <p>If the table implements extended interfaces such as
 * {@link org.apache.calcite.schema.ScannableTable},
 * {@link org.apache.calcite.schema.FilterableTable} or
 * {@link org.apache.calcite.schema.ProjectableFilterableTable}, you may wish
 * to make the table returned from {@link #extend} implement these interfaces
 * as well.
 */
public interface ExtensibleTable extends Table {
  /** Returns a table that has the row type of this table plus the given
   * fields. */
  Table extend(List<RelDataTypeField> fields);

  /** Returns the starting offset of the first extended column, which may differ
   * from the field count when the table stores metadata columns that are not
   * counted in the row-type field count. */
  int getExtendedColumnOffset();
}

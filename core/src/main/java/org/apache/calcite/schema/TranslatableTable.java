/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: TranslatableTable.java
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

import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;

/**
 * Extension to {@link Table} that specifies how it is to be translated to
 * a {@link org.apache.calcite.rel.RelNode relational expression}.
 *
 * <p>It is optional for a Table to implement this interface. If Table does
 * not implement this interface, it will be converted to an
 * {@link org.apache.calcite.adapter.enumerable.EnumerableTableScan}.
 * Generally a Table will implement this interface to
 * create a particular subclass of RelNode, and also register rules that act
 * on that particular subclass of RelNode.
 */
public interface TranslatableTable extends Table {
  /** Converts this table into a {@link RelNode relational expression}. */
  RelNode toRel(
      RelOptTable.ToRelContext context,
      RelOptTable relOptTable);
}

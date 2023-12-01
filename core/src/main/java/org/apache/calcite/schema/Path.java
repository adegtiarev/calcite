/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/ember-codemods/ember-tracked-properties-codemod/releases/tag/calcite-1.26.0
*    Source File: Path.java
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

import org.apache.calcite.util.Pair;

import java.util.List;
import java.util.RandomAccess;

/**
 * Path from a root schema to a particular object (schema, table, function).
 *
 * <p>Examples:
 * <ul>
 *   <li>The root schema has a single element [(root, "")].
 *   <li>A direct child "foo" of the root schema has a two elements
 *   [(root, ""), (child, "foo")].
 * </ul>
 */
public interface Path extends List<Pair<String, Schema>>, RandomAccess {
  /** Returns the parent path, or null if the path is empty. */
  Path parent();

  /** Returns the names of this path, not including the name of the root. */
  List<String> names();

  /** Returns the schemas of this path. */
  List<Schema> schemas();
}

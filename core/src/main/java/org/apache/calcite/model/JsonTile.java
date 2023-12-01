/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: JsonTile.java
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
package org.apache.calcite.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Materialized view within a {@link org.apache.calcite.model.JsonLattice}.
 *
 * <p>A tile is defined in terms of its dimensionality (the grouping columns,
 * drawn from the lattice) and measures (aggregate functions applied to
 * lattice columns).
 *
 * <p>Occurs within {@link JsonLattice#tiles}.
 *
 * @see JsonRoot Description of schema elements
 */
public class JsonTile {
  /** List of dimensions that define this tile.
   *
   * <p>Each dimension is a column from the lattice. The list of dimensions
   * defines the level of aggregation, like a {@code GROUP BY} clause.
   *
   * <p>Required, but may be empty. Each element is either a string
   * (the unique label of the column within the lattice)
   * or a string list (a pair consisting of a table alias and a column name).
   */
  public final List dimensions = new ArrayList();

  /** List of measures in this tile.
   *
   * <p>If not specified, uses {@link JsonLattice#defaultMeasures}.
   */
  public final List<JsonMeasure> measures;

  @JsonCreator
  public JsonTile(@JsonProperty("measures") @Nullable List<JsonMeasure> measures) {
    this.measures = measures == null
        ? ImmutableList.of(new JsonMeasure("count", null)) : measures;
  }

  public void accept(ModelHandler handler) {
    handler.visit(this);
  }
}

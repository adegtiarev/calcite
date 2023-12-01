/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: JsonTable.java
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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Table schema element.
 *
 * <p>Occurs within {@link JsonMapSchema#tables}.
 *
 * @see JsonRoot Description of schema elements
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = JsonCustomTable.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = JsonCustomTable.class, name = "custom"),
    @JsonSubTypes.Type(value = JsonView.class, name = "view") })
public abstract class JsonTable {
  /** Name of this table.
   *
   * <p>Required. Must be unique within the schema.
   */
  public final String name;

  /** Definition of the columns of this table.
   *
   * <p>Required for some kinds of type,
   * optional for others (such as {@link JsonView}).
   */
  public final List<JsonColumn> columns = new ArrayList<>();

  /** Information about whether the table can be streamed, and if so, whether
   * the history of the table is also available. */
  public final @Nullable JsonStream stream;

  protected JsonTable(String name, @Nullable JsonStream stream) {
    this.name = requireNonNull(name, "name");
    this.stream = stream;
  }

  public abstract void accept(ModelHandler handler);
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: JsonJdbcSchema.java
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

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * JSON object representing a schema that maps to a JDBC database.
 *
 * <p>Like the base class {@link JsonSchema},
 * occurs within {@link JsonRoot#schemas}.
 *
 * @see JsonRoot Description of JSON schema elements
 */
public class JsonJdbcSchema extends JsonSchema {
  /** The name of the JDBC driver class.
   *
   * <p>Optional. If not specified, uses whichever class the JDBC
   * {@link java.sql.DriverManager} chooses.
   */
  public final @Nullable String jdbcDriver;

  /** The FQN of the {@link org.apache.calcite.sql.SqlDialectFactory} implementation.
   *
   * <p>Optional. If not specified, uses whichever class the JDBC
   * {@link java.sql.DriverManager} chooses.
   */
  public final @Nullable String sqlDialectFactory;

  /** JDBC connect string, for example "jdbc:mysql://localhost/foodmart".
   */
  public final String jdbcUrl;

  /** JDBC user name.
   *
   * <p>Optional.
   */
  public final @Nullable String jdbcUser;

  /** JDBC connect string, for example "jdbc:mysql://localhost/foodmart".
   *
   * <p>Optional.
   */
  public final @Nullable String jdbcPassword;

  /** Name of the initial catalog in the JDBC data source.
   *
   * <p>Optional.
   */
  public final @Nullable String jdbcCatalog;

  /** Name of the initial schema in the JDBC data source.
   *
   * <p>Optional.
   */
  public final @Nullable String jdbcSchema;

  @JsonCreator
  public JsonJdbcSchema(
      @JsonProperty(value = "name", required = true) String name,
      @JsonProperty("path") @Nullable List<Object> path,
      @JsonProperty("cache") @Nullable Boolean cache,
      @JsonProperty("autoLattice") @Nullable Boolean autoLattice,
      @JsonProperty("jdbcDriver") @Nullable String jdbcDriver,
      @JsonProperty("sqlDialectFactory") @Nullable String sqlDialectFactory,
      @JsonProperty(value = "jdbcUrl", required = true)  String jdbcUrl,
      @JsonProperty("jdbcUser") @Nullable String jdbcUser,
      @JsonProperty("jdbcPassword") @Nullable String jdbcPassword,
      @JsonProperty("jdbcCatalog") @Nullable String jdbcCatalog,
      @JsonProperty("jdbcSchema") @Nullable String jdbcSchema) {
    super(name, path, cache, autoLattice);
    this.jdbcDriver = jdbcDriver;
    this.sqlDialectFactory = sqlDialectFactory;
    this.jdbcUrl = requireNonNull(jdbcUrl, "jdbcUrl");
    this.jdbcUser = jdbcUser;
    this.jdbcPassword = jdbcPassword;
    this.jdbcCatalog = jdbcCatalog;
    this.jdbcSchema = jdbcSchema;
  }

  @Override public void accept(ModelHandler handler) {
    handler.visit(this);
  }
}

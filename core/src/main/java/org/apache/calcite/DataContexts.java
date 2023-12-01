/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: DataContexts.java
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
package org.apache.calcite;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.schema.SchemaPlus;

import com.google.common.collect.ImmutableMap;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/** Utilities for {@link DataContext}. */
public class DataContexts {
  private DataContexts() {
  }

  /** Instance of {@link DataContext} that has no variables. */
  public static final DataContext EMPTY = new EmptyDataContext();

  /** Returns an instance of {@link DataContext} with the given map. */
  public static DataContext of(Map<String, ?> map) {
    return new MapDataContext(map);
  }

  /** Returns an instance of {@link DataContext} with the given function. */
  public static DataContext of(Function<String, ? extends @Nullable Object> fn) {
    return new FunctionDataContext(fn);
  }

  /** Returns an instance of {@link DataContext} with the given connection
   * and root schema but no variables. */
  public static DataContext of(CalciteConnection connection,
      @Nullable SchemaPlus rootSchema) {
    return new DataContextImpl(connection, rootSchema, ImmutableMap.of());
  }

  /** Implementation of {@link DataContext} that has no variables.
   *
   * <p>It is {@link Serializable} for Spark's benefit. */
  private static class EmptyDataContext implements DataContext, Serializable {
    @Override public @Nullable SchemaPlus getRootSchema() {
      return null;
    }

    @Override public JavaTypeFactory getTypeFactory() {
      throw new UnsupportedOperationException();
    }

    @Override public QueryProvider getQueryProvider() {
      throw new UnsupportedOperationException();
    }

    @Override public @Nullable Object get(String name) {
      return null;
    }
  }

  /** Implementation of {@link DataContext} backed by a Map.
   *
   * <p>Keys and values in the map must not be null. Rather than storing a null
   * value for a key, remove the key from the map; the effect will be the
   * same. */
  private static class MapDataContext extends EmptyDataContext {
    private final ImmutableMap<String, ?> map;

    MapDataContext(Map<String, ?> map) {
      this.map = ImmutableMap.copyOf(map);
    }

    @Override public @Nullable Object get(String name) {
      return map.get(name);
    }
  }

  /** Implementation of {@link DataContext} backed by a Function. */
  private static class FunctionDataContext extends EmptyDataContext {
    private final Function<String, ? extends @Nullable Object> fn;

    FunctionDataContext(Function<String, ? extends @Nullable Object> fn) {
      this.fn = requireNonNull(fn, "fn");
    }

    @Override public @Nullable Object get(String name) {
      return fn.apply(name);
    }
  }

  /** Implementation of {@link DataContext} backed by a Map. */
  private static class DataContextImpl extends MapDataContext {
    private CalciteConnection connection;
    private @Nullable SchemaPlus rootSchema;

    DataContextImpl(CalciteConnection connection,
        @Nullable SchemaPlus rootSchema, Map<String, Object> map) {
      super(map);
      this.connection = requireNonNull(connection, "connection");
      this.rootSchema = requireNonNull(rootSchema, "rootSchema");
    }

    @Override public JavaTypeFactory getTypeFactory() {
      return connection.getTypeFactory();
    }

    @Override public @Nullable SchemaPlus getRootSchema() {
      return rootSchema;
    }

    @Override public QueryProvider getQueryProvider() {
      return connection;
    }
  }
}

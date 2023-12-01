/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Contexts.java
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
package org.apache.calcite.plan;

import org.apache.calcite.config.CalciteConnectionConfig;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utilities for {@link Context}.
 */
public class Contexts {
  public static final EmptyContext EMPTY_CONTEXT = new EmptyContext();

  private Contexts() {}

  /** Returns a context that contains a
   * {@link org.apache.calcite.config.CalciteConnectionConfig}.
   *
   * @deprecated Use {@link #of}
   */
  @Deprecated // to be removed before 2.0
  public static Context withConfig(CalciteConnectionConfig config) {
    return of(config);
  }

  /** Returns a context that returns null for all inquiries. */
  public static Context empty() {
    return EMPTY_CONTEXT;
  }

  /** Returns a context that wraps an object.
   *
   * <p>A call to {@code unwrap(C)} will return {@code target} if it is an
   * instance of {@code C}.
   */
  public static Context of(Object o) {
    return new WrapContext(o);
  }

  /** Returns a context that wraps an array of objects, ignoring any nulls. */
  public static Context of(@Nullable Object... os) {
    final List<Context> contexts = new ArrayList<>();
    for (Object o : os) {
      if (o != null) {
        contexts.add(of(o));
      }
    }
    return chain(contexts);
  }

  /** Returns a context that wraps a list of contexts.
   *
   * <p>A call to {@code unwrap(C)} will return the first object that is an
   * instance of {@code C}.
   *
   * <p>If any of the contexts is a {@link Context}, recursively looks in that
   * object. Thus this method can be used to chain contexts.
   */
  public static Context chain(Context... contexts) {
    return chain(ImmutableList.copyOf(contexts));
  }

  private static Context chain(Iterable<? extends Context> contexts) {
    // Flatten any chain contexts in the list, and remove duplicates
    final List<Context> list = new ArrayList<>();
    for (Context context : contexts) {
      build(list, context);
    }
    switch (list.size()) {
    case 0:
      return empty();
    case 1:
      return list.get(0);
    default:
      return new ChainContext(ImmutableList.copyOf(list));
    }
  }

  /** Recursively populates a list of contexts. */
  private static void build(List<Context> list, Context context) {
    if (context == EMPTY_CONTEXT || list.contains(context)) {
      return;
    }
    if (context instanceof ChainContext) {
      ChainContext chainContext = (ChainContext) context;
      for (Context child : chainContext.contexts) {
        build(list, child);
      }
    } else {
      list.add(context);
    }
  }

  /** Context that wraps an object. */
  private static class WrapContext implements Context {
    final Object target;

    WrapContext(Object target) {
      this.target = Objects.requireNonNull(target, "target");
    }

    @Override public <T extends Object> @Nullable T unwrap(Class<T> clazz) {
      if (clazz.isInstance(target)) {
        return clazz.cast(target);
      }
      return null;
    }
  }

  /** Empty context. */
  static class EmptyContext implements Context {
    @Override public <T extends Object> @Nullable T unwrap(Class<T> clazz) {
      return null;
    }
  }

  /** Context that wraps a chain of contexts. */
  private static final class ChainContext implements Context {
    final ImmutableList<Context> contexts;

    ChainContext(ImmutableList<Context> contexts) {
      this.contexts = Objects.requireNonNull(contexts, "contexts");
      for (Context context : contexts) {
        assert !(context instanceof ChainContext) : "must be flat";
      }
    }

    @Override public <T extends Object> @Nullable T unwrap(Class<T> clazz) {
      for (Context context : contexts) {
        final T t = context.unwrap(clazz);
        if (t != null) {
          return t;
        }
      }
      return null;
    }
  }
}

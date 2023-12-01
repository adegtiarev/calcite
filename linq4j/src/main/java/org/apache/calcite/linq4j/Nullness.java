/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.29.0-rc0
*    Source File: Nullness.java
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
package org.apache.calcite.linq4j;

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

/**
 * The methods in this class allow to cast nullable reference to a non-nullable one.
 * This is an internal class, and it is not meant to be used as a public API.
 * <p>The class enables to remove checker-qual runtime dependency, and helps IDEs to see
 * the resulting types of {@code castNonNull} better</p>
 */
@SuppressWarnings({"cast.unsafe", "NullableProblems", "contracts.postcondition.not.satisfied"})
public class Nullness {
  private Nullness() {
  }

  /**
   * Enables to threat nullable type as non-nullable with no assertions.
   *
   * <p>It is useful in the case you have a nullable lately-initialized field like the following:
   * {@code class Wrapper<T> { @Nullable T value; }}.
   * That signature allows to use {@code Wrapper} with both nullable or non-nullable types:
   * {@code Wrapper<@Nullable Integer>} vs {@code Wrapper<Integer>}. Suppose you need to implement
   * {@code T get() { return value; }} The issue is checkerframework does not permit that
   * because {@code T} has unknown nullability, so the following needs to be used:
   * {@code T get() { return sneakyNull(value); }}</p>
   *
   * @param <T>     the type of the reference
   * @param ref     a reference of @Nullable type, that is non-null at run time
   * @return the argument, casted to have the type qualifier @NonNull
   */
  @Pure
  public static @EnsuresNonNull("#1")
  <T extends @Nullable Object> @NonNull T castNonNull(
      @Nullable T ref) {
    //noinspection ConstantConditions
    return (@NonNull T) ref;
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: PredicateImpl.java
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
package org.apache.calcite.runtime;

import com.google.common.base.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Abstract implementation of {@link com.google.common.base.Predicate}.
 *
 * <p>Derived class needs to implement the {@link #test} method.
 *
 * <p>Helps with the transition to {@code java.util.function.Predicate},
 * which was introduced in JDK 1.8, and is required in Guava 21.0 and higher,
 * but still works on JDK 1.7.
 *
 * @param <T> the type of the input to the predicate
 *
 * @deprecated Now Calcite is Java 8 and higher, we recommend that you
 * implement {@link java.util.function.Predicate} directly.
 */
public abstract class PredicateImpl<T> implements Predicate<T> {
  @Override public final boolean apply(@Nullable T input) {
    return test(input);
  }

  /** Overrides {@code java.util.function.Predicate#test} in JDK8 and higher. */
  // Suppress ErrorProne's MissingOverride warning. The @Override annotation
  // would be incorrect on Guava < 21 because Guava's interface Predicate does
  // not implement Java's interface Predicate until Guava 21, and we need the
  // code to compile on all versions.
  @SuppressWarnings("MissingOverride")
  public abstract boolean test(@Nullable T t);
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: EnumeratorCursor.java
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

import org.apache.calcite.avatica.util.PositionedCursor;
import org.apache.calcite.linq4j.Enumerator;

/**
 * Implementation of {@link org.apache.calcite.avatica.util.Cursor} on top of an
 * {@link org.apache.calcite.linq4j.Enumerator} that
 * returns a record for each row. The returned record is cached to avoid
 * multiple computations of current row.
 * For instance,
 * {@link org.apache.calcite.adapter.enumerable.EnumerableCalc}
 * computes result just in {@code current()} method, thus it makes sense to
 * cache the result and make it available for all the accessors.
 *
 * @param <T> Element type
 */
public abstract class EnumeratorCursor<T> extends PositionedCursor<T> {
  private final Enumerator<T> enumerator;

  /**
   * Creates an {@code EnumeratorCursor}.
   *
   * @param enumerator input enumerator
   */
  protected EnumeratorCursor(Enumerator<T> enumerator) {
    this.enumerator = enumerator;
  }

  @Override protected T current() {
    return enumerator.current();
  }

  @Override public boolean next() {
    return enumerator.moveNext();
  }

  @Override public void close() {
    enumerator.close();
  }
}

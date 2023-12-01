/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: CartesianProductEnumerator.java
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

import java.util.List;

/**
 * Enumerator over the cartesian product of enumerators.
 *
 * @param <T> Input element type
 * @param <E> Element type
 */
public abstract class CartesianProductEnumerator<T, E> implements Enumerator<E> {
  private final List<Enumerator<T>> enumerators;
  protected final T[] elements;
  private boolean first = true;

  protected CartesianProductEnumerator(List<Enumerator<T>> enumerators) {
    this.enumerators = enumerators;
    //noinspection unchecked
    this.elements = (T[]) new Object[enumerators.size()];
  }

  @Override public boolean moveNext() {
    if (first) {
      int i = 0;
      for (Enumerator<T> enumerator : enumerators) {
        if (!enumerator.moveNext()) {
          return false;
        }
        elements[i++] = enumerator.current();
      }
      first = false;
      return true;
    }
    for (int ordinal = enumerators.size() - 1; ordinal >= 0; --ordinal) {
      final Enumerator<T> enumerator = enumerators.get(ordinal);
      if (enumerator.moveNext()) {
        elements[ordinal] = enumerator.current();
        return true;
      }

      // Move back to first element.
      enumerator.reset();
      if (!enumerator.moveNext()) {
        // Very strange... this was empty all along.
        return false;
      }
      elements[ordinal] = enumerator.current();
    }
    return false;
  }

  @Override public void reset() {
    first = true;
    for (Enumerator<T> enumerator : enumerators) {
      enumerator.reset();
    }
  }

  @Override public void close() {
    // If there is one or more exceptions, carry on and close all enumerators,
    // then throw the first.
    Throwable rte = null;
    for (Enumerator<T> enumerator : enumerators) {
      try {
        enumerator.close();
      } catch (Throwable e) {
        if (rte == null) {
          rte = e;
        } else {
          rte.addSuppressed(e);
        }
      }
    }
    if (rte != null) {
      if (rte instanceof Error) {
        throw (Error) rte;
      } else {
        throw (RuntimeException) rte;
      }
    }
  }
}

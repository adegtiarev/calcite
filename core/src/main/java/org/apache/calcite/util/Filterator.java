/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Filterator.java
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
package org.apache.calcite.util;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.apache.calcite.linq4j.Nullness.castNonNull;

/**
 * Filtered iterator class: an iterator that includes only elements that are
 * instanceof a specified class.
 *
 * <p>Apologies for the dorky name.
 *
 * @see Util#cast(java.util.List, Class)
 * @see Util#cast(Iterator, Class)
 *
 * @param <E> Element type
 */
public class Filterator<E extends Object> implements Iterator<E> {
  //~ Instance fields --------------------------------------------------------

  Class<E> includeFilter;
  Iterator<?> iterator;
  @Nullable E lookAhead;
  boolean ready;

  //~ Constructors -----------------------------------------------------------

  public Filterator(Iterator<?> iterator, Class<E> includeFilter) {
    this.iterator = iterator;
    this.includeFilter = includeFilter;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public boolean hasNext() {
    if (ready) {
      // Allow hasNext() to be called repeatedly.
      return true;
    }

    // look ahead to see if there are any additional elements
    try {
      lookAhead = next();
      ready = true;
      return true;
    } catch (NoSuchElementException e) {
      ready = false;
      return false;
    }
  }

  @Override public E next() {
    if (ready) {
      E o = lookAhead;
      ready = false;
      return castNonNull(o);
    }

    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (includeFilter.isInstance(o)) {
        return includeFilter.cast(o);
      }
    }
    throw new NoSuchElementException();
  }

  @Override public void remove() {
    iterator.remove();
  }
}

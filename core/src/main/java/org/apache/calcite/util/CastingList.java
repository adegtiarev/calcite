/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: CastingList.java
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

import java.util.AbstractList;
import java.util.List;

import static org.apache.calcite.linq4j.Nullness.castNonNull;

/**
 * Converts a list whose members are automatically down-cast to a given type.
 *
 * <p>If a member of the backing list is not an instanceof <code>E</code>, the
 * accessing method (such as {@link List#get}) will throw a
 * {@link ClassCastException}.
 *
 * <p>All modifications are automatically written to the backing list. Not
 * synchronized.
 *
 * @param <E> Element type
 */
public class CastingList<E> extends AbstractList<E> implements List<E> {
  //~ Instance fields --------------------------------------------------------

  private final List<? super E> list;
  private final Class<E> clazz;

  //~ Constructors -----------------------------------------------------------

  protected CastingList(List<? super E> list, Class<E> clazz) {
    super();
    this.list = list;
    this.clazz = clazz;
  }

  //~ Methods ----------------------------------------------------------------

  @Override public E get(int index) {
    Object o = list.get(index);
    return clazz.cast(castNonNull(o));
  }

  @Override public int size() {
    return list.size();
  }

  @Override public E set(int index, E element) {
    final Object o = list.set(index, element);
    return clazz.cast(castNonNull(o));
  }

  @Override public E remove(int index) {
    Object o = list.remove(index);
    return clazz.cast(castNonNull(o));
  }

  @Override public void add(int pos, E o) {
    list.add(pos, o);
  }
}

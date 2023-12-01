/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: CompositeMap.java
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/** Unmodifiable view onto multiple backing maps. An element occurs in the map
 * if it occurs in any of the backing maps; the value is the value that occurs
 * in the first map that contains the key.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class CompositeMap<K, V> implements Map<K, V> {
  private final ImmutableList<Map<K, V>> maps;

  public CompositeMap(ImmutableList<Map<K, V>> maps) {
    this.maps = maps;
  }

  /** Creates a CompositeMap. */
  // Would like to use '@SafeVarargs' but JDK 1.6 doesn't support it.
  @SafeVarargs public static <K, V> CompositeMap<K, V> of(Map<K, V> map0,
      Map<K, V>... maps) {
    return new CompositeMap<>(list(map0, maps));
  }

  private static <E> ImmutableList<E> list(E e, E[] es) {
    ImmutableList.Builder<E> builder = ImmutableList.builder();
    builder.add(e);
    for (E map : es) {
      builder.add(map);
    }
    return builder.build();
  }

  @Override public int size() {
    return keySet().size();
  }

  @Override public boolean isEmpty() {
    // Empty iff all maps are empty.
    for (Map<K, V> map : maps) {
      if (!map.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("contracts.conditional.postcondition.not.satisfied")
  @Override public boolean containsKey(@Nullable Object key) {
    for (Map<K, V> map : maps) {
      if (map.containsKey(key)) {
        return true;
      }
    }
    return false;
  }

  @Override public boolean containsValue(@Nullable Object value) {
    for (Map<K, V> map : maps) {
      if (map.containsValue(value)) {
        return true;
      }
    }
    return false;
  }

  @Override public @Nullable V get(@Nullable Object key) {
    for (Map<K, V> map : maps) {
      //noinspection SuspiciousMethodCalls
      if (map.containsKey(key)) {
        return map.get(key);
      }
    }
    return null;
  }

  @Override public V put(K key, V value) {
    // we are an unmodifiable view on the maps
    throw new UnsupportedOperationException();
  }

  @Override public V remove(@Nullable Object key) {
    // we are an unmodifiable view on the maps
    throw new UnsupportedOperationException();
  }

  @Override public void putAll(Map<? extends K, ? extends V> m) {
    // we are an unmodifiable view on the maps
    throw new UnsupportedOperationException();
  }

  @Override public void clear() {
    // we are an unmodifiable view on the maps
    throw new UnsupportedOperationException();
  }

  @SuppressWarnings("return.type.incompatible")
  @Override public Set<@KeyFor("this") K> keySet() {
    final Set<K> keys = new LinkedHashSet<>();
    for (Map<K, V> map : maps) {
      keys.addAll(map.keySet());
    }
    return keys;
  }

  private Map<K, V> combinedMap() {
    ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
    final Set<K> keys = new LinkedHashSet<>();
    for (Map<K, V> map : maps) {
      for (Entry<K, V> entry : map.entrySet()) {
        if (keys.add(entry.getKey())) {
          builder.put(entry);
        }
      }
    }
    return builder.build();
  }

  @Override public Collection<V> values() {
    return combinedMap().values();
  }

  @SuppressWarnings("return.type.incompatible")
  @Override public Set<Entry<@KeyFor("this") K, V>> entrySet() {
    return combinedMap().entrySet();
  }
}

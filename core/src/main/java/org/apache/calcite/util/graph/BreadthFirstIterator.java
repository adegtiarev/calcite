/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: BreadthFirstIterator.java
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
package org.apache.calcite.util.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Iterates over the vertices in a directed graph in breadth-first order.
 *
 * @param <V> Vertex type
 * @param <E> Edge type
 */
public class BreadthFirstIterator<V, E extends DefaultEdge>
    implements Iterator<V> {
  private final DirectedGraph<V, E> graph;
  private final Deque<V> deque = new ArrayDeque<>();
  private final Set<V> set = new HashSet<>();

  public BreadthFirstIterator(DirectedGraph<V, E> graph, V root) {
    this.graph = graph;
    this.deque.add(root);
  }

  public static <V, E extends DefaultEdge> Iterable<V> of(
      final DirectedGraph<V, E> graph, final V root) {
    return () -> new BreadthFirstIterator<>(graph, root);
  }

  /** Populates a set with the nodes reachable from a given node. */
  public static <V, E extends DefaultEdge> void reachable(Set<V> set,
      final DirectedGraph<V, E> graph, final V root) {
    final Deque<V> deque = new ArrayDeque<>();
    deque.add(root);
    set.add(root);
    while (!deque.isEmpty()) {
      V v = deque.removeFirst();
      for (E e : graph.getOutwardEdges(v)) {
        @SuppressWarnings("unchecked") V target = (V) e.target;
        if (set.add(target)) {
          deque.addLast(target);
        }
      }
    }
  }

  @Override public boolean hasNext() {
    return !deque.isEmpty();
  }

  @Override public V next() {
    V v = deque.removeFirst();
    for (E e : graph.getOutwardEdges(v)) {
      @SuppressWarnings("unchecked") V target = (V) e.target;
      if (set.add(target)) {
        deque.addLast(target);
      }
    }
    return v;
  }

  @Override public void remove() {
    throw new UnsupportedOperationException();
  }
}

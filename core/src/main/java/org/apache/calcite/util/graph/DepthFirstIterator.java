/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: DepthFirstIterator.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Iterates over the vertices in a directed graph in depth-first order.
 *
 * @param <V> Vertex type
 * @param <E> Edge type
 */
public class DepthFirstIterator<V, E extends DefaultEdge>
    implements Iterator<V> {
  private final Iterator<V> iterator;

  public DepthFirstIterator(DirectedGraph<V, E> graph, V start) {
    // Dumb implementation that builds the list first.
    iterator = buildList(graph, start).iterator();
  }

  private static <V, E extends DefaultEdge> List<V> buildList(
      DirectedGraph<V, E> graph, V start) {
    final List<V> list = new ArrayList<>();
    buildListRecurse(list, new HashSet<>(), graph, start);
    return list;
  }

  /** Creates an iterable over the vertices in the given graph in a depth-first
   * iteration order. */
  public static <V, E extends DefaultEdge> Iterable<V> of(
      DirectedGraph<V, E> graph, V start) {
    // Doesn't actually return a DepthFirstIterator, but a list with the same
    // contents, which is more efficient.
    return buildList(graph, start);
  }

  /** Populates a collection with the nodes reachable from a given node. */
  public static <V, E extends DefaultEdge> void reachable(Collection<V> list,
      final DirectedGraph<V, E> graph, final V start) {
    buildListRecurse(list, new HashSet<>(), graph, start);
  }

  private static <V, E extends DefaultEdge> void buildListRecurse(
      Collection<V> list, Set<V> activeVertices, DirectedGraph<V, E> graph,
      V start) {
    if (!activeVertices.add(start)) {
      return;
    }
    list.add(start);
    List<E> edges = graph.getOutwardEdges(start);
    for (E edge : edges) {
      //noinspection unchecked
      buildListRecurse(list, activeVertices, graph, (V) edge.target);
    }
    activeVertices.remove(start);
  }

  @Override public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override public V next() {
    return iterator.next();
  }

  @Override public void remove() {
    throw new UnsupportedOperationException();
  }
}

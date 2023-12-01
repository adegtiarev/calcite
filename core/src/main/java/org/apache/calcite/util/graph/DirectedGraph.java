/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: DirectedGraph.java
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

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Directed graph.
 *
 * @param <V> Vertex type
 * @param <E> Edge type
 */
public interface DirectedGraph<V, E> {
  /** Adds a vertex to this graph.
   *
   * @param vertex Vertex
   * @return Whether vertex was added
   */
  boolean addVertex(V vertex);

  /** Adds an edge to this graph.
   *
   * @param vertex Source vertex
   * @param targetVertex Target vertex
   * @return New edge, if added, otherwise null
   * @throws IllegalArgumentException if either vertex is not already in graph
   */
  @Nullable E addEdge(V vertex, V targetVertex);

  @Nullable E getEdge(V source, V target);

  boolean removeEdge(V vertex, V targetVertex);

  Set<? extends V> vertexSet();

  /** Removes from this graph all vertices that are in {@code collection},
   * and the edges into and out of those vertices. */
  void removeAllVertices(Collection<V> collection);

  List<E> getOutwardEdges(V source);

  List<E> getInwardEdges(V vertex);

  Set<E> edgeSet();

  /** Factory for edges.
   *
   * @param <V> Vertex type
   * @param <E> Edge type
   */
  interface EdgeFactory<V, E> {
    E createEdge(V v0, V v1);
  }
}

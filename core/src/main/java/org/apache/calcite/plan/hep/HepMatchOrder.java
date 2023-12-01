/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/cf-testorg/calcite-test/releases/tag/calcite-1.26.0-rc0
*    Source File: HepMatchOrder.java
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
package org.apache.calcite.plan.hep;

/**
 * HepMatchOrder specifies the order of graph traversal when looking for rule
 * matches.
 */
public enum HepMatchOrder {
  /**
   * Match in arbitrary order. This is the default because it is
   * efficient, and most rules don't care about order.
   */
  ARBITRARY,

  /**
   * Match from leaves up. A match attempt at a descendant precedes all match
   * attempts at its ancestors.
   */
  BOTTOM_UP,

  /**
   * Match from root down. A match attempt at an ancestor always precedes all
   * match attempts at its descendants.
   */
  TOP_DOWN,

  /**
   * Match in depth-first order.
   *
   * <p>It avoids applying a rule to the previous
   * {@link org.apache.calcite.rel.RelNode} repeatedly after new vertex is
   * generated in one rule application. It can therefore be more efficient than
   * {@link #ARBITRARY} in cases such as
   * {@link org.apache.calcite.rel.core.Union} with large fan-out.
   */
  DEPTH_FIRST
}

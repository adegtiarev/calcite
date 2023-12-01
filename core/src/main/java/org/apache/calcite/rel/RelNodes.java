/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.33.0
*    Source File: RelNodes.java
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
package org.apache.calcite.rel;

import org.apache.calcite.runtime.Utilities;

import com.google.common.collect.Ordering;

import java.util.Comparator;

/**
 * Utilities concerning relational expressions.
 */
public class RelNodes {
  /** Comparator that provides an arbitrary but stable ordering to
   * {@link RelNode}s. */
  public static final Comparator<RelNode> COMPARATOR =
      new RelNodeComparator();

  /** Ordering for {@link RelNode}s. */
  public static final Ordering<RelNode> ORDERING = Ordering.from(COMPARATOR);

  private RelNodes() {}

  /** Compares arrays of {@link RelNode}. */
  public static int compareRels(RelNode[] rels0, RelNode[] rels1) {
    int c = Utilities.compare(rels0.length, rels1.length);
    if (c != 0) {
      return c;
    }
    for (int i = 0; i < rels0.length; i++) {
      c = COMPARATOR.compare(rels0[i], rels1[i]);
      if (c != 0) {
        return c;
      }
    }
    return 0;
  }

  /** Arbitrary stable comparator for {@link RelNode}s. */
  private static class RelNodeComparator implements Comparator<RelNode> {
    @Override public int compare(RelNode o1, RelNode o2) {
      // Compare on field count first. It is more stable than id (when rules
      // are added to the set of active rules).
      final int c = Utilities.compare(o1.getRowType().getFieldCount(),
          o2.getRowType().getFieldCount());
      if (c != 0) {
        return -c;
      }
      return Utilities.compare(o1.getId(), o2.getId());
    }
  }
}

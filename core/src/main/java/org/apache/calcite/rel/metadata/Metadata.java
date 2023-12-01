/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/Abzhanov/piscine-go/releases/tag/calcite-1.26.0
*    Source File: Metadata.java
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
package org.apache.calcite.rel.metadata;

import org.apache.calcite.rel.RelNode;

/**
 * Metadata about a relational expression.
 *
 * <p>For particular types of metadata, a sub-class defines one of more methods
 * to query that metadata. Then a {@link RelMetadataProvider} can offer those
 * kinds of metadata for particular sub-classes of {@link RelNode}.
 *
 * <p>User code (typically in a planner rule or an implementation of
 * {@link RelNode#computeSelfCost(org.apache.calcite.plan.RelOptPlanner, RelMetadataQuery)})
 * acquires a {@code Metadata} instance by calling {@link RelNode#metadata}.
 *
 * <p>A {@code Metadata} instance already knows which particular {@code RelNode}
 * it is describing, so the methods do not pass in the {@code RelNode}. In fact,
 * quite a few metadata methods have no extra parameters. For instance, you can
 * get the row-count as follows:</p>
 *
 * <blockquote><pre><code>
 * RelNode rel;
 * double rowCount = rel.metadata(RowCount.class).rowCount();
 * </code></pre></blockquote>
 */
public interface Metadata {
  /** Returns the relational expression that this metadata is about. */
  RelNode rel();
}

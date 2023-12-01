/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: HepRelMetadataProvider.java
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

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.metadata.Metadata;
import org.apache.calcite.rel.metadata.MetadataDef;
import org.apache.calcite.rel.metadata.MetadataHandler;
import org.apache.calcite.rel.metadata.RelMetadataProvider;
import org.apache.calcite.rel.metadata.UnboundMetadata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * HepRelMetadataProvider implements the {@link RelMetadataProvider} interface
 * by combining metadata from the rels inside of a {@link HepRelVertex}.
 */
@Deprecated // to be removed before 2.0
class HepRelMetadataProvider implements RelMetadataProvider {
  //~ Methods ----------------------------------------------------------------

  @Override public boolean equals(@Nullable Object obj) {
    return obj instanceof HepRelMetadataProvider;
  }

  @Override public int hashCode() {
    return 107;
  }

  @Deprecated // to be removed before 2.0
  @Override public <@Nullable M extends @Nullable Metadata> UnboundMetadata<M> apply(
      Class<? extends RelNode> relClass,
      final Class<? extends M> metadataClass) {
    return (rel, mq) -> {
      if (!(rel instanceof HepRelVertex)) {
        return null;
      }
      HepRelVertex vertex = (HepRelVertex) rel;
      final RelNode rel2 = vertex.getCurrentRel();
      UnboundMetadata<M> function =
          requireNonNull(rel.getCluster().getMetadataProvider(), "metadataProvider")
              .apply(rel2.getClass(), metadataClass);
      return requireNonNull(
          function,
          () -> "no metadata provider for class " + metadataClass)
          .bind(rel2, mq);
    };
  }

  @Deprecated // to be removed before 2.0
  @Override public <M extends Metadata> Multimap<Method, MetadataHandler<M>> handlers(
      MetadataDef<M> def) {
    return ImmutableMultimap.of();
  }

  @Override public List<MetadataHandler<?>> handlers(
      Class<? extends MetadataHandler<?>> handlerClass) {
    return ImmutableList.of();
  }
}

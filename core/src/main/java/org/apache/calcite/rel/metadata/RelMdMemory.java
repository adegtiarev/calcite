/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RelMdMemory.java
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

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Default implementations of the
 * {@link org.apache.calcite.rel.metadata.BuiltInMetadata.Memory}
 * metadata provider for the standard logical algebra.
 *
 * @see RelMetadataQuery#isPhaseTransition
 * @see RelMetadataQuery#splitCount
 */
public class RelMdMemory implements MetadataHandler<BuiltInMetadata.Memory> {
  /** Source for
   * {@link org.apache.calcite.rel.metadata.BuiltInMetadata.Memory}. */
  public static final RelMetadataProvider SOURCE =
      ReflectiveRelMetadataProvider.reflectiveSource(new RelMdMemory(),
          BuiltInMetadata.Memory.Handler.class);

  //~ Constructors -----------------------------------------------------------

  protected RelMdMemory() {}

  //~ Methods ----------------------------------------------------------------

  @Override public MetadataDef<BuiltInMetadata.Memory> getDef() {
    return BuiltInMetadata.Memory.DEF;
  }

  /** Catch-all implementation for
   * {@link BuiltInMetadata.Memory#memory()},
   * invoked using reflection.
   *
   * @see org.apache.calcite.rel.metadata.RelMetadataQuery#memory
   */
  public @Nullable Double memory(RelNode rel, RelMetadataQuery mq) {
    return null;
  }

  /** Catch-all implementation for
   * {@link BuiltInMetadata.Memory#cumulativeMemoryWithinPhase()},
   * invoked using reflection.
   *
   * @see org.apache.calcite.rel.metadata.RelMetadataQuery#memory
   */
  public @Nullable Double cumulativeMemoryWithinPhase(RelNode rel, RelMetadataQuery mq) {
    Double nullable = mq.memory(rel);
    if (nullable == null) {
      return null;
    }
    Boolean isPhaseTransition = mq.isPhaseTransition(rel);
    if (isPhaseTransition == null) {
      return null;
    }
    double d = nullable;
    if (!isPhaseTransition) {
      for (RelNode input : rel.getInputs()) {
        nullable = mq.cumulativeMemoryWithinPhase(input);
        if (nullable == null) {
          return null;
        }
        d += nullable;
      }
    }
    return d;
  }

  /** Catch-all implementation for
   * {@link BuiltInMetadata.Memory#cumulativeMemoryWithinPhaseSplit()},
   * invoked using reflection.
   *
   * @see org.apache.calcite.rel.metadata.RelMetadataQuery#cumulativeMemoryWithinPhaseSplit
   */
  public @Nullable Double cumulativeMemoryWithinPhaseSplit(RelNode rel,
      RelMetadataQuery mq) {
    final Double memoryWithinPhase = mq.cumulativeMemoryWithinPhase(rel);
    final Integer splitCount = mq.splitCount(rel);
    if (memoryWithinPhase == null || splitCount == null) {
      return null;
    }
    return memoryWithinPhase / splitCount;
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MemoryEnumerator.java
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
package org.apache.calcite.linq4j;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Enumerator that keeps some recent and some "future" values.
 *
 * @param <E> Row value
 */
public class MemoryEnumerator<@Nullable E> implements Enumerator<MemoryFactory.Memory<E>> {
  private final Enumerator<E> enumerator;
  private final MemoryFactory<E> memoryFactory;
  private final AtomicInteger prevCounter;
  private final AtomicInteger postCounter;

  /**
   * Creates a MemoryEnumerator.
   *
   * <p>Use factory method {@link MemoryEnumerable#enumerator()}.
   *
   * @param enumerator The Enumerator that memory should be "wrapped" around
   * @param history Number of present steps to remember
   * @param future Number of future steps to "remember"
   */
  MemoryEnumerator(Enumerator<E> enumerator, int history, int future) {
    this.enumerator = enumerator;
    this.memoryFactory = new MemoryFactory<>(history, future);
    this.prevCounter = new AtomicInteger(future);
    this.postCounter = new AtomicInteger(future);
  }

  @Override public MemoryFactory.Memory<E> current() {
    return memoryFactory.create();
  }

  @Override public boolean moveNext() {
    if (prevCounter.get() > 0) {
      boolean lastMove = false;
      while (prevCounter.getAndDecrement() >= 0) {
        lastMove = moveNextInternal();
      }
      return lastMove;
    } else {
      return moveNextInternal();
    }
  }

  private boolean moveNextInternal() {
    final boolean moveNext = enumerator.moveNext();
    if (moveNext) {
      memoryFactory.add(enumerator.current());
      return true;
    } else {
      // Check if we have to add "history" additional values
      if (postCounter.getAndDecrement() > 0) {
        memoryFactory.add(null);
        return true;
      }
    }
    return false;
  }

  @Override public void reset() {
    enumerator.reset();
  }

  @Override public void close() {
    enumerator.close();
  }
}

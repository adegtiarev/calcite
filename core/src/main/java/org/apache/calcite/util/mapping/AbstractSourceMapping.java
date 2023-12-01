/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: AbstractSourceMapping.java
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
package org.apache.calcite.util.mapping;

import java.util.Iterator;

/**
 * Simple implementation of
 * {@link org.apache.calcite.util.mapping.Mappings.TargetMapping} where the
 * number of sources and targets are specified as constructor parameters, and you
 * just need to implement one method.
 */
public abstract class AbstractSourceMapping
    extends Mappings.AbstractMapping
    implements Mapping {
  private final int sourceCount;
  private final int targetCount;

  protected AbstractSourceMapping(int sourceCount, int targetCount) {
    this.sourceCount = sourceCount;
    this.targetCount = targetCount;
  }

  @Override public int getSourceCount() {
    return sourceCount;
  }

  @Override public int getTargetCount() {
    return targetCount;
  }

  @Override public Mapping inverse() {
    return Mappings.invert(this);
  }

  @Override public int size() {
    return targetCount;
  }

  @Override public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override public MappingType getMappingType() {
    return MappingType.INVERSE_PARTIAL_FUNCTION;
  }

  @SuppressWarnings("method.invocation.invalid")
  @Override public Iterator<IntPair> iterator() {
    return new Iterator<IntPair>() {
      int source;
      int target = -1;

      {
        moveToNext();
      }

      private void moveToNext() {
        while (++target < targetCount) {
          source = getSourceOpt(target);
          if (source >= 0) {
            break;
          }
        }
      }

      @Override public boolean hasNext() {
        return target < targetCount;
      }

      @Override public IntPair next() {
        IntPair p = new IntPair(source, target);
        moveToNext();
        return p;
      }

      @Override public void remove() {
        throw new UnsupportedOperationException("remove");
      }
    };
  }

  @Override public abstract int getSourceOpt(int source);
}

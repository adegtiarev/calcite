/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: FunctionContexts.java
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
package org.apache.calcite.runtime;

import org.apache.calcite.DataContext;
import org.apache.calcite.rel.metadata.NullSentinel;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.FunctionContext;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Runtime support for {@link org.apache.calcite.schema.FunctionContext}.
 */
public class FunctionContexts {
  private FunctionContexts() {
  }

  /** Creates a FunctionContext. */
  public static FunctionContext of(DataContext root, Object[] argumentValues) {
    return new FunctionContextImpl(root, argumentValues);
  }

  /** Implementation of {@link org.apache.calcite.schema.FunctionContext}. */
  private static class FunctionContextImpl implements FunctionContext {
    private final DataContext root;
    private final @Nullable Object[] argumentValues;

    FunctionContextImpl(DataContext root, @Nullable Object[] argumentValues) {
      this.root = root;
      this.argumentValues = argumentValues;
    }

    @Override public RelDataTypeFactory getTypeFactory() {
      return root.getTypeFactory();
    }

    @Override public int getParameterCount() {
      return argumentValues.length;
    }

    @Override public boolean isArgumentConstant(int ordinal) {
      return argumentValue(ordinal) != null;
    }

    private @Nullable Object argumentValue(int ordinal) {
      if (ordinal < 0 || ordinal >= argumentValues.length) {
        throw new IndexOutOfBoundsException("argument ordinal " + ordinal
            + " is out of range");
      }
      return argumentValues[ordinal];
    }

    @Override public <V> @Nullable V getArgumentValueAs(int ordinal,
        Class<V> valueClass) {
      final Object v = argumentValue(ordinal);
      if (v == null) {
        throw new IllegalArgumentException("value of argument " + ordinal
            + " is not constant");
      }
      if (v == NullSentinel.INSTANCE) {
        return null; // value is constant NULL
      }
      if (valueClass == String.class
          && !(v instanceof String)) {
        return valueClass.cast(v.toString());
      }
      return valueClass.cast(v);
    }
  }
}

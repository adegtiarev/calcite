/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Interpreters.java
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
package org.apache.calcite.interpreter;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.runtime.ArrayBindable;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Utilities relating to {@link org.apache.calcite.interpreter.Interpreter}
 * and {@link org.apache.calcite.interpreter.InterpretableConvention}.
 */
public class Interpreters {
  private Interpreters() {}

  /** Creates a {@link org.apache.calcite.runtime.Bindable} that interprets a
   * given relational expression. */
  public static ArrayBindable bindable(final RelNode rel) {
    if (rel instanceof ArrayBindable) {
      // E.g. if rel instanceof BindableRel
      return (ArrayBindable) rel;
    }
    return new ArrayBindable() {
      @Override public Enumerable<@Nullable Object[]> bind(DataContext dataContext) {
        return new Interpreter(dataContext, rel);
      }

      @Override public Class<Object[]> getElementType() {
        return Object[].class;
      }
    };
  }
}

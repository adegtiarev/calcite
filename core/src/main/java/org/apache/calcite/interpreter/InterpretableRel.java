/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: InterpretableRel.java
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
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.rel.RelNode;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Relational expression that can implement itself using an interpreter.
 */
public interface InterpretableRel extends RelNode {
  /** Creates an interpreter node to implement this relational expression. */
  Node implement(InterpreterImplementor implementor);

  /** Context when a {@link RelNode} is being converted to an interpreter
   * {@link Node}. */
  class InterpreterImplementor {
    public final Compiler compiler;
    public final Map<String, Object> internalParameters =
        new LinkedHashMap<>();
    public final CalcitePrepare.@Nullable SparkHandler spark;
    public final DataContext dataContext;
    public final Map<RelNode, List<Sink>> relSinks = new HashMap<>();

    public InterpreterImplementor(Compiler compiler,
        CalcitePrepare.@Nullable SparkHandler spark,
        DataContext dataContext) {
      this.compiler = compiler;
      this.spark = spark;
      this.dataContext = dataContext;
    }
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: TableFunctionScanNode.java
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

import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.function.Function1;
import org.apache.calcite.rel.core.TableFunctionScan;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.schema.Function;
import org.apache.calcite.schema.impl.TableFunctionImpl;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.validate.SqlUserDefinedTableFunction;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Interpreter node that implements a
 * {@link TableFunctionScan}.
 */
public class TableFunctionScanNode implements Node {
  private final Scalar scalar;
  private final Context context;
  private final Sink sink;
  private final Function1<?, Row> mapFn;

  private TableFunctionScanNode(Compiler compiler, TableFunctionScan rel) {
    final RelDataType rowType = rel.getRowType();
    this.scalar = compiler.compile(ImmutableList.of(rel.getCall()), rowType);
    this.context = compiler.createContext();
    this.sink = compiler.sink(rel);
    if (rowType.getFieldCount() == 1
        && rel.getElementType() != Object[].class) {
      this.mapFn = (Function1<Object, Row>) Row::of;
    } else {
      this.mapFn = (Function1<@Nullable Object[], Row>) Row::asCopy;
    }
  }

  @Override public void run() throws InterruptedException {
    final Object o = scalar.execute(context);
    if (o instanceof Enumerable) {
      for (@SuppressWarnings({"unchecked", "rawtypes"})
           final Enumerator<Row> enumerator =
           ((Enumerable) o).select(mapFn).enumerator();
           enumerator.moveNext();) {
        sink.send(enumerator.current());
      }
    }
  }

  /** Creates a TableFunctionScanNode. */
  static TableFunctionScanNode create(Compiler compiler, TableFunctionScan rel) {
    RexNode call = rel.getCall();
    if (call instanceof RexCall) {
      SqlOperator operator = ((RexCall) call).getOperator();
      if (operator instanceof SqlUserDefinedTableFunction) {
        Function function = ((SqlUserDefinedTableFunction) operator).function;
        if (function instanceof TableFunctionImpl) {
          return new TableFunctionScanNode(compiler, rel);
        }
      }
    }
    throw new AssertionError("cannot convert table function scan "
        + rel.getCall() + " to enumerable");
  }
}

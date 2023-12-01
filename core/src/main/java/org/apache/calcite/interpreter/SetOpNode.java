/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SetOpNode.java
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

import org.apache.calcite.rel.core.SetOp;

import com.google.common.collect.HashMultiset;

import java.util.Collection;
import java.util.HashSet;

/**
 * Interpreter node that implements a
 * {@link org.apache.calcite.rel.core.SetOp},
 * including {@link org.apache.calcite.rel.core.Minus},
 * {@link org.apache.calcite.rel.core.Union} and
 * {@link org.apache.calcite.rel.core.Intersect}.
 */
public class SetOpNode implements Node {
  private final Source leftSource;
  private final Source rightSource;
  private final Sink sink;
  private final SetOp setOp;

  public SetOpNode(Compiler compiler, SetOp setOp) {
    leftSource = compiler.source(setOp, 0);
    rightSource = compiler.source(setOp, 1);
    sink = compiler.sink(setOp);
    this.setOp = setOp;
  }

  @Override public void close() {
    leftSource.close();
    rightSource.close();
  }

  @Override public void run() throws InterruptedException {
    final Collection<Row> leftRows;
    final Collection<Row> rightRows;
    if (setOp.all) {
      leftRows = HashMultiset.create();
      rightRows = HashMultiset.create();
    } else {
      leftRows = new HashSet<>();
      rightRows = new HashSet<>();
    }
    Row row;
    while ((row = leftSource.receive()) != null) {
      leftRows.add(row);
    }
    while ((row = rightSource.receive()) != null) {
      rightRows.add(row);
    }
    switch (setOp.kind) {
    case INTERSECT:
      for (Row leftRow : leftRows) {
        if (rightRows.remove(leftRow)) {
          sink.send(leftRow);
        }
      }
      break;
    case EXCEPT:
      for (Row leftRow : leftRows) {
        if (!rightRows.remove(leftRow)) {
          sink.send(leftRow);
        }
      }
      break;
    case UNION:
      leftRows.addAll(rightRows);
      for (Row r : leftRows) {
        sink.send(r);
      }
      break;
    default:
      break;
    }
  }
}

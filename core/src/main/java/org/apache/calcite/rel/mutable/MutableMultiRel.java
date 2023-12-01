/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: MutableMultiRel.java
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
package org.apache.calcite.rel.mutable;

import org.apache.calcite.linq4j.Ord;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.util.Util;

import java.util.ArrayList;
import java.util.List;

/** Base Class for relations with three or more inputs. */
abstract class MutableMultiRel extends MutableRel {
  protected final List<MutableRel> inputs;

  @SuppressWarnings("initialization.invalid.field.write.initialized")
  protected MutableMultiRel(RelOptCluster cluster,
      RelDataType rowType, MutableRelType type, List<MutableRel> inputs) {
    super(cluster, rowType, type);
    this.inputs = new ArrayList<>(inputs);
    for (Ord<MutableRel> input : Ord.zip(inputs)) {
      input.e.parent = this;
      input.e.ordinalInParent = input.i;
    }
  }

  @Override public void setInput(int ordinalInParent, MutableRel input) {
    inputs.set(ordinalInParent, input);
    if (input != null) {
      input.parent = this;
      input.ordinalInParent = ordinalInParent;
    }
  }

  @Override public List<MutableRel> getInputs() {
    return inputs;
  }

  @Override public void childrenAccept(MutableRelVisitor visitor) {
    for (MutableRel input : inputs) {
      visitor.visit(input);
    }
  }

  protected List<MutableRel> cloneChildren() {
    return Util.transform(inputs, MutableRel::clone);
  }
}

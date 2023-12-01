/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ConstantUntypedNull.java
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
package org.apache.calcite.linq4j.tree;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a constant null of unknown type
 * Java allows type inference for such nulls, thus "null" cannot always be
 * replaced to (Object)null and vise versa.
 *
 * <p>{@code ConstantExpression(null, Object.class)} is not equal to
 * {@code ConstantUntypedNull} However, optimizers might treat all the nulls
 * equal (e.g. in case of comparison).
 */
public class ConstantUntypedNull extends ConstantExpression {
  public static final ConstantExpression INSTANCE = new ConstantUntypedNull();

  private ConstantUntypedNull() {
    super(Object.class, null);
  }

  @Override void accept(ExpressionWriter writer, int lprec, int rprec) {
    writer.append("null");
  }

  @Override public boolean equals(@Nullable Object o) {
    return o == INSTANCE;
  }

  @Override public int hashCode() {
    return ConstantUntypedNull.class.hashCode();
  }
}

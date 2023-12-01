/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Expression.java
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

import java.lang.reflect.Type;

/**
 * <p>Analogous to LINQ's System.Linq.Expression.</p>
 */
public abstract class Expression extends AbstractNode {

  /**
   * Creates an Expression.
   *
   * <p>The type of the expression may, at the caller's discretion, be a
   * regular class (because {@link Class} implements {@link Type}) or it may
   * be a different implementation that retains information about type
   * parameters.</p>
   *
   * @param nodeType Node type
   * @param type Type of the expression
   */
  protected Expression(ExpressionType nodeType, Type type) {
    super(nodeType, type);
    assert nodeType != null;
    assert type != null;
  }

  @Override // More specific return type.
  public abstract Expression accept(Shuttle shuttle);

  /**
   * Indicates that the node can be reduced to a simpler node. If this
   * returns true, Reduce() can be called to produce the reduced form.
   */
  public boolean canReduce() {
    return false;
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: JavaRelImplementor.java
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
package org.apache.calcite.adapter.enumerable;

import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.tree.ParameterExpression;
import org.apache.calcite.plan.RelImplementor;
import org.apache.calcite.rex.RexBuilder;

/**
 * Abstract base class for implementations of {@link RelImplementor}
 * that generate java code.
 */
public abstract class JavaRelImplementor implements RelImplementor {
  private final RexBuilder rexBuilder;

  protected JavaRelImplementor(RexBuilder rexBuilder) {
    this.rexBuilder = rexBuilder;
    assert rexBuilder.getTypeFactory() instanceof JavaTypeFactory
        : "Type factory of rexBuilder should be a JavaTypeFactory";
  }

  public RexBuilder getRexBuilder() {
    return rexBuilder;
  }

  public JavaTypeFactory getTypeFactory() {
    return (JavaTypeFactory) rexBuilder.getTypeFactory();
  }

  /**
   * Returns the expression used to access
   * {@link org.apache.calcite.DataContext}.
   *
   * @return expression used to access {@link org.apache.calcite.DataContext}.
   */
  public ParameterExpression getRootExpression() {
    return DataContext.ROOT;
  }
}

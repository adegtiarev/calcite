/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RexToSqlNodeConverter.java
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
package org.apache.calcite.rex;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Converts expressions from {@link RexNode} to {@link SqlNode}.
 *
 * <p>For most purposes, {@link org.apache.calcite.rel.rel2sql.SqlImplementor}
 * is superior. See in particular
 * {@link org.apache.calcite.rel.rel2sql.SqlImplementor.Context#toSql(RexProgram, RexNode)}.
 */
public interface RexToSqlNodeConverter {
  //~ Methods ----------------------------------------------------------------

  /**
   * Converts a {@link RexNode} to a {@link SqlNode} expression,
   * typically by dispatching to one of the other interface methods.
   *
   * @param node RexNode to translate
   * @return SqlNode, or null if no translation was available
   */
  @Nullable SqlNode convertNode(RexNode node);

  /**
   * Converts a {@link RexCall} to a {@link SqlNode} expression.
   *
   * @param call RexCall to translate
   * @return SqlNode, or null if no translation was available
   */
  @Nullable SqlNode convertCall(RexCall call);

  /**
   * Converts a {@link RexLiteral} to a {@link SqlLiteral}.
   *
   * @param literal RexLiteral to translate
   * @return SqlNode, or null if no translation was available
   */
  @Nullable SqlNode convertLiteral(RexLiteral literal);

  /**
   * Converts a {@link RexInputRef} to a {@link SqlIdentifier}.
   *
   * @param ref RexInputRef to translate
   * @return SqlNode, or null if no translation was available
   */
  @Nullable SqlNode convertInputRef(RexInputRef ref);
}

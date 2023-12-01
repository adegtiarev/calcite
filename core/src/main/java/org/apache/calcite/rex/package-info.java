/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: package-info.java
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

/**
 * Provides a language for representing row-expressions.
 *
 * <h2>Life-cycle</h2>
 *
 * <p>A {@link org.apache.calcite.sql2rel.SqlToRelConverter} converts a SQL
 * parse tree consisting of {@link org.apache.calcite.sql.SqlNode} objects into
 * a relational expression ({@link org.apache.calcite.rel.RelNode}). Several
 * kinds of nodes in this tree have row expressions
 * ({@link org.apache.calcite.rex.RexNode}).</p>
 *
 * <p>After the relational expression has been optimized, a
 * {@link org.apache.calcite.adapter.enumerable.JavaRelImplementor} converts it
 * into to a plan. If the plan is a Java parse tree, row-expressions are
 * translated into equivalent Java expressions.</p>
 *
 * <h2>Expressions</h2>
 *
 *
 * <p>Every row-expression has a type. (Compare with
 * {@link org.apache.calcite.sql.SqlNode}, which is created before validation,
 * and therefore types may not be available.)</p>
 *
 * <p>Every node in the parse tree is a {@link org.apache.calcite.rex.RexNode}.
 *     Sub-types are:</p>
 * <ul>
 *     <li>{@link org.apache.calcite.rex.RexLiteral} represents a boolean,
 *         numeric, string, or date constant, or the value <code>NULL</code>.
 *     </li>
 *     <li>{@link org.apache.calcite.rex.RexVariable} represents a leaf of the
 *         tree. It has sub-types:
 *         <ul>
 *             <li>{@link org.apache.calcite.rex.RexCorrelVariable} is a
 *                 correlating variable for nested-loop joins
 *             </li>
 *             <li>{@link org.apache.calcite.rex.RexInputRef} refers to a field
 *                 of an input relational expression
 *             </li>
 *             <li>{@link org.apache.calcite.rex.RexCall} is a call to an
 *                 operator or function.  By means of special operators, we can
 *                 use this construct to represent virtually every non-leaf node
 *                 in the tree.
 *             </li>
 *             <li>{@link org.apache.calcite.rex.RexRangeRef} refers to a
 *                 collection of contiguous fields from an input relational
 *                 expression. It usually exists only during translation.
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <p>Expressions are generally
 *     created using a {@link org.apache.calcite.rex.RexBuilder} factory.</p>
 *
 * <h2>Related packages</h2>
 * <ul>
 *     <li>{@link org.apache.calcite.sql} SQL object model</li>
 *
 *     <li>{@link org.apache.calcite.plan} Core classes, including
 * {@link org.apache.calcite.rel.type.RelDataType} and
 * {@link org.apache.calcite.rel.type.RelDataTypeFactory}.</li>
 *
 * </ul>
 */
@DefaultQualifier(value = NonNull.class, locations = TypeUseLocation.FIELD)
@DefaultQualifier(value = NonNull.class, locations = TypeUseLocation.PARAMETER)
@DefaultQualifier(value = NonNull.class, locations = TypeUseLocation.RETURN)
package org.apache.calcite.rex;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;

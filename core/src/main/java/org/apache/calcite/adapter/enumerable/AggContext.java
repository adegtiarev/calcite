/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: AggContext.java
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

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.util.ImmutableBitSet;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Information on the aggregate calculation context.
 * {@link AggAddContext} provides basic static information on types of arguments
 * and the return value of the aggregate being implemented.
 */
public interface AggContext {
  /**
   * Returns the aggregation being implemented.
   * @return aggregation being implemented.
   */
  SqlAggFunction aggregation();

  /**
   * Returns the return type of the aggregate as
   * {@link org.apache.calcite.rel.type.RelDataType}.
   * This can be helpful to test
   * {@link org.apache.calcite.rel.type.RelDataType#isNullable()}.
   *
   * @return return type of the aggregate
   */
  RelDataType returnRelType();

  /**
   * Returns the return type of the aggregate as {@link java.lang.reflect.Type}.
   * @return return type of the aggregate as {@link java.lang.reflect.Type}
   */
  Type returnType();

  /**
   * Returns the parameter types of the aggregate as
   * {@link org.apache.calcite.rel.type.RelDataType}.
   * This can be helpful to test
   * {@link org.apache.calcite.rel.type.RelDataType#isNullable()}.
   *
   * @return Parameter types of the aggregate
   */
  List<? extends RelDataType> parameterRelTypes();

  /**
   * Returns the parameter types of the aggregate as
   * {@link java.lang.reflect.Type}.
   *
   * @return Parameter types of the aggregate
   */
  List<? extends Type> parameterTypes();

  /** Returns the ordinals of the input fields that make up the key. */
  List<Integer> keyOrdinals();

  /**
   * Returns the types of the group key as
   * {@link org.apache.calcite.rel.type.RelDataType}.
   */
  List<? extends RelDataType> keyRelTypes();

  /**
   * Returns the types of the group key as
   * {@link java.lang.reflect.Type}.
   */
  List<? extends Type> keyTypes();

  /** Returns the grouping sets we are aggregating on. */
  List<ImmutableBitSet> groupSets();
}

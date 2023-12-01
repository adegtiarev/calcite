/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ElasticsearchMethod.java
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
package org.apache.calcite.adapter.elasticsearch;

import org.apache.calcite.linq4j.tree.Types;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Builtin methods in the Elasticsearch adapter.
 */
enum ElasticsearchMethod {

  ELASTICSEARCH_QUERYABLE_FIND(ElasticsearchTable.ElasticsearchQueryable.class,
      "find",
      List.class, // ops  - projections and other stuff
      List.class, // fields
      List.class, // sort
      List.class, // groupBy
      List.class, // aggregations
      Map.class, // item to expression mapping. Eg. _MAP['a.b.c'] and EXPR$1
      Long.class, // offset
      Long.class); // fetch

  @SuppressWarnings("ImmutableEnumChecker")
  public final Method method;

  public static final ImmutableMap<Method, ElasticsearchMethod> MAP;

  static {
    final ImmutableMap.Builder<Method, ElasticsearchMethod> builder = ImmutableMap.builder();
    for (ElasticsearchMethod value: ElasticsearchMethod.values()) {
      builder.put(value.method, value);
    }
    MAP = builder.build();
  }

  ElasticsearchMethod(Class clazz, String methodName, Class... argumentTypes) {
    this.method = Types.lookupMethod(clazz, methodName, argumentTypes);
  }
}

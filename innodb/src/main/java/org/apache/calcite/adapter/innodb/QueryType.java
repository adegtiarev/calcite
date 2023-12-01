/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: QueryType.java
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
package org.apache.calcite.adapter.innodb;

/**
 * Query type of a push down condition in InnoDB data source.
 */
public enum QueryType {
  /** Primary key point query. */
  PK_POINT_QUERY(0),
  /** Secondary key point query. */
  SK_POINT_QUERY(1),
  /** Primary key range query with lower and upper bound. */
  PK_RANGE_QUERY(2),
  /** Secondary key range query with lower and upper bound. */
  SK_RANGE_QUERY(3),
  /** Scanning table fully with primary key. */
  PK_FULL_SCAN(4),
  /** Scanning table fully with secondary key. */
  SK_FULL_SCAN(5);

  private final int priority;

  static QueryType getPointQuery(boolean isSk) {
    return isSk ? SK_POINT_QUERY : PK_POINT_QUERY;
  }

  static QueryType getRangeQuery(boolean isSk) {
    return isSk ? SK_RANGE_QUERY : PK_RANGE_QUERY;
  }

  QueryType(int priority) {
    this.priority = priority;
  }

  int priority() {
    return priority;
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/googleinterns/calcite/releases/tag/master-e94c866
*    Source File: PigAggFunction.java
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
package org.apache.calcite.adapter.pig;

import org.apache.calcite.sql.SqlKind;

/**
 * Supported Pig aggregate functions and their Calcite counterparts. The enum's
 * name() is the same as the function's name in Pig Latin.
 */
public enum PigAggFunction {

  COUNT(SqlKind.COUNT, false), COUNT_STAR(SqlKind.COUNT, true);

  private final SqlKind calciteFunc;
  private final boolean star; // as in COUNT(*)

  PigAggFunction(SqlKind calciteFunc) {
    this(calciteFunc, false);
  }

  PigAggFunction(SqlKind calciteFunc, boolean star) {
    this.calciteFunc = calciteFunc;
    this.star = star;
  }

  public static PigAggFunction valueOf(SqlKind calciteFunc, boolean star) {
    for (PigAggFunction pigAggFunction : values()) {
      if (pigAggFunction.calciteFunc == calciteFunc && pigAggFunction.star == star) {
        return pigAggFunction;
      }
    }
    throw new IllegalArgumentException("Pig agg func for " + calciteFunc + " is not supported");
  }
}

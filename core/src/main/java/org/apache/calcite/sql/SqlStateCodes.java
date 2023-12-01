/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlStateCodes.java
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
package org.apache.calcite.sql;

/**
 * Contains {@link org.apache.calcite.util.Glossary#SQL2003} SQL state codes.
 *
 * <p>SQL State codes are defined in
 *
 * <blockquote><pre>&#64;sql.2003 Part 2 Section 23.1</pre></blockquote>
 *
 * @deprecated Use {@code org.apache.calcite.avatica.SqlState}
 */
@Deprecated // will be removed before 2.0
public enum SqlStateCodes {
  CARDINALITY_VIOLATION("cardinality violation", "21", "000"),

  NULL_VALUE_NOT_ALLOWED("null value not allowed", "22", "004"),

  NUMERIC_VALUE_OUT_OF_RANGE("numeric value out of range", "22", "003");

  @SuppressWarnings("unused")
  private final String msg;
  private final String stateClass;
  private final String stateSubClass;

  SqlStateCodes(
      String msg,
      String stateClass,
      String stateSubClass) {
    this.msg = msg;
    this.stateClass = stateClass;
    this.stateSubClass = stateSubClass;
  }

  public String getStateClass() {
    return stateClass;
  }

  public String getStateSubClass() {
    return stateSubClass;
  }

  public String getState() {
    return stateClass + stateSubClass;
  }
}

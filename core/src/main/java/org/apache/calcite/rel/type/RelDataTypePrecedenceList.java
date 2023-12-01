/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: http://www.mycat.org.cn/mycat2-index.html
*    Release: https://github.com/MyCATApache/Mycat2/releases/tag/v1.13-beta-2020-12-24
*    Source File: RelDataTypePrecedenceList.java
*    
*    Copyrights:
*      copyright (c) <year>  <name of author>
*      copyright (c) <2020>  <chen junwen>
*      copyright (c) <2020>  <chenjunwen>
*      copyright (c) 2007 free software foundation, inc. <https://fsf.org/>
*      copyright treaty adopted on 20 december 1996, or
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
package org.apache.calcite.rel.type;

import org.apache.calcite.util.Glossary;

/**
 * RelDataTypePrecedenceList defines a type precedence list for a particular
 * type.
 *
 * @see Glossary#SQL99 SQL:1999 Part 2 Section 9.5
 */
public interface RelDataTypePrecedenceList {
  //~ Methods ----------------------------------------------------------------

  /**
   * Determines whether a type appears in this precedence list.
   *
   * @param type type to check
   * @return true iff this list contains type
   */
  boolean containsType(RelDataType type);

  /**
   * Compares the precedence of two types.
   *
   * <p>The list must contain both types.</p>
   *
   * @param type1 first type to compare
   * @param type2 second type to compare
   * @return positive if type1 has higher precedence; negative if type2 has
   * higher precedence; 0 if types have equal precedence
   */
  int compareTypePrecedence(RelDataType type1, RelDataType type2);
}

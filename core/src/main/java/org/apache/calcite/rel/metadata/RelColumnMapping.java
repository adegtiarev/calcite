/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: http://www.mycat.org.cn/mycat2-index.html
*    Release: https://github.com/MyCATApache/Mycat2/releases/tag/v1.13-beta-2020-12-24
*    Source File: RelColumnMapping.java
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
package org.apache.calcite.rel.metadata;

/**
 * Mapping from an input column of a {@link org.apache.calcite.rel.RelNode} to
 * one of its output columns.
 */
public class RelColumnMapping {
  public RelColumnMapping(
      int iOutputColumn, int iInputRel, int iInputColumn, boolean derived) {
    this.iOutputColumn = iOutputColumn;
    this.iInputRel = iInputRel;
    this.iInputColumn = iInputColumn;
    this.derived = derived;
  }

  //~ Instance fields --------------------------------------------------------

  /**
   * 0-based ordinal of mapped output column.
   */
  public final int iOutputColumn;

  /**
   * 0-based ordinal of mapped input rel.
   */
  public final int iInputRel;

  /**
   * 0-based ordinal of mapped column within input rel.
   */
  public final int iInputColumn;

  /**
   * Whether the column mapping transforms the input.
   */
  public final boolean derived;
}

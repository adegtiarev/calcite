/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: InvalidRelException.java
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
package org.apache.calcite.rel;

/**
 * Exception that indicates that a relational expression would be invalid
 * with given parameters.
 *
 * <p>This exception is thrown by the constructor of a subclass of
 * {@link RelNode} when given parameters it cannot accept. For example,
 * {@code EnumerableJoinRel} can only implement equi-joins, so its constructor
 * throws {@code InvalidRelException} when given the condition
 * {@code input0.x - input1.y = 2}.</p>
 *
 * <p>Because the exception is checked (i.e. extends {@link Exception} but not
 * {@link RuntimeException}), constructors that throw this exception will
 * declare this exception in their {@code throws} clause, and rules that create
 * those relational expressions will need to handle it. Usually a rule will
 * not take the exception personally, and will fail to match. The burden of
 * checking is removed from the rule, which means less code for the author of
 * the rule to maintain.</p>
 *
 * <p>The caller that receives an {@code InvalidRelException} (typically a rule
 * attempting to create a relational expression) should log it at
 * the DEBUG level.</p>
 */
public class InvalidRelException extends Exception {
  /**
   * Creates an InvalidRelException.
   */
  public InvalidRelException(String message) {
    super(message);
  }

  /**
   * Creates an InvalidRelException with a cause.
   */
  public InvalidRelException(String message, Throwable cause) {
    super(message, cause);
  }
}

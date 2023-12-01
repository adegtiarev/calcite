/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: FunctionParameter.java
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
package org.apache.calcite.schema;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;

/**
 * Parameter to a {@link Function}.
 *
 * <p>NOTE: We'd have called it {@code Parameter} but the overlap with
 * {@link java.lang.reflect.Parameter} was too confusing.</p>
 */
public interface FunctionParameter {
  /**
   * Zero-based ordinal of this parameter within the member's parameter
   * list.
   *
   * @return Parameter ordinal
   */
  int getOrdinal();

  /**
   * Name of the parameter.
   *
   * @return Parameter name
   */
  String getName();

  /**
   * Returns the type of this parameter.
   *
   * @param typeFactory Type factory to be used to create the type
   *
   * @return Parameter type.
   */
  RelDataType getType(RelDataTypeFactory typeFactory);

  /**
   * Returns whether this parameter is optional.
   *
   * <p>If true, the value of the parameter can be supplied using the DEFAULT
   * SQL keyword, or it can be omitted from a function called using argument
   * assignment, or the function can be called with fewer parameters (if all
   * parameters after it are optional too).
   *
   * <p>If a parameter is optional its default value is NULL. We may in future
   * allow functions to specify other default values.
   */
  boolean isOptional();
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/Abzhanov/piscine-go/releases/tag/calcite-1.26.0
*    Source File: HintOptionChecker.java
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
package org.apache.calcite.rel.hint;

import org.apache.calcite.util.Litmus;

/**
 * A {@code HintOptionChecker} validates the options of a {@link RelHint}.
 *
 * <p>Every hint would have a validation when converting to a {@link RelHint}, the
 * validation logic is: i) checks whether the hint was already registered;
 * ii) use the registered {@code HintOptionChecker} to check the hint options.
 *
 * <p>In {@link HintStrategyTable} the option checker is used for
 * hints registration as an optional parameter.
 *
 * @see HintStrategyTable#validateHint
 */
@FunctionalInterface
public interface HintOptionChecker {
  /**
   * Checks if the given hint is valid.
   *
   * <p>Always use the {@link Litmus#check(boolean, String, Object...)}
   * to do the check. The default behavior is to log warnings for any hint error.
   *
   * @param hint The hint to check
   * @param errorHandler The error handler
   *
   * @return True if the check passes
   */
  boolean checkOptions(RelHint hint, Litmus errorHandler);
}

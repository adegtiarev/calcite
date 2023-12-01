/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.29.0-rc0
*    Source File: SqlTypeMappingRule.java
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
package org.apache.calcite.sql.type;

import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Interface that defines rules within type mappings.
 *
 * <p>Each instance should define a type mapping matrix which actually defines
 * the rules that indicate whether one type can apply the rule to another.
 *
 * <p>Typically, the "rule" can be type assignment or type coercion.
 */
public interface SqlTypeMappingRule {

  /** Returns the type mappings of this rule instance.
   *
   * <p>The mappings should be immutable.
   */
  Map<SqlTypeName, ImmutableSet<SqlTypeName>> getTypeMapping();

  /** Returns whether it is valid to apply the defined rules from type {@code from} to
   * type {@code to}. */
  default boolean canApplyFrom(SqlTypeName to, SqlTypeName from) {
    Objects.requireNonNull(to, "to");
    Objects.requireNonNull(from, "from");

    if (to == SqlTypeName.NULL) {
      return false;
    } else if (from == SqlTypeName.NULL) {
      return true;
    }

    final Set<SqlTypeName> rule = getTypeMapping().get(to);
    if (rule == null) {
      // If you hit this assert, see the constructor of this class on how
      // to add new rule
      throw new AssertionError("No assign rules for " + to + " defined");
    }

    return rule.contains(from);
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RuleSets.java
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
package org.apache.calcite.tools;

import org.apache.calcite.plan.RelOptRule;

import com.google.common.collect.ImmutableList;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;

/**
 * Utilities for creating and composing rule sets.
 *
 * @see org.apache.calcite.tools.RuleSet
 */
public class RuleSets {
  private RuleSets() {
  }

  /** Creates a rule set with a given array of rules. */
  public static RuleSet ofList(RelOptRule... rules) {
    return new ListRuleSet(ImmutableList.copyOf(rules));
  }

  /** Creates a rule set with a given collection of rules. */
  public static RuleSet ofList(Iterable<? extends RelOptRule> rules) {
    return new ListRuleSet(ImmutableList.copyOf(rules));
  }

  /** Rule set that consists of a list of rules. */
  private static class ListRuleSet implements RuleSet {
    private final ImmutableList<RelOptRule> rules;

    ListRuleSet(ImmutableList<RelOptRule> rules) {
      this.rules = rules;
    }

    @Override public int hashCode() {
      return rules.hashCode();
    }

    @Override public boolean equals(@Nullable Object obj) {
      return obj == this
          || obj instanceof ListRuleSet
          && rules.equals(((ListRuleSet) obj).rules);
    }

    @Override public Iterator<RelOptRule> iterator() {
      return rules.iterator();
    }
  }
}

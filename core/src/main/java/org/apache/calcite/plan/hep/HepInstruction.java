/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.29.0-rc0
*    Source File: HepInstruction.java
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
package org.apache.calcite.plan.hep;

import org.apache.calcite.plan.RelOptRule;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * HepInstruction represents one instruction in a HepProgram. The actual
 * instruction set is defined here via inner classes; if these grow too big,
 * they should be moved out to top-level classes.
 */
abstract class HepInstruction {
  //~ Methods ----------------------------------------------------------------

  void initialize(boolean clearCache) {
  }

  // typesafe dispatch via the visitor pattern
  abstract void execute(HepPlanner planner);

  //~ Inner Classes ----------------------------------------------------------

  /** Instruction that executes all rules of a given class.
   *
   * @param <R> rule type */
  static class RuleClass<R extends RelOptRule> extends HepInstruction {
    @Nullable Class<R> ruleClass;

    /**
     * Actual rule set instantiated during planning by filtering all of the
     * planner's rules through ruleClass.
     */
    @Nullable Set<RelOptRule> ruleSet;

    @Override void initialize(boolean clearCache) {
      if (!clearCache) {
        return;
      }

      ruleSet = null;
    }

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that executes all rules in a given collection. */
  static class RuleCollection extends HepInstruction {
    /**
     * Collection of rules to apply.
     */
    @Nullable Collection<RelOptRule> rules;

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that executes converter rules. */
  static class ConverterRules extends HepInstruction {
    boolean guaranteed;

    /**
     * Actual rule set instantiated during planning by filtering all of the
     * planner's rules, looking for the desired converters.
     */
    @MonotonicNonNull Set<RelOptRule> ruleSet;

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that finds common relational sub-expressions. */
  static class CommonRelSubExprRules extends HepInstruction {
    @Nullable Set<RelOptRule> ruleSet;

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that executes a given rule. */
  static class RuleInstance extends HepInstruction {
    /**
     * Description to look for, or null if rule specified explicitly.
     */
    @Nullable String ruleDescription;

    /**
     * Explicitly specified rule, or rule looked up by planner from
     * description.
     */
    @Nullable RelOptRule rule;

    @Override void initialize(boolean clearCache) {
      if (!clearCache) {
        return;
      }

      if (ruleDescription != null) {
        // Look up anew each run.
        rule = null;
      }
    }

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that sets match order. */
  static class MatchOrder extends HepInstruction {
    @Nullable HepMatchOrder order;

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that sets match limit. */
  static class MatchLimit extends HepInstruction {
    int limit;

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that executes a sub-program. */
  static class Subprogram extends HepInstruction {
    @Nullable HepProgram subprogram;

    @Override void initialize(boolean clearCache) {
      if (subprogram != null) {
        subprogram.initialize(clearCache);
      }
    }

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that begins a group. */
  static class BeginGroup extends HepInstruction {
    @Nullable EndGroup endGroup;

    @Override void initialize(boolean clearCache) {
    }

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }

  /** Instruction that ends a group. */
  static class EndGroup extends HepInstruction {
    /**
     * Actual rule set instantiated during planning by collecting grouped
     * rules.
     */
    @Nullable Set<RelOptRule> ruleSet;

    boolean collecting;

    @Override void initialize(boolean clearCache) {
      if (!clearCache) {
        return;
      }

      ruleSet = new HashSet<>();
      collecting = true;
    }

    @Override void execute(HepPlanner planner) {
      planner.executeInstruction(this);
    }
  }
}

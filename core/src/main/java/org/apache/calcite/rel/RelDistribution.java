/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RelDistribution.java
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

import org.apache.calcite.plan.RelMultipleTrait;
import org.apache.calcite.util.mapping.Mappings;

import java.util.List;

/**
 * Description of the physical distribution of a relational expression.
 *
 * <p>TBD:</p>
 * <ul>
 *   <li>Can we shorten {@link Type#HASH_DISTRIBUTED} to HASH, etc.</li>
 *   <li>Do we need {@link RelDistributions}.DEFAULT?</li>
 *   <li>{@link RelDistributionTraitDef#convert}
 *       does not create specific physical operators as it does in Drill. Drill
 *       will need to create rules; or we could allow "converters" to be
 *       registered with the planner that are not trait-defs.
 * </ul>
 */
public interface RelDistribution extends RelMultipleTrait {
  /** Returns the type of distribution. */
  Type getType();

  /**
   * Returns the ordinals of the key columns.
   *
   * <p>Order is important for some types (RANGE); other types (HASH) consider
   * it unimportant but impose an arbitrary order; other types (BROADCAST,
   * SINGLETON) never have keys.
   */
  List<Integer> getKeys();

  /**
   * Applies mapping to this distribution trait.
   *
   * <p>Mapping can change the distribution trait only if it depends on distribution keys.
   *
   * <p>For example if relation is HASH distributed by keys [0, 1], after applying
   * a mapping (3, 2, 1, 0), the relation will have a distribution HASH(2,3) because
   * distribution keys changed their ordinals.
   *
   * <p>If mapping eliminates one of the distribution keys, the {@link Type#ANY}
   * distribution will be returned.
   *
   * <p>If distribution doesn't have keys (BROADCAST or SINGLETON), method will return
   * the same distribution.
   *
   * @param mapping   Mapping
   * @return distribution with mapping applied
   */
  @Override RelDistribution apply(Mappings.TargetMapping mapping);

  /** Type of distribution. */
  enum Type {
    /** There is only one instance of the stream. It sees all records. */
    SINGLETON("single"),

    /** There are multiple instances of the stream, and each instance contains
     * records whose keys hash to a particular hash value. Instances are
     * disjoint; a given record appears on exactly one stream. */
    HASH_DISTRIBUTED("hash"),

    /** There are multiple instances of the stream, and each instance contains
     * records whose keys fall into a particular range. Instances are disjoint;
     * a given record appears on exactly one stream. */
    RANGE_DISTRIBUTED("range"),

    /** There are multiple instances of the stream, and each instance contains
     * randomly chosen records. Instances are disjoint; a given record appears
     * on exactly one stream. */
    RANDOM_DISTRIBUTED("random"),

    /** There are multiple instances of the stream, and records are assigned
     * to instances in turn. Instances are disjoint; a given record appears
     * on exactly one stream. */
    ROUND_ROBIN_DISTRIBUTED("rr"),

    /** There are multiple instances of the stream, and all records appear in
     * each instance. */
    BROADCAST_DISTRIBUTED("broadcast"),

    /** Not a valid distribution, but indicates that a consumer will accept any
     * distribution. */
    ANY("any");

    public final String shortName;

    Type(String shortName) {
      this.shortName = shortName;
    }
  }
}

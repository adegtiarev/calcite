/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ComplexMetric.java
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
package org.apache.calcite.adapter.druid;

import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.sql.SqlKind;

/**
 * Stores information about available complex metrics in the Druid Adapter.
 */
public class ComplexMetric {

  /** The underlying metric column that this complex metric represents. */
  private final String metricName;

  /** The type of this metric. */
  private final DruidType type;

  public ComplexMetric(String metricName, DruidType type) {
    validate(type);
    this.metricName = metricName;
    this.type = type;
  }

  private static void validate(DruidType type) {
    if (!type.isComplex()) {
      throw new IllegalArgumentException("Druid type: " + type + " is not complex");
    }
  }

  public String getMetricName() {
    return metricName;
  }

  public DruidType getDruidType() {
    return type;
  }

  public String getMetricType() {
    switch (type) {
    case HYPER_UNIQUE:
      return "hyperUnique";
    case THETA_SKETCH:
      return "thetaSketch";
    default:
      throw new AssertionError("Type: "
              + type + " does not have an associated metric type");
    }
  }

  /**
   * Returns true if and only if this <code>ComplexMetric</code>
   * can be used in the given {@link AggregateCall}.
   * */
  public boolean canBeUsed(AggregateCall call) {
    switch (type) {
    case HYPER_UNIQUE:
    case THETA_SKETCH:
      return call != null
            && call.getAggregation().getKind() == SqlKind.COUNT
            && call.isDistinct();
    default:
      return false;
    }
  }
}

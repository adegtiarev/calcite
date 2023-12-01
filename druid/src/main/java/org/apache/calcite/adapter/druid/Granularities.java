/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: Granularities.java
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

import org.apache.calcite.avatica.util.TimeUnitRange;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Objects;

import static org.apache.calcite.adapter.druid.DruidQuery.writeFieldIf;

/**
 * Factory methods and helpers for {@link Granularity}.
 */
public class Granularities {
  // Private constructor for utility class
  private Granularities() {}

  /** Returns a Granularity that causes all rows to be rolled up into one. */
  public static Granularity all() {
    return AllGranularity.INSTANCE;
  }

  /** Creates a Granularity based on a time unit.
   *
   * <p>When used in a query, Druid will rollup and round time values based on
   * specified period and timezone. */
  public static Granularity createGranularity(TimeUnitRange timeUnit,
      String timeZone) {
    switch (timeUnit) {
    case YEAR:
      return new PeriodGranularity(Granularity.Type.YEAR, "P1Y", timeZone);
    case QUARTER:
      return new PeriodGranularity(Granularity.Type.QUARTER, "P3M", timeZone);
    case MONTH:
      return new PeriodGranularity(Granularity.Type.MONTH, "P1M", timeZone);
    case WEEK:
      return new PeriodGranularity(Granularity.Type.WEEK, "P1W", timeZone);
    case DAY:
      return new PeriodGranularity(Granularity.Type.DAY, "P1D", timeZone);
    case HOUR:
      return new PeriodGranularity(Granularity.Type.HOUR, "PT1H", timeZone);
    case MINUTE:
      return new PeriodGranularity(Granularity.Type.MINUTE, "PT1M", timeZone);
    case SECOND:
      return new PeriodGranularity(Granularity.Type.SECOND, "PT1S", timeZone);
    default:
      throw new AssertionError(timeUnit);
    }
  }

  /** Implementation of {@link Granularity} for {@link Granularity.Type#ALL}.
   * A singleton. */
  private enum AllGranularity implements Granularity {
    INSTANCE;

    @Override public void write(JsonGenerator generator) throws IOException {
      generator.writeObject("all");
    }

    @Override public Type getType() {
      return Type.ALL;
    }
  }

  /** Implementation of {@link Granularity} based on a time unit.
   * Corresponds to PeriodGranularity in Druid. */
  private static class PeriodGranularity implements Granularity {
    private final Type type;
    private final String period;
    private final String timeZone;

    private PeriodGranularity(Type type, String period, String timeZone) {
      this.type = Objects.requireNonNull(type, "type");
      this.period = Objects.requireNonNull(period, "period");
      this.timeZone = Objects.requireNonNull(timeZone, "timeZone");
    }

    @Override public void write(JsonGenerator generator) throws IOException {
      generator.writeStartObject();
      generator.writeStringField("type", "period");
      writeFieldIf(generator, "period", period);
      writeFieldIf(generator, "timeZone", timeZone);
      generator.writeEndObject();
    }

    @Override public Type getType() {
      return type;
    }
  }
}

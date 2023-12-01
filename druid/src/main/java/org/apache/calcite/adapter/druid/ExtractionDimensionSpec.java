/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ExtractionDimensionSpec.java
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

import com.fasterxml.jackson.core.JsonGenerator;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Objects;

import static org.apache.calcite.adapter.druid.DruidQuery.writeField;
import static org.apache.calcite.adapter.druid.DruidQuery.writeFieldIf;
import static org.apache.calcite.util.DateTimeStringUtils.ISO_DATETIME_FRACTIONAL_SECOND_FORMAT;

/**
 * Implementation of extraction function DimensionSpec.
 *
 * <p>The extraction function implementation returns dimension values transformed
 * using the given extraction function.
 */
public class ExtractionDimensionSpec implements DimensionSpec {
  private final String dimension;
  private final ExtractionFunction extractionFunction;
  private final String outputName;
  private final DruidType outputType;

  public ExtractionDimensionSpec(String dimension, ExtractionFunction extractionFunction,
      String outputName) {
    this(dimension, extractionFunction, outputName, DruidType.STRING);
  }

  public ExtractionDimensionSpec(String dimension, ExtractionFunction extractionFunction,
      String outputName, DruidType outputType) {
    this.dimension = Objects.requireNonNull(dimension, "dimension");
    this.extractionFunction = Objects.requireNonNull(extractionFunction, "extractionFunction");
    this.outputName = outputName;
    this.outputType = outputType == null ? DruidType.STRING : outputType;
  }

  @Override public String getOutputName() {
    return outputName;
  }

  @Override public DruidType getOutputType() {
    return outputType;
  }

  @Override public ExtractionFunction getExtractionFn() {
    return extractionFunction;
  }

  @Override public String getDimension() {
    return dimension;
  }

  @Override public void write(JsonGenerator generator) throws IOException {
    generator.writeStartObject();
    generator.writeStringField("type", "extraction");
    generator.writeStringField("dimension", dimension);
    writeFieldIf(generator, "outputName", outputName);
    writeField(generator, "extractionFn", extractionFunction);
    generator.writeEndObject();
  }

  /** Returns a valid {@link Granularity} of floor extract, or null when not
   * possible.
   *
   * @param dimensionSpec Druid Dimension specification
   */
  public static @Nullable Granularity toQueryGranularity(DimensionSpec dimensionSpec) {
    if (!DruidTable.DEFAULT_TIMESTAMP_COLUMN.equals(dimensionSpec.getDimension())) {
      // Only __time column can be substituted by granularity
      return null;
    }
    final ExtractionFunction extractionFunction = dimensionSpec.getExtractionFn();
    if (extractionFunction == null) {
      // No Extract thus no Granularity
      return null;
    }
    if (extractionFunction instanceof TimeExtractionFunction) {
      Granularity granularity = ((TimeExtractionFunction) extractionFunction).getGranularity();
      String format = ((TimeExtractionFunction) extractionFunction).getFormat();
      if (!ISO_DATETIME_FRACTIONAL_SECOND_FORMAT.equals(format)) {
        return null;
      }
      return granularity;
    }
    return null;
  }

}

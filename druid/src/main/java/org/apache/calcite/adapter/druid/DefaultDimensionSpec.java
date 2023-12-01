/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: DefaultDimensionSpec.java
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

import java.io.IOException;
import java.util.Objects;

/**
 * Default implementation of DimensionSpec.
 *
 * <p>The default implementation returns dimension values as is and optionally
 * renames the dimension.
 */
public class DefaultDimensionSpec implements DimensionSpec {

  private final String dimension;
  private final String outputName;
  private final DruidType outputType;

  public DefaultDimensionSpec(String dimension, String outputName, DruidType outputType) {
    this.dimension = Objects.requireNonNull(dimension, "dimension");
    this.outputName = Objects.requireNonNull(outputName, "outputName");
    this.outputType = outputType == null ? DruidType.STRING : outputType;
  }

  public DefaultDimensionSpec(String dimension) {
    this(dimension, dimension, null);
  }

  @Override public void write(JsonGenerator generator) throws IOException {
    generator.writeStartObject();
    generator.writeStringField("type", "default");
    generator.writeStringField("dimension", dimension);
    generator.writeStringField("outputName", outputName);
    generator.writeStringField("outputType", outputType.name());
    generator.writeEndObject();
  }

  @Override public String getOutputName() {
    return outputName;
  }

  @Override public DruidType getOutputType() {
    return outputType;
  }

  @Override public ExtractionFunction getExtractionFn() {
    return null;
  }

  @Override public String getDimension() {
    return dimension;
  }
}

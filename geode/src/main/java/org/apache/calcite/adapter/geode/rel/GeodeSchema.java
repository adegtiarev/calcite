/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: GeodeSchema.java
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
package org.apache.calcite.adapter.geode.rel;

import org.apache.calcite.adapter.geode.util.GeodeUtils;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Schema mapped onto a Geode Region.
 */
public class GeodeSchema extends AbstractSchema {

  final GemFireCache cache;
  private final List<String> regionNames;
  private ImmutableMap<String, Table> tableMap;

  public GeodeSchema(final GemFireCache gemFireCache, final Iterable<String> regionNames) {
    super();
    this.cache = Objects.requireNonNull(gemFireCache, "gemFireCache");
    this.regionNames = ImmutableList.copyOf(Objects.requireNonNull(regionNames, "regionNames"));
  }

  @Override protected Map<String, Table> getTableMap() {

    if (tableMap == null) {

      final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();

      for (String regionName : regionNames) {
        Region region = GeodeUtils.createRegion(cache, regionName);
        Table table = new GeodeTable(region);
        builder.put(regionName, table);
      }

      tableMap = builder.build();
    }

    return tableMap;
  }
}

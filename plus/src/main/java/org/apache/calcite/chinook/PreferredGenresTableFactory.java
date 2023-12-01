/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: PreferredGenresTableFactory.java
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
package org.apache.calcite.chinook;

import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.TableFactory;
import org.apache.calcite.sql.type.SqlTypeName;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * Factory for the table of genres preferred by the current user.
 */
public class PreferredGenresTableFactory implements TableFactory<AbstractQueryableTable> {
  private static final Integer[] SPECIFIC_USER_PREFERRED_GENRES =
      {1, 2, 7, 9, 15};
  private static final int FIRST_ID = 1;
  private static final int LAST_ID = 25;

  @Override public AbstractQueryableTable create(
      SchemaPlus schema,
      String name,
      Map<String, Object> operand,
      @Nullable RelDataType rowType) {
    return new AbstractQueryableTable(Integer.class) {
      @Override public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return typeFactory.builder().add("ID", SqlTypeName.INTEGER).build();
      }

      @Override public Queryable<Integer> asQueryable(
          QueryProvider qp,
          SchemaPlus sp,
          String string) {
        return fetchPreferredGenres();
      }
    };
  }

  private static Queryable<Integer> fetchPreferredGenres() {
    if (EnvironmentFairy.getUser() == EnvironmentFairy.User.SPECIFIC_USER) {
      return Linq4j.asEnumerable(SPECIFIC_USER_PREFERRED_GENRES).asQueryable();
    } else {
      final ContiguousSet<Integer> set =
          ContiguousSet.create(Range.closed(FIRST_ID, LAST_ID),
              DiscreteDomain.integers());
      return Linq4j.asEnumerable(set).asQueryable();
    }
  }
}

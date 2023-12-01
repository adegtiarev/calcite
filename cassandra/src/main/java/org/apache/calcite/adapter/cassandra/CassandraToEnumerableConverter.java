/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: CassandraToEnumerableConverter.java
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
package org.apache.calcite.adapter.cassandra;

import org.apache.calcite.adapter.enumerable.EnumerableRel;
import org.apache.calcite.adapter.enumerable.EnumerableRelImplementor;
import org.apache.calcite.adapter.enumerable.JavaRowFormat;
import org.apache.calcite.adapter.enumerable.PhysType;
import org.apache.calcite.adapter.enumerable.PhysTypeImpl;
import org.apache.calcite.config.CalciteSystemProperty;
import org.apache.calcite.linq4j.tree.BlockBuilder;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.linq4j.tree.Expressions;
import org.apache.calcite.linq4j.tree.MethodCallExpression;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterImpl;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.runtime.Hook;
import org.apache.calcite.util.BuiltInMethod;
import org.apache.calcite.util.Pair;
import org.apache.calcite.util.Util;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Relational expression representing a scan of a table in a Cassandra data source.
 */
public class CassandraToEnumerableConverter
    extends ConverterImpl
    implements EnumerableRel {
  protected CassandraToEnumerableConverter(
      RelOptCluster cluster,
      RelTraitSet traits,
      RelNode input) {
    super(cluster, ConventionTraitDef.INSTANCE, traits, input);
  }

  @Override public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
    return new CassandraToEnumerableConverter(
        getCluster(), traitSet, sole(inputs));
  }

  @Override public @Nullable RelOptCost computeSelfCost(RelOptPlanner planner,
      RelMetadataQuery mq) {
    return super.computeSelfCost(planner, mq).multiplyBy(.1);
  }

  @Override public Result implement(EnumerableRelImplementor implementor, Prefer pref) {
    // Generates a call to "query" with the appropriate fields and predicates
    final BlockBuilder list = new BlockBuilder();
    final CassandraRel.Implementor cassandraImplementor = new CassandraRel.Implementor();
    cassandraImplementor.visitChild(0, getInput());
    final RelDataType rowType = getRowType();
    final PhysType physType =
        PhysTypeImpl.of(
                implementor.getTypeFactory(), rowType,
                pref.prefer(JavaRowFormat.ARRAY));
    final Expression fields =
        list.append("fields",
            constantArrayList(
                Pair.zip(CassandraRules.cassandraFieldNames(rowType),
                    new AbstractList<Class<?>>() {
                      @Override public Class<?> get(int index) {
                        return physType.fieldClass(index);
                      }

                      @Override public int size() {
                        return rowType.getFieldCount();
                      }
                    }),
                Pair.class));
    List<Map.Entry<String, String>> selectList = new ArrayList<>();
    for (Map.Entry<String, String> entry
            : Pair.zip(cassandraImplementor.selectFields.keySet(),
                cassandraImplementor.selectFields.values())) {
      selectList.add(entry);
    }
    final Expression selectFields =
        list.append("selectFields", constantArrayList(selectList, Pair.class));
    final Expression table =
        list.append("table",
            Objects.requireNonNull(
                cassandraImplementor.table.getExpression(
                    CassandraTable.CassandraQueryable.class)));
    final Expression predicates =
        list.append("predicates",
            constantArrayList(cassandraImplementor.whereClause, String.class));
    final Expression order =
        list.append("order",
            constantArrayList(cassandraImplementor.order, String.class));
    final Expression offset =
        list.append("offset",
            Expressions.constant(cassandraImplementor.offset));
    final Expression fetch =
        list.append("fetch",
            Expressions.constant(cassandraImplementor.fetch));
    Expression enumerable =
        list.append("enumerable",
            Expressions.call(table,
                CassandraMethod.CASSANDRA_QUERYABLE_QUERY.method, fields,
                selectFields, predicates, order, offset, fetch));
    if (CalciteSystemProperty.DEBUG.value()) {
      System.out.println("Cassandra: " + predicates);
    }
    Hook.QUERY_PLAN.run(predicates);
    list.add(
        Expressions.return_(null, enumerable));
    return implementor.result(physType, list.toBlock());
  }

  /** E.g. {@code constantArrayList("x", "y")} returns
   * "Arrays.asList('x', 'y')". */
  private static <T> MethodCallExpression constantArrayList(List<T> values,
      Class<?> clazz) {
    return Expressions.call(
        BuiltInMethod.ARRAYS_AS_LIST.method,
        Expressions.newArrayInit(clazz, constantList(values)));
  }

  /** E.g. {@code constantList("x", "y")} returns
   * {@code {ConstantExpression("x"), ConstantExpression("y")}}. */
  private static <T> List<Expression> constantList(List<T> values) {
    return Util.transform(values, Expressions::constant);
  }
}

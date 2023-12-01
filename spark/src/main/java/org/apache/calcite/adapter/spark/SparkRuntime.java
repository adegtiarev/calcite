/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/ember-codemods/ember-tracked-properties-codemod/releases/tag/calcite-1.26.0
*    Source File: SparkRuntime.java
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
package org.apache.calcite.adapter.spark;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.Arrays;
import java.util.List;

/**
 * Runtime utilities for Calcite's Spark adapter. Generated code calls these
 * methods.
 */
public abstract class SparkRuntime {
  private SparkRuntime() {}

  /** Converts an array into an RDD. */
  public static <T> JavaRDD<T> createRdd(JavaSparkContext sc, T[] ts) {
    final List<T> list = Arrays.asList(ts);
    return sc.parallelize(list);
  }

  /** Converts an enumerable into an RDD. */
  public static <T> JavaRDD<T> createRdd(JavaSparkContext sc,
      Enumerable<T> enumerable) {
    final List<T> list = enumerable.toList();
    return sc.parallelize(list);
  }

  /** Converts an RDD into an enumerable. */
  public static <T> Enumerable<T> asEnumerable(JavaRDD<T> rdd) {
    return Linq4j.asEnumerable(rdd.collect());
  }

  /** Returns the Spark context for the current execution.
   *
   * <p>Currently a global variable; maybe later held within {@code root}.</p>
   */
  public static JavaSparkContext getSparkContext(DataContext root) {
    return (JavaSparkContext) SparkHandlerImpl.instance().sparkContext();
  }

  /** Combines linq4j {@link org.apache.calcite.linq4j.function.Function}
   * and Spark {@link org.apache.spark.api.java.function.FlatMapFunction}.
   *
   * @param <T> argument type
   * @param <R> result type */
  public abstract static class CalciteFlatMapFunction<T, R>
      implements FlatMapFunction<T, R>,
      org.apache.calcite.linq4j.function.Function {
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: ElasticsearchFilter.java
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
package org.apache.calcite.adapter.elasticsearch;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlKind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Objects;

/**
 * Implementation of a {@link org.apache.calcite.rel.core.Filter}
 * relational expression in Elasticsearch.
 */
public class ElasticsearchFilter extends Filter implements ElasticsearchRel {
  ElasticsearchFilter(RelOptCluster cluster, RelTraitSet traitSet, RelNode child,
      RexNode condition) {
    super(cluster, traitSet, child, condition);
    assert getConvention() == ElasticsearchRel.CONVENTION;
    assert getConvention() == child.getConvention();
  }

  @Override public @Nullable RelOptCost computeSelfCost(RelOptPlanner planner,
      RelMetadataQuery mq) {
    return super.computeSelfCost(planner, mq).multiplyBy(0.1);
  }

  @Override public Filter copy(RelTraitSet relTraitSet, RelNode input, RexNode condition) {
    return new ElasticsearchFilter(getCluster(), relTraitSet, input, condition);
  }

  @Override public void implement(Implementor implementor) {
    implementor.visitChild(0, getInput());
    ObjectMapper mapper = implementor.elasticsearchTable.mapper;
    PredicateAnalyzerTranslator translator = new PredicateAnalyzerTranslator(mapper);
    try {
      implementor.add(translator.translateMatch(condition));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (PredicateAnalyzer.ExpressionNotAnalyzableException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * New version of translator which uses visitor pattern
   * and allow to process more complex (boolean) predicates.
   */
  static class PredicateAnalyzerTranslator {
    private final ObjectMapper mapper;

    PredicateAnalyzerTranslator(final ObjectMapper mapper) {
      this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    String translateMatch(RexNode condition) throws IOException,
        PredicateAnalyzer.ExpressionNotAnalyzableException {

      StringWriter writer = new StringWriter();
      JsonGenerator generator = mapper.getFactory().createGenerator(writer);
      boolean disMax = condition.isA(SqlKind.OR);
      Iterator<RexNode> operands = ((RexCall) condition).getOperands().iterator();
      while (operands.hasNext() && !disMax) {
        if (operands.next().isA(SqlKind.OR)) {
          disMax = true;
          break;
        }
      }
      if (disMax) {
        QueryBuilders.disMaxQueryBuilder(PredicateAnalyzer.analyze(condition)).writeJson(generator);
      } else {
        QueryBuilders.constantScoreQuery(PredicateAnalyzer.analyze(condition)).writeJson(generator);
      }
      generator.flush();
      generator.close();
      return "{\"query\" : " + writer.toString() + "}";
    }
  }

}

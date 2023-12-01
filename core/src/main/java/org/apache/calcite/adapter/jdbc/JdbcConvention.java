/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/v2-org-test-4/calcite/releases/tag/calcite-1.26.0
*    Source File: JdbcConvention.java
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
package org.apache.calcite.adapter.jdbc;

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.rel.rules.CoreRules;
import org.apache.calcite.sql.SqlDialect;

/**
 * Calling convention for relational operations that occur in a JDBC
 * database.
 *
 * <p>The convention is a slight misnomer. The operations occur in whatever
 * data-flow architecture the database uses internally. Nevertheless, the result
 * pops out in JDBC.</p>
 *
 * <p>This is the only convention, thus far, that is not a singleton. Each
 * instance contains a JDBC schema (and therefore a data source). If Calcite is
 * working with two different databases, it would even make sense to convert
 * from "JDBC#A" convention to "JDBC#B", even though we don't do it currently.
 * (That would involve asking database B to open a database link to database
 * A.)</p>
 *
 * <p>As a result, converter rules from and to this convention need to be
 * instantiated, at the start of planning, for each JDBC database in play.</p>
 */
public class JdbcConvention extends Convention.Impl {
  /** Cost of a JDBC node versus implementing an equivalent node in a "typical"
   * calling convention. */
  public static final double COST_MULTIPLIER = 0.8d;

  public final SqlDialect dialect;
  public final Expression expression;

  public JdbcConvention(SqlDialect dialect, Expression expression,
      String name) {
    super("JDBC." + name, JdbcRel.class);
    this.dialect = dialect;
    this.expression = expression;
  }

  public static JdbcConvention of(SqlDialect dialect, Expression expression,
      String name) {
    return new JdbcConvention(dialect, expression, name);
  }

  @Override public void register(RelOptPlanner planner) {
    for (RelOptRule rule : JdbcRules.rules(this)) {
      planner.addRule(rule);
    }
    planner.addRule(CoreRules.FILTER_SET_OP_TRANSPOSE);
    planner.addRule(CoreRules.PROJECT_REMOVE);
  }
}

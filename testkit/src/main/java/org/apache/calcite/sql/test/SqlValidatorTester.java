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
package org.apache.calcite.sql.test;

import org.apache.calcite.sql.validate.SqlValidator;

import java.util.function.UnaryOperator;

/**
 * Tester of {@link SqlValidator}.
 */
public class SqlValidatorTester extends AbstractSqlTester {

  public SqlValidatorTester(SqlTestFactory factory) {
    this(factory, UnaryOperator.identity());
  }

  SqlValidatorTester(SqlTestFactory factory,
      UnaryOperator<SqlValidator> validatorTransform) {
    super(factory, validatorTransform);
  }

  @Override protected SqlTester with(SqlTestFactory factory) {
    return new SqlValidatorTester(factory, validatorTransform);
  }

  @Override public SqlTester withValidatorTransform(
      UnaryOperator<UnaryOperator<SqlValidator>> transform) {
    return new SqlValidatorTester(factory,
        transform.apply(validatorTransform));
  }
}

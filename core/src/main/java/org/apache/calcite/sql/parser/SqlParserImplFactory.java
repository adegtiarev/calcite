/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlParserImplFactory.java
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
package org.apache.calcite.sql.parser;

import org.apache.calcite.linq4j.function.Experimental;
import org.apache.calcite.server.DdlExecutor;

import java.io.Reader;

/**
 * Factory for
 * {@link org.apache.calcite.sql.parser.SqlAbstractParserImpl} objects.
 *
 * <p>A parser factory allows you to include a custom parser in
 * {@link org.apache.calcite.tools.Planner} created through
 * {@link org.apache.calcite.tools.Frameworks}.</p>
 */
@FunctionalInterface
public interface SqlParserImplFactory {

  /**
   * Get the underlying parser implementation.
   *
   * @return {@link SqlAbstractParserImpl} object.
   */
  SqlAbstractParserImpl getParser(Reader stream);

  /**
   * Returns a DDL executor.
   *
   * <p>The default implementation returns {@link DdlExecutor#USELESS},
   * which cannot handle any DDL commands.
   *
   * <p>DDL execution is related to parsing but it is admittedly a stretch to
   * control them in the same factory. Therefore this is marked 'experimental'.
   * We are bundling them because they are often overridden at the same time. In
   * particular, we want a way to refine the behavior of the "server" module,
   * which supports DDL parsing and execution, and we're not yet ready to define
   * a new {@link java.sql.Driver} or
   * {@link org.apache.calcite.server.CalciteServer}.
   */
  @Experimental
  default DdlExecutor getDdlExecutor() {
    return DdlExecutor.USELESS;
  }
}

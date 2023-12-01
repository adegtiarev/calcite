/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlValidatorException.java
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
package org.apache.calcite.sql.validate;

import org.apache.calcite.config.CalciteSystemProperty;
import org.apache.calcite.util.CalciteValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// NOTE:  This class gets compiled independently of everything else so that
// resource generation can use reflection.  That means it must have no
// dependencies on other Calcite code.

/**
 * Exception thrown while validating a SQL statement.
 *
 * <p>Unlike {@link org.apache.calcite.runtime.CalciteException}, this is a
 * checked exception, which reminds code authors to wrap it in another exception
 * containing the line/column context.
 */
public class SqlValidatorException extends Exception
    implements CalciteValidatorException {
  //~ Static fields/initializers ---------------------------------------------

  private static final Logger LOGGER =
      LoggerFactory.getLogger("org.apache.calcite.runtime.CalciteException");

  static final long serialVersionUID = -831683113957131387L;

  //~ Constructors -----------------------------------------------------------

  /**
   * Creates a new SqlValidatorException object.
   *
   * @param message error message
   * @param cause   underlying cause
   */
  @SuppressWarnings({"argument.type.incompatible", "method.invocation.invalid"})
  public SqlValidatorException(
      String message,
      Throwable cause) {
    super(message, cause);

    // TODO: see note in CalciteException constructor
    LOGGER.trace("SqlValidatorException", this);
    if (CalciteSystemProperty.DEBUG.value()) {
      LOGGER.error(toString());
    }
  }
}

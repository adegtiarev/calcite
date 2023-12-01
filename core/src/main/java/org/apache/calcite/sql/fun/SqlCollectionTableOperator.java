/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Release: https://github.com/cf-testorg/calcite-test/releases/tag/calcite-1.26.0-rc0
*    Source File: SqlCollectionTableOperator.java
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
package org.apache.calcite.sql.fun;

import org.apache.calcite.sql.SqlFunctionalOperator;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.validate.SqlModality;

/**
 * SqlCollectionTableOperator is the "table function derived table" operator. It
 * converts a table-valued function into a relation, e.g. "<code>SELECT * FROM
 * TABLE(ramp(5))</code>".
 *
 * <p>This operator has function syntax (with one argument), whereas
 * {@link SqlStdOperatorTable#EXPLICIT_TABLE} is a prefix operator.
 */
public class SqlCollectionTableOperator extends SqlFunctionalOperator {
  private final SqlModality modality;

  //~ Constructors -----------------------------------------------------------

  public SqlCollectionTableOperator(String name, SqlModality modality) {
    super(name, SqlKind.COLLECTION_TABLE, 200, true, ReturnTypes.ARG0, null,
        OperandTypes.CURSOR);
    this.modality = modality;
  }

  //~ Methods ----------------------------------------------------------------

  public SqlModality getModality() {
    return modality;
  }
}

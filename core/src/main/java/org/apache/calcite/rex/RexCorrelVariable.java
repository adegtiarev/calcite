/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RexCorrelVariable.java
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
package org.apache.calcite.rex;

import org.apache.calcite.rel.core.CorrelationId;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlKind;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Reference to the current row of a correlating relational expression.
 *
 * <p>Correlating variables are introduced when performing nested loop joins.
 * Each row is received from one side of the join, a correlating variable is
 * assigned a value, and the other side of the join is restarted.</p>
 */
public class RexCorrelVariable extends RexVariable {
  public final CorrelationId id;

  //~ Constructors -----------------------------------------------------------

  RexCorrelVariable(
      CorrelationId id,
      RelDataType type) {
    super(id.getName(), type);
    this.id = Objects.requireNonNull(id, "id");
  }

  //~ Methods ----------------------------------------------------------------

  @Override public <R> R accept(RexVisitor<R> visitor) {
    return visitor.visitCorrelVariable(this);
  }

  @Override public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
    return visitor.visitCorrelVariable(this, arg);
  }

  @Override public SqlKind getKind() {
    return SqlKind.CORREL_VARIABLE;
  }

  @Override public boolean equals(@Nullable Object obj) {
    return this == obj
        || obj instanceof RexCorrelVariable
        && Objects.equals(digest, ((RexCorrelVariable) obj).digest)
        && type.equals(((RexCorrelVariable) obj).type)
        && id.equals(((RexCorrelVariable) obj).id);
  }

  @Override public int hashCode() {
    return Objects.hash(digest, type, id);
  }
}

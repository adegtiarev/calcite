/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RexFieldAccess.java
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

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.sql.SqlKind;

import com.google.common.base.Preconditions;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Access to a field of a row-expression.
 *
 * <p>You might expect to use a <code>RexFieldAccess</code> to access columns of
 * relational tables, for example, the expression <code>emp.empno</code> in the
 * query
 *
 * <blockquote>
 * <pre>SELECT emp.empno FROM emp</pre>
 * </blockquote>
 *
 * <p>but there is a specialized expression {@link RexInputRef} for this
 * purpose. So in practice, <code>RexFieldAccess</code> is usually used to
 * access fields of correlating variables, for example the expression
 * <code>emp.deptno</code> in
 *
 * <blockquote>
 * <pre>SELECT ename
 * FROM dept
 * WHERE EXISTS (
 *     SELECT NULL
 *     FROM emp
 *     WHERE emp.deptno = dept.deptno
 *     AND gender = 'F')</pre>
 * </blockquote>
 */
public class RexFieldAccess extends RexNode {
  //~ Instance fields --------------------------------------------------------

  private final RexNode expr;
  private final RelDataTypeField field;

  //~ Constructors -----------------------------------------------------------

  RexFieldAccess(
      RexNode expr,
      RelDataTypeField field) {
    checkValid(expr, field);
    this.expr = expr;
    this.field = field;
    this.digest = expr + "." + field.getName();
  }

  //~ Methods ----------------------------------------------------------------

  private static void checkValid(RexNode expr, RelDataTypeField field) {
    RelDataType exprType = expr.getType();
    int fieldIdx = field.getIndex();
    Preconditions.checkArgument(
        fieldIdx >= 0 && fieldIdx < exprType.getFieldList().size()
            && exprType.getFieldList().get(fieldIdx).equals(field),
        "Field " + field + " does not exist for expression " + expr);
  }

  public RelDataTypeField getField() {
    return field;
  }

  @Override public RelDataType getType() {
    return field.getType();
  }

  @Override public SqlKind getKind() {
    return SqlKind.FIELD_ACCESS;
  }

  @Override public <R> R accept(RexVisitor<R> visitor) {
    return visitor.visitFieldAccess(this);
  }

  @Override public <R, P> R accept(RexBiVisitor<R, P> visitor, P arg) {
    return visitor.visitFieldAccess(this, arg);
  }

  /**
   * Returns the expression whose field is being accessed.
   */
  public RexNode getReferenceExpr() {
    return expr;
  }

  @Override public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RexFieldAccess that = (RexFieldAccess) o;

    return field.equals(that.field) && expr.equals(that.expr);
  }

  @Override public int hashCode() {
    int result = expr.hashCode();
    result = 31 * result + field.hashCode();
    return result;
  }
}

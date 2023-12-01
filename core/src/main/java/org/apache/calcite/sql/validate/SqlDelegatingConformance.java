/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.32.0-rc0
*    Source File: SqlDelegatingConformance.java
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

import org.apache.calcite.sql.fun.SqlLibrary;

/**
 * Implementation of {@link SqlConformance} that delegates all methods to
 * another object. You can create a sub-class that overrides particular
 * methods.
 */
public class SqlDelegatingConformance extends SqlAbstractConformance {
  private final SqlConformance delegate;

  /** Creates a SqlDelegatingConformance. */
  protected SqlDelegatingConformance(SqlConformance delegate) {
    this.delegate = delegate;
  }

  @Override public boolean isGroupByAlias() {
    return delegate.isGroupByAlias();
  }

  @Override public boolean isGroupByOrdinal() {
    return delegate.isGroupByOrdinal();
  }

  @Override public boolean isHavingAlias() {
    return delegate.isGroupByAlias();
  }

  @Override public boolean isSortByOrdinal() {
    return delegate.isSortByOrdinal();
  }

  @Override public boolean isSortByAlias() {
    return delegate.isSortByAlias();
  }

  @Override public boolean isSortByAliasObscures() {
    return delegate.isSortByAliasObscures();
  }

  @Override public boolean isFromRequired() {
    return delegate.isFromRequired();
  }

  @Override public boolean isBangEqualAllowed() {
    return delegate.isBangEqualAllowed();
  }

  @Override public boolean isMinusAllowed() {
    return delegate.isMinusAllowed();
  }

  @Override public boolean isInsertSubsetColumnsAllowed() {
    return delegate.isInsertSubsetColumnsAllowed();
  }

  @Override public boolean allowNiladicParentheses() {
    return delegate.allowNiladicParentheses();
  }

  @Override public boolean allowAliasUnnestItems() {
    return delegate.allowAliasUnnestItems();
  }

  @Override public SqlLibrary semantics() {
    return delegate.semantics();
  }

}

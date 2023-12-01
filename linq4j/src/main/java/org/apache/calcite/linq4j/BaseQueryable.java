/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: BaseQueryable.java
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
package org.apache.calcite.linq4j;

import org.apache.calcite.linq4j.tree.Expression;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Skeleton implementation of {@link Queryable}.
 *
 * <p>The default implementation of {@link #enumerator()} calls the provider's
 * {@link QueryProvider#executeQuery(Queryable)} method, but the derived class
 * can override.
 *
 * @param <TSource> Element type
 */
public abstract class BaseQueryable<TSource>
    extends AbstractQueryable<TSource> {
  protected final QueryProvider provider;
  protected final Type elementType;
  protected final @Nullable Expression expression;

  protected BaseQueryable(QueryProvider provider, Type elementType,
      @Nullable Expression expression) {
    this.provider = provider;
    this.elementType = elementType;
    this.expression = expression;
  }

  @Override public QueryProvider getProvider() {
    return provider;
  }

  @Override public Type getElementType() {
    return elementType;
  }

  @Override public @Nullable Expression getExpression() {
    return expression;
  }

  @Override public Iterator<TSource> iterator() {
    return Linq4j.enumeratorIterator(enumerator());
  }

  @Override public Enumerator<TSource> enumerator() {
    return provider.executeQuery(this);
  }
}

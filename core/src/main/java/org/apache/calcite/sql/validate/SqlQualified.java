/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: SqlQualified.java
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

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.util.Util;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Fully-qualified identifier.
 *
 * <p>The result of calling
 * {@link org.apache.calcite.sql.validate.SqlValidatorScope#fullyQualify(org.apache.calcite.sql.SqlIdentifier)},
 * a fully-qualified identifier contains the name (in correct case),
 * parser position, type, and scope of each component of the identifier.
 *
 * <p>It is immutable.
 */
public class SqlQualified {
  public final int prefixLength;
  public final @Nullable SqlValidatorNamespace namespace;
  public final SqlIdentifier identifier;

  private SqlQualified(@Nullable SqlValidatorScope scope, int prefixLength,
      @Nullable SqlValidatorNamespace namespace, SqlIdentifier identifier) {
    Util.discard(scope);
    this.prefixLength = prefixLength;
    this.namespace = namespace;
    this.identifier = identifier;
  }

  @Override public String toString() {
    return "{id: " + identifier.toString() + ", prefix: " + prefixLength + "}";
  }

  public static SqlQualified create(@Nullable SqlValidatorScope scope, int prefixLength,
      @Nullable SqlValidatorNamespace namespace, SqlIdentifier identifier) {
    return new SqlQualified(scope, prefixLength, namespace, identifier);
  }

  public final List<String> prefix() {
    return identifier.names.subList(0, prefixLength);
  }

  public final List<String> suffix() {
    return Util.skip(identifier.names, prefixLength);
  }
}

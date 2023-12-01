/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.33.0
*    Source File: SqlMoniker.java
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

import com.google.common.collect.Ordering;

import java.util.Comparator;
import java.util.List;

/**
 * An interface of an object identifier that represents a SqlIdentifier.
 */
public interface SqlMoniker {
  Comparator<SqlMoniker> COMPARATOR =
      new Comparator<SqlMoniker>() {
        final Ordering<Iterable<String>> listOrdering =
            Ordering.<String>natural().lexicographical();

        @Override public int compare(SqlMoniker o1, SqlMoniker o2) {
          int c = o1.getType().compareTo(o2.getType());
          if (c == 0) {
            c = listOrdering.compare(o1.getFullyQualifiedNames(),
                o2.getFullyQualifiedNames());
          }
          return c;
        }
      };

  //~ Methods ----------------------------------------------------------------

  /**
   * Returns the type of object referred to by this moniker. Never null.
   */
  SqlMonikerType getType();

  /**
   * Returns the array of component names.
   */
  List<String> getFullyQualifiedNames();

  /**
   * Creates a {@link SqlIdentifier} containing the fully-qualified name.
   */
  SqlIdentifier toIdentifier();

  String id();
}

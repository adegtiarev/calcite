/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: RedisSchemaFactory.java
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
package org.apache.calcite.adapter.redis;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Map;

/**
 * Factory that creates a {@link RedisSchema}.
 *
 * <p>Allows a custom schema to be included in a redis-test-model.json file.
 * See <a href="http://calcite.apache.org/docs/file_adapter.html">File adapter</a>.
 */
@SuppressWarnings("UnusedDeclaration")
public class RedisSchemaFactory implements SchemaFactory {
  // public constructor, per factory contract
  public RedisSchemaFactory() {
  }

  @Override public Schema create(SchemaPlus schema, String name,
      Map<String, Object> operand) {
    Preconditions.checkArgument(operand.get("tables") != null,
        "tables must be specified");
    Preconditions.checkArgument(operand.get("host") != null,
        "host must be specified");
    Preconditions.checkArgument(operand.get("port") != null,
        "port must be specified");
    Preconditions.checkArgument(operand.get("database") != null,
        "database must be specified");

    @SuppressWarnings("unchecked") List<Map<String, Object>> tables =
        (List) operand.get("tables");
    String host = operand.get("host").toString();
    int port = (int) operand.get("port");
    int database = Integer.parseInt(operand.get("database").toString());
    String password = operand.get("password") == null ? null
        : operand.get("password").toString();
    return new RedisSchema(host, port, database, password, tables);
  }
}

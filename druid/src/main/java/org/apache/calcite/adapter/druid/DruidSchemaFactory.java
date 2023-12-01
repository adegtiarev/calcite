/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: DruidSchemaFactory.java
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
package org.apache.calcite.adapter.druid;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.List;
import java.util.Map;

/**
 * Schema factory that creates Druid schemas.
 *
 * <table>
 *   <caption>Druid schema operands</caption>
 *   <tr>
 *     <th>Operand</th>
 *     <th>Description</th>
 *     <th>Required</th>
 *   </tr>
 *   <tr>
 *     <td>url</td>
 *     <td>URL of Druid's query node.
 *     The default is "http://localhost:8082".</td>
 *     <td>No</td>
 *   </tr>
 *   <tr>
 *     <td>coordinatorUrl</td>
 *     <td>URL of Druid's coordinator node.
 *     The default is <code>url</code>, replacing "8082" with "8081",
 *     for example "http://localhost:8081".</td>
 *     <td>No</td>
 *   </tr>
 * </table>
 */
public class DruidSchemaFactory implements SchemaFactory {
  /** Default Druid URL. */
  public static final String DEFAULT_URL = "http://localhost:8082";

  @Override public Schema create(SchemaPlus parentSchema, String name,
      Map<String, Object> operand) {
    final String url = operand.get("url") instanceof String
        ? (String) operand.get("url")
        : DEFAULT_URL;
    final String coordinatorUrl = operand.get("coordinatorUrl") instanceof String
        ? (String) operand.get("coordinatorUrl")
        : url.replace(":8082", ":8081");
    // "tables" is a hidden attribute, copied in from the enclosing custom
    // schema
    final boolean containsTables = operand.get("tables") instanceof List
        && ((List) operand.get("tables")).size() > 0;
    return new DruidSchema(url, coordinatorUrl, !containsTables);
  }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: FmppTask.kt
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

package org.apache.calcite.buildtools.fmpp

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.withGroovyBuilder

@CacheableTask
open class FmppTask @Inject constructor(
    objectFactory: ObjectFactory
) : DefaultTask() {
    @Classpath
    val fmppClasspath = objectFactory.property<Configuration>()
        .convention(project.configurations.named(FmppPlugin.FMPP_CLASSPATH_CONFIGURATION_NAME))

    @InputFile
    @PathSensitive(PathSensitivity.NONE)
    val config = objectFactory.fileProperty()

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val templates = objectFactory.directoryProperty()

    @InputFile
    @PathSensitive(PathSensitivity.NONE)
    val defaultConfig = objectFactory.fileProperty()
        .convention(templates.file("../default_config.fmpp"))

    @OutputDirectory
    val output = objectFactory.directoryProperty()
        .convention(project.layout.buildDirectory.dir("fmpp/$name"))

    /**
     * Path might contain spaces and TDD special characters, so it needs to be quoted.
     * See http://fmpp.sourceforge.net/tdd.html
     */
    private fun String.tddString() =
        "\"${toString().replace("\\", "\\\\").replace("\"", "\\\"")}\""

    @TaskAction
    fun run() {
        project.delete(output.asFileTree)
        ant.withGroovyBuilder {
            "taskdef"(
                "name" to "fmpp",
                "classname" to "fmpp.tools.AntTask",
                "classpath" to fmppClasspath.get().asPath
            )
            "fmpp"(
                "configuration" to config.get(),
                "sourceRoot" to templates.get().asFile,
                "outputRoot" to output.get().asFile,
                "data" to "tdd(${config.get().toString().tddString()}), " +
                    "default: tdd(${defaultConfig.get().toString().tddString()})"
            )
        }
    }
}

/*
*    ------ BEGIN LICENSE ATTRIBUTION ------
*    
*    Portions of this file have been appropriated or derived from the following project(s) and therefore require attribution to the original licenses and authors.
*    
*    Project: https://calcite.apache.org/
*    Release: https://github.com/apache/calcite/releases/tag/calcite-1.34.0-rc0
*    Source File: BarfingInvocationHandler.java
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
package org.apache.calcite.util;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * A class derived from <code>BarfingInvocationHandler</code> handles a method
 * call by looking for a method in itself with identical parameters. If no such
 * method is found, it throws {@link UnsupportedOperationException}.
 *
 * <p>It is useful when you are prototyping code. You can rapidly create a
 * prototype class which implements the important methods in an interface, then
 * implement other methods as they are called.</p>
 *
 * @see DelegatingInvocationHandler
 */
public class BarfingInvocationHandler implements InvocationHandler {
  //~ Constructors -----------------------------------------------------------

  protected BarfingInvocationHandler() {
  }

  //~ Methods ----------------------------------------------------------------

  @Override public @Nullable Object invoke(
      Object proxy,
      Method method,
      @Nullable Object[] args) throws Throwable {
    Class clazz = getClass();
    Method matchingMethod;
    try {
      matchingMethod =
          clazz.getMethod(
              method.getName(),
              method.getParameterTypes());
    } catch (NoSuchMethodException | SecurityException e) {
      throw noMethod(method);
    }
    if (matchingMethod.getReturnType() != method.getReturnType()) {
      throw noMethod(method);
    }

    // Invoke the method in the derived class.
    try {
      return matchingMethod.invoke(this, args);
    } catch (UndeclaredThrowableException e) {
      throw e.getCause();
    }
  }

  /**
   * Called when this class (or its derived class) does not have the required
   * method from the interface.
   */
  protected UnsupportedOperationException noMethod(Method method) {
    StringBuilder buf = new StringBuilder();
    final Class[] parameterTypes = method.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      if (i > 0) {
        buf.append(",");
      }
      buf.append(parameterTypes[i].getName());
    }
    String signature =
        method.getReturnType().getName() + " "
            + method.getDeclaringClass().getName() + "." + method.getName()
            + "(" + buf.toString() + ")";
    return new UnsupportedOperationException(signature);
  }
}

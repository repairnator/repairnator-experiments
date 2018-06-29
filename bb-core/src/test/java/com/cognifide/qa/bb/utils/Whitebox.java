/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.utils;

import java.lang.reflect.Field;

/**
 * Required after Mockito update, code taken from: http://grepcode.com/file_/repo1.maven.org/maven2/org
 * .mockito/mockito-all/1.10.19/org/mockito/internal/util/reflection/Whitebox.java/?v=source
 *
 * @author Mockito
 * @deprecated to be removed after tests are cleaned up
 * <p>
 * The original code is licensed under MIT license:
 * <p>
 * {@code
 * The MIT License
 * <p>
 * Copyright (c) 2007 Mockito contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * }
 */
@Deprecated
public class Whitebox {

  public static Object getInternalState(Object target, String field) {
    Class<?> c = target.getClass();
    try {
      Field f = getFieldFromHierarchy(c, field);
      f.setAccessible(true);
      return f.get(target);
    } catch (Exception e) {
      throw new RuntimeException(
          "Unable to get internal state on a private field. Please report to mockito mailing list.", e);
    }
  }

  public static void setInternalState(Object target, String field, Object value) {
    Class<?> c = target.getClass();
    try {
      Field f = getFieldFromHierarchy(c, field);
      f.setAccessible(true);
      f.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(
          "Unable to set internal state on a private field. Please report to mockito mailing list.", e);
    }
  }

  private static Field getFieldFromHierarchy(Class<?> clazz, String field) {
    Field f = getField(clazz, field);
    while (f == null && clazz != Object.class) {
      clazz = clazz.getSuperclass();
      f = getField(clazz, field);
    }
    if (f == null) {
      throw new RuntimeException(
          "You want me to get this field: '" + field +
              "' on this class: '" + clazz.getSimpleName() +
              "' but this field is not declared withing hierarchy of this class!");
    }
    return f;
  }

  private static Field getField(Class<?> clazz, String field) {
    try {
      return clazz.getDeclaredField(field);
    } catch (NoSuchFieldException e) {
      return null;
    }
  }
}

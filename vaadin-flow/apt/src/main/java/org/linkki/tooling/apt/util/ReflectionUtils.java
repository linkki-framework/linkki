/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.tooling.apt.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public final class ReflectionUtils {

    private ReflectionUtils() {
        // don't instantiate
    }

    public static Optional<Method> getMethodByName(List<Method> methods, String name) {
        return methods.stream().filter(it -> it.getName().equals(name)).findFirst();
    }

    public static Optional<Object> getAnnotationProperty(Annotation annotation, String propertyName) {
        try {
            return Optional.ofNullable(annotation.annotationType().getMethod(propertyName).invoke(annotation));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            return Optional.empty();
        }
    }

}

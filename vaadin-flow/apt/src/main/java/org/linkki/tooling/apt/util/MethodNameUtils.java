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

import static org.linkki.util.BeanUtils.GET_PREFIX;
import static org.linkki.util.BeanUtils.IS_PREFIX;

import java.lang.reflect.Type;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

import org.apache.commons.lang3.StringUtils;
import org.linkki.util.BeanUtils;

public class MethodNameUtils {

    private MethodNameUtils() {
        // util
    }

    /**
     * Extracts the property name out of a getter, e.g. getName becomes name. If the method name
     * does not start with "get" or "is", than the methodName itself will be returned.
     *
     * @param methodName the name of the method
     * @return property name or the method name itself if no getter.
     */
    public static String toPropertyName(Type returnType, String methodName) {
        return BeanUtils.getPropertyName(returnType, methodName);
    }

    public static boolean isGetter(String methodName) {
        return methodName.startsWith(GET_PREFIX) || methodName.startsWith(IS_PREFIX);
    }

    public static String getPropertyName(ExecutableElement method) {
        return toPropertyName(method.getReturnType().getKind(), method.getSimpleName().toString());
    }

    public static String getAspectMethodRegex(String propertyName, String aspectName) {
        return "(is|get)" + StringUtils.capitalize(propertyName) + StringUtils.capitalize(aspectName);
    }

    public static String toPropertyName(TypeKind kind, String methodName) {
        if (TypeKind.VOID.equals(kind)) {
            return toPropertyName(Void.TYPE, methodName);
        } else {
            return toPropertyName(Object.class, methodName);
        }
    }
}

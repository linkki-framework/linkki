/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */


package org.linkki.tooling.util;

import javax.lang.model.element.ExecutableElement;

import org.apache.commons.lang3.StringUtils;

public class MethodNameUtils {

    private MethodNameUtils() {
        // util
    }

    /**
     * Extracts the property name out of a getter, e.g. getName becomes name. If the method name does
     * not start with "get" or "is", than the methodName itself will be returned.
     *
     * @param methodName the name of the method
     * @return property name or the method name itself if no getter.
     */
    public static String toPropertyName(String methodName) {
        if (methodName.startsWith("get")) {
            return StringUtils.uncapitalize(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            return StringUtils.uncapitalize(methodName.substring(2));
        } else {
            return methodName;
        }
    }

    public static boolean isGetter(String methodName) {
        return methodName.startsWith("get") || methodName.startsWith("is");
    }

    public static String getPropertyName(ExecutableElement method) {
        return toPropertyName(method.getSimpleName().toString());
    }

    public static String getAspectMethodRegex(String propertyName, String aspectName) {
        return "(is|get)" + StringUtils.capitalize(propertyName) + StringUtils.capitalize(aspectName);
    }
}

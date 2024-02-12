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
package org.linkki.testbench.util;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

/**
 * Helper to access {@linkplain System#getProperty(String) system properties} as
 * {@link Optional}{@link String &lt;String&gt;}. Ignores values that contain unresolved Maven
 * properties like <code>${my.property}</code>.
 */
class SystemProperties {

    private SystemProperties() {
        // util
    }

    /**
     * Returns whether the given property is set as a system property and its value is not an
     * unresolved Maven property.
     */
    static boolean isSet(String property) {
        String value = System.getProperty(property);
        return StringUtils.isNotBlank(value) && !isUnresolvedMavenProperty(value);
    }

    private static boolean isUnresolvedMavenProperty(String value) {
        return value.startsWith("${");
    }

    /**
     * Returns an {@link Optional} containing the property's value if it is set,
     * {@link Optional#empty()} otherwise.
     */
    static Optional<String> get(String property) {
        return isSet(property) ? Optional.of(System.getProperty(property)) : Optional.empty();
    }

    /**
     * Returns the property's value or throws an exception if it is not set.
     * 
     * @param property the key for a system property
     * @param msgPrefix will be prepended to the exception
     *            message({@code "<msgPrefix>the property <property> must be set."}
     * @throws IllegalStateException if the property is not set
     */
    static String getOrThrow(String property, String msgPrefix) {
        return get(property)
                .orElseThrow(() -> new IllegalStateException(msgPrefix + "the property " + property + " must be set."));
    }

}

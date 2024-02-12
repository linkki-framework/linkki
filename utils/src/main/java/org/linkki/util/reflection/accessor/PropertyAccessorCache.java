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
package org.linkki.util.reflection.accessor;

/**
 * Global static cache for {@link PropertyAccessor PropertyAccessors}.
 */
public final class PropertyAccessorCache {

    private PropertyAccessorCache() {
        // should not be instantiated
    }

    /**
     * @param clazz a class
     * @param property a property of the class
     * @return a {@link PropertyAccessor} to access the property of instances of the class
     *
     * @deprecated as this should only be used internally. Use
     *             {@link PropertyAccessor#get(Class, String)} instead.
     */
    @Deprecated(since = "2.5")
    public static <T> PropertyAccessor<T, ?> get(Class<T> clazz, String property) {
        return PropertyAccessor.get(clazz, property);
    }
}

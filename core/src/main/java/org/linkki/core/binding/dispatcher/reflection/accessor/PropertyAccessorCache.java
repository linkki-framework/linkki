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
package org.linkki.core.binding.dispatcher.reflection.accessor;

import static java.util.Objects.requireNonNull;

import org.linkki.util.LazyInitializingMap;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Global static cache for {@link PropertyAccessor PropertyAccessors}.
 */
public final class PropertyAccessorCache {

    private static final LazyInitializingMap<CacheKey, PropertyAccessor<?, ?>> ACCESSOR_CACHE = new LazyInitializingMap<>(
            key -> new PropertyAccessor<>(key.clazz, key.property));

    private PropertyAccessorCache() {
        // should not be instantiated
    }

    /**
     * @param clazz a class
     * @param property a property of the class
     * @return a {@link PropertyAccessor} to access the property of instances of the class
     */
    @SuppressWarnings("unchecked")
    public static <T> PropertyAccessor<T, ?> get(Class<T> clazz, String property) {
        return (PropertyAccessor<T, ?>)ACCESSOR_CACHE.get(new CacheKey(clazz, property));
    }

    private static final class CacheKey {
        private final Class<?> clazz;
        private final String property;

        public CacheKey(@NonNull Class<?> clazz, String property) {
            super();
            this.clazz = requireNonNull(clazz, "clazz must not be null");
            this.property = requireNonNull(property, "property must not be null");
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + clazz.hashCode();
            result = prime * result + property.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            CacheKey other = (CacheKey)obj;
            @SuppressWarnings("java:S1872")
            boolean isSameClassName = clazz.getName().equals(other.clazz.getName());
            if (!isSameClassName) {
                return false;
            }
            if (!property.equals(other.property)) {
                return false;
            }
            return true;
        }

    }
}

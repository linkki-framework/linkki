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
package org.linkki.core.binding.dispatcher.accessor;

import static java.util.Objects.requireNonNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.linkki.util.LazyInitializingMap;

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
     * @return the accessor for the property
     */
    @SuppressWarnings("unchecked")
    public static <@NonNull T> PropertyAccessor<T, ?> get(Class<T> clazz, String property) {
        return (PropertyAccessor<T, ?>)ACCESSOR_CACHE.get(new CacheKey(clazz, property));
    }

    private static final class CacheKey {
        private final Class<@NonNull ?> clazz;
        private final String property;

        public CacheKey(@NonNull Class<@NonNull ?> clazz, @NonNull String property) {
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
        public boolean equals(@Nullable Object obj) {
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
            if (!clazz.getName().equals(other.clazz.getName())) {
                return false;
            }
            if (!property.equals(other.property)) {
                return false;
            }
            return true;
        }

    }
}

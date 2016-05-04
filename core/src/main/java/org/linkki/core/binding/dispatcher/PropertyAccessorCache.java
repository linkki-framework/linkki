/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import org.linkki.core.binding.dispatcher.accessor.PropertyAccessor;
import org.linkki.util.LazyInitializingMap;

/**
 * Global static cache for {@link PropertyAccessor PropertyAccessors}.
 *
 * @author dschwering
 */
class PropertyAccessorCache {

    private static final LazyInitializingMap<CacheKey, PropertyAccessor> ACCESSOR_CACHE = new LazyInitializingMap<>(
            key -> new PropertyAccessor(key.clazz, key.property));

    private PropertyAccessorCache() {
    }

    /**
     * @param clazz a class
     * @param property a property of the class
     * @return the accessor for the property
     */
    public static PropertyAccessor get(Class<?> clazz, String property) {
        return ACCESSOR_CACHE.get(new CacheKey(clazz, property));
    }

    private static final class CacheKey {
        private final Class<?> clazz;
        private final String property;

        public CacheKey(Class<?> clazz, String property) {
            super();
            this.clazz = clazz;
            this.property = property;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
            result = prime * result + ((property == null) ? 0 : property.hashCode());
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
            if (clazz == null) {
                if (other.clazz != null) {
                    return false;
                }
            } else if (!clazz.getName().equals(other.clazz.getName())) {
                return false;
            }
            if (property == null) {
                if (other.property != null) {
                    return false;
                }
            } else if (!property.equals(other.property)) {
                return false;
            }
            return true;
        }

    }
}

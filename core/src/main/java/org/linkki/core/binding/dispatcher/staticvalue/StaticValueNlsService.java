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
package org.linkki.core.binding.dispatcher.staticvalue;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.nls.NlsService;
import org.linkki.core.uiframework.UiFramework;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Service for Native Language Support for presentation model objects. This class should only be used
 * internally.
 * 
 * TODO: make package-private after LIN-3442 has been implemented
 */
public class StaticValueNlsService {
    /**
     * TODO: can be removed after LIN-3442 has been implemented
     */
    public static final String CAPTION_KEY = "caption";
    /**
     * default bundle name for PMO. We search properties file with such name in package with PMO class
     */
    private static final String DEFAULT_PMO_BUNDLE_NAME = "linkki-messages";
    private static final StaticValueNlsService INSTANCE = new StaticValueNlsService();
    private final Map<CacheKey, String> nlsStringCache = new ConcurrentSkipListMap<>();

    /**
     * Creates a {@link StaticValueNlsService} using the "linkki-messages" appended to the given pmo
     * classes' package name as the bundle name.
     */
    private StaticValueNlsService() {
        // created as singleton
    }

    /**
     * @return Singleton instance of {@link StaticValueNlsService}
     */
    public static StaticValueNlsService getInstance() {
        return INSTANCE;
    }

    private static Optional<String> getString(CacheKey cacheKey, Function<CacheKey, String> propertyKeyFunction) {
        return NlsService.get().getString(cacheKey.getBundleName(), propertyKeyFunction.apply(cacheKey));
    }

    /* private */static void clearCache() {
        INSTANCE.nlsStringCache.clear();
    }

    /**
     * Gets the internationalized String for the given <code>propertyName</code> and
     * <code>aspectName</code> of the given <code>PMO</code> class. {@link UiFramework#getLocale()} is
     * used as locale.
     * 
     * @param pmoClass PMO class for which the internationalized String should be retrieved
     * @param propertyName name of the property
     * @param aspectName name of the aspect
     * @param fallbackValue value that is returned if there is no corresponding resources
     */
    public String getString(Class<?> pmoClass, String propertyName, String aspectName, String fallbackValue) {
        var key = new CacheKey(requireNonNull(pmoClass), propertyName, aspectName, fallbackValue,
                UiFramework.getLocale());
        return nlsStringCache.computeIfAbsent(key, this::computeLabel);
    }

    /* private */String computeLabel(CacheKey cacheKey) {
        return getClassesForPropertyKeyLookup(cacheKey.pmoClass).stream()
                .flatMap(c -> getStringDeclaredDirectlyIn(cacheKey.forClass(c)).stream())
                .findFirst()
                .orElse(cacheKey.fallbackValue);
    }

    private Optional<String> getStringDeclaredDirectlyIn(CacheKey cacheKey) {
        return getString(cacheKey, CacheKey::getPropertyKey).or(() -> getStringWithFallbackKey(cacheKey));
    }

    /**
     * Returns an ordered list of classes for whose classname is used to create the key to lookup
     * properties. Uses the same order as {@link AspectAnnotationReader}, meaning the <code>clazz</code>
     * is always the highest priority. After that, all superclasses except {@code Object} as well as
     * implemented interfaces and their superinterfaces are added iteratively.
     */
    /* private */List<Class<?>> getClassesForPropertyKeyLookup(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        if (clazz == null || Object.class.equals(clazz)) {
            return classes;
        }
        classes.add(clazz);
        classes.addAll(getClassesForPropertyKeyLookup(clazz.getSuperclass()));
        Arrays.stream(clazz.getInterfaces()).flatMap(c -> getClassesForPropertyKeyLookup(c).stream())
                .forEach(classes::add);
        return classes;
    }

    /**
     * Looks for a property-key with a singular "_" (underscore). This way of defining keys is
     * deprecated. The correct way of defining a caption key follows the pattern for a label, being
     * classname_propertyname_aspect. For a caption property name it would be empty and aspect=caption.
     */
    private Optional<String> getStringWithFallbackKey(CacheKey cacheKey) {
        return Optional.of(cacheKey.propertyName)
                .filter(String::isEmpty)
                .flatMap($ -> getString(cacheKey, CacheKey::getFallbackPropertyKey));
    }

    /* private */ record CacheKey(Class<?> pmoClass, String propertyName, String aspectName, String fallbackValue,
            Locale locale)
            implements Comparable<CacheKey> {

        @Override
        public int compareTo(@NonNull CacheKey other) {
            return Comparator.comparing((CacheKey o) -> o.pmoClass.getSimpleName())
                    .thenComparing(CacheKey::propertyName)
                    .thenComparing(CacheKey::aspectName)
                    .thenComparing(cacheKey -> cacheKey.locale.toString())
                    .thenComparing(CacheKey::fallbackValue)
                    .compare(this, other);
        }

        private CacheKey forClass(Class<?> clazz) {
            return new CacheKey(clazz, propertyName, aspectName, fallbackValue, locale);
        }


        private String getPropertyKey() {
            return pmoClass.getSimpleName() + '_' + propertyName + '_' + aspectName;
        }

        private String getFallbackPropertyKey() {
            return pmoClass.getSimpleName() + '_' + aspectName;
        }

        private String getBundleName() {
            return pmoClass.getPackage().getName() + '.' + DEFAULT_PMO_BUNDLE_NAME;
        }
    }

}

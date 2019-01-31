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

package org.linkki.util.service;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * Helper for {@link ServiceLoader} access.
 */
public final class Services {

    private static final Map<Class<?>, Object> INSTANCES = new HashMap<>();

    private Services() {
        // util
    }

    /**
     * Finds the single implementation of the service class via {@link ServiceLoader}.
     * 
     * @return the service instance
     * @throws IllegalStateException if there is no or more than one implementation available
     */
    public static <S> S get(Class<S> serviceClass) {
        @SuppressWarnings("unchecked")
        S service = (S)INSTANCES.computeIfAbsent(serviceClass, s -> {
            ServiceLoader<S> serviceLoader = ServiceLoader.load(serviceClass);
            return StreamSupport.stream(serviceLoader.spliterator(), false).reduce((f1, f2) -> {
                throw new IllegalStateException(
                        "Multiple implementations of " + serviceClass.getName() + " found on the classpath.");
            }).orElseThrow(() -> new IllegalStateException(
                    "No implementation of " + serviceClass.getName() + " found on the classpath."));
        });
        return service;
    }

}

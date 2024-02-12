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

package org.linkki.util.reflection;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.function.Function;

/**
 * JDK-independent provider for {@link Lookup} instances.
 */
public class LookupProvider {
    private static Function<Class<?>, Lookup> lookupIn;
    static {
        lookupIn = clazz -> {
            try {
                Lookup lookup = MethodHandles.lookup();
                lookup.lookupClass().getModule().addReads(clazz.getModule());
                return MethodHandles.privateLookupIn(clazz, lookup);
            } catch (IllegalAccessException | IllegalArgumentException e2) {
                throw new IllegalStateException("Can't create " + Lookup.class.getSimpleName() + " for "
                        + clazz.getName(), e2);
            }
        };
    }

    private LookupProvider() {
        // prevent instantiation
    }

    public static Lookup lookup(Class<?> clazz) {
        return lookupIn.apply(clazz);
    }
}

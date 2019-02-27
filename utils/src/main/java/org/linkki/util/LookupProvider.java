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

package org.linkki.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;

/**
 * JDK-independent provider for {@link Lookup} instances.
 */
public class LookupProvider {
    private static Function<@NonNull Class<?>, @NonNull Lookup> lookupIn;
    static {
        try {
            Method privateLookupInMethod = MethodHandles.class.getMethod(new String(
                    // privateLookupIn; Somehow Java 11 uses compact Strings that f**k up
                    // String#equals when looking up the method from a class compiled with Java8
                    new int[] { 112, 114, 105, 118, 97, 116, 101, 76, 111, 111, 107, 117, 112, 73, 110 }, 0, 15),
                                                                         Class.class,
                                                                         Lookup.class);
            lookupIn = clazz -> {
                try {
                    return (Lookup)privateLookupInMethod.invoke(null, clazz, MethodHandles.lookup());
                } catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e2) {
                    throw new IllegalStateException("Can't create " + Lookup.class.getSimpleName() + " for "
                            + clazz.getName(), e2);
                }
            };
        } catch (NoSuchMethodException | SecurityException e) {
            // that's OK, we're probably still running on Java 8
            Constructor<Lookup> constructor = getPackagePrivateLookupConstructor();
            lookupIn = clazz -> {
                try {
                    return constructor.newInstance(clazz);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e2) {
                    throw new IllegalStateException("Can't create " + Lookup.class.getSimpleName() + " for "
                            + clazz.getName(), e2);
                }
            };
        }
    }

    private LookupProvider() {
        // util
    }

    private static Constructor<Lookup> getPackagePrivateLookupConstructor() {
        try {
            Constructor<Lookup> constructor = Lookup.class.getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException | SecurityException e1) {
            throw new IllegalStateException("Can't access constructor " + Lookup.class.getSimpleName() + '('
                    + Class.class.getSimpleName() + ')');
        }
    }

    @SuppressWarnings("null")
    public static Lookup lookup(Class<?> clazz) {
        return lookupIn.apply(clazz);
    }
}

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

import java.lang.reflect.InvocationTargetException;

/**
 * Utility for handling {@link Class Classes}.
 */
public class Classes {

    private Classes() {
        // prevent instantiation
    }

    /**
     * Instantiates the given class with it's no-arguments constructor.
     * 
     * @param <T> the class
     * @param clazz the class
     * @return an instance of the class
     * @throws IllegalArgumentException if the class has no accessible no-arguments constructor or if
     *             calling it fails for any reason
     */
    public static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz
                    .getConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(
                    String.format("Cannot instantiate %s", clazz.getName()),
                    e);
        }
    }
}
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

package org.linkki.ips.ui.element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;

import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.uiframework.UiFramework;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A caption provider that returns a string in the format "name [id]". The first existing method of
 * {@code getName(Locale)}, {@code getName()} and {@code toString()} is invoked to obtain the name,
 * {@code getId()} to obtain the id. The locale is determined by {@link UiFramework#getLocale()}.
 */
public class IdAndNameCaptionProvider implements ItemCaptionProvider<Object> {

    @Override
    public String getCaption(Object o) {
        return getName(o) + " [" + getId(o) + "]";
    }

    @CheckForNull
    private String getId(Object value) {
        Optional<Method> getIdMethod = getMethod(value, "getId");
        if (getIdMethod.isPresent()) {
            return invokeStringMethod(getIdMethod.get(), value);
        }

        throw new IllegalStateException("Cannot get id from object " + value);
    }

    @CheckForNull
    private static String getName(Object value) {
        Optional<Method> getLocalizedNameMethod = getMethod(value, "getName", Locale.class);
        if (getLocalizedNameMethod.isPresent()) {
            return invokeStringMethod(getLocalizedNameMethod.get(), value, UiFramework.getLocale());
        }

        Optional<Method> getNameMethod = getMethod(value, "getName");
        if (getNameMethod.isPresent()) {
            return invokeStringMethod(getNameMethod.get(), value);
        }

        Method toStringMethod = getMethod(value, "toString").get();
        return invokeStringMethod(toStringMethod, value);
    }

    private static Optional<Method> getMethod(Object value, String name, Class<?>... parameters) {
        try {
            Method method = value.getClass().getMethod(name, parameters);
            return Optional.of(method);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    @CheckForNull
    private static String invokeStringMethod(Method method, Object value, Object... parameters) {
        try {
            return (String)method.invoke(value, parameters);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(
                    "Can't get value from method " + value.getClass() + "#" + method.getName() + " of " + value);
        }
    }
}

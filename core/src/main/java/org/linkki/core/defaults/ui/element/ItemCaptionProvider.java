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
package org.linkki.core.defaults.ui.element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.defaults.nls.NlsText;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.util.reflection.Classes;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * An {@link ItemCaptionProvider} is used to get the caption for an item in a selection UI component
 * like a combo box.
 * <p>
 * Due to the current VAADIN implementation we cannot be fully type safe.
 * <p>
 * We provide three default implementations:
 * <ul>
 * <li>{@link DefaultCaptionProvider} calls a method {@code getName()} on the value object.</li>
 * <li>{@link ToStringCaptionProvider} simply uses the object's {@link Object#toString()}
 * method.</li>
 * </ul>
 * <p>
 * The {@link ItemCaptionProvider} is called once for each ComboBox and each value during each
 * update. This is necessary to detect possible changes of the caption for the same object. It is
 * therefore particularly important that the ItemCaptionProvider is very fast.
 */
@FunctionalInterface
public interface ItemCaptionProvider<T> {

    /**
     * Instantiates an {@link ItemCaptionProvider} using the default constructor.
     * <p>
     * The method uses {@link Classes#instantiate(Supplier, Class)} with the
     * {@link DefaultCaptionProvider} as fallback. This may not be the correct default for every
     * {@link ItemCaptionProvider} but is only evaluated by an annotation processor.
     * 
     * @throws LinkkiBindingException if the class could not be instantiated
     */
    static ItemCaptionProvider<?> instantiate(Supplier<Class<? extends ItemCaptionProvider<?>>> cls) {
        try {
            return Classes.instantiate(cls, DefaultCaptionProvider.class);
        } catch (IllegalArgumentException e) {
            throw new LinkkiBindingException(
                    "Cannot instantiate item caption provider " + Classes.getTypeName(cls)
                            + " using default constructor.",
                    e);
        }
    }

    /**
     * Returns the text that should be displayed for the specified value. Depending on the
     * implementation, this value may be localized.
     * 
     * @implNote {@link UiFramework#getLocale()} can be used to determine the current locale if this
     *           caption provider supports localization.
     * 
     * @param value The value for which we need a caption. May be null.
     * @return The caption for the specified value
     */
    String getCaption(@CheckForNull T value);

    /**
     * Returns the caption for the <code>null</code> value. Default is the empty String.
     * 
     * @return A caption for the <code>null</code> value
     */
    default String getNullCaption() {
        return StringUtils.EMPTY;
    }

    /**
     * This is the unsafe version of {@link #getCaption(Object)}. The framework will only call this
     * method because the type is not necessarily known in the UI selection component. When
     * implementing this interface you could ignore this method because it simply delegates to
     * {@link #getCaption(Object)} which is the type-safe variant for you, or to
     * {@link #getNullCaption()} if the value is <code>null</code>. However, if anybody does
     * something nasty we would get a ClassCastException right here.
     * 
     * @param value The value for which we need a caption
     * @return The caption for the specified value
     */
    @SuppressWarnings("unchecked")
    default String getUnsafeCaption(@CheckForNull Object value) {
        if (value == null) {
            return getNullCaption();
        } else {
            return getCaption((T)value);
        }
    }

    /**
     * A simple caption provider that uses an item's toString() method as its caption.
     * <p>
     * It is generally not a good idea to use this unless your list consists of Strings.
     * 
     */
    class ToStringCaptionProvider implements ItemCaptionProvider<Object> {
        @Override
        public String getCaption(Object t) {
            return Optional.ofNullable(t).map(Object::toString).orElse("");
        }
    }

    /**
     * A caption provider that invokes the first existing method of {@code getName(Locale)},
     * {@code getName()} and {@code toString()} to obtain the caption. The locale is determined by
     * {@link UiFramework#getLocale()}.
     */
    class DefaultCaptionProvider implements ItemCaptionProvider<Object> {

        private static final Map<Class<?>, Function<Object, String>> CACHE = new ConcurrentHashMap<>();

        @CheckForNull
        private final Locale locale;

        /**
         * Default constructor that uses the default locale value provided by
         * {@link UiFramework#getLocale()}
         */
        public DefaultCaptionProvider() {
            this.locale = null;
        }

        /**
         * Constructor to set the local value manually
         * 
         * @param locale the locale to be set as the locale value for this class.
         */
        public DefaultCaptionProvider(Locale locale) {
            this.locale = locale;
        }

        @Override
        public String getCaption(Object o) {
            if (o instanceof Boolean b) {
                return booleanToCaption(b);
            }
            String name = CACHE.computeIfAbsent(o.getClass(), this::getNameFunction).apply(o);
            return StringUtils.defaultString(name);
        }

        private Function<Object, String> getNameFunction(Class<?> type) {
            Optional<Method> getLocalizedNameMethod = getMethod(type, "getName", Locale.class);
            if (getLocalizedNameMethod.isPresent()) {
                Method m = getLocalizedNameMethod.get();
                return o -> invokeStringMethod(m, o, getLocale());
            }

            Optional<Method> getNameMethod = getMethod(type, "getName");
            if (getNameMethod.isPresent()) {
                Method m = getNameMethod.get();
                return o -> invokeStringMethod(m, o);
            } else {
                return Object::toString;
            }
        }

        private static Optional<Method> getMethod(Class<?> type, String name, Class<?>... parameters) {
            try {
                Method method = type.getMethod(name, parameters);
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
                        String.format("Can't get value from method %s#%s of %s: %s", value.getClass(), method.getName(),
                                      value, e.getMessage()),
                        e);
            }
        }

        private String booleanToCaption(boolean bool) {
            return bool
                    ? NlsText.getString("DefaultCaptionProvider.True", getLocale()) // $NON-NLS-1$
                    : NlsText.getString("DefaultCaptionProvider.False", getLocale()); // $NON-NLS-1$
        }

        private Locale getLocale() {
            return locale == null ? UiFramework.getLocale() : locale;
        }
    }

}

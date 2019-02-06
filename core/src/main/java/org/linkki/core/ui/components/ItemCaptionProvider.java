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
package org.linkki.core.ui.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.Nullable;

/**
 * An {@link ItemCaptionProvider} is used to get the caption for an item in a selection UI component
 * like a combo box.
 * <p>
 * Due to the current VAADIN implementation we cannot be fully type safe.
 * <p>
 * We provide three default implementations:
 * <ul>
 * <li>{@link DefaultCaptionProvider} calls a method {@code getName()} on the value object.</li>
 * <li>{@link ToStringCaptionProvider} simply uses the object's {@link Object#toString()} method.</li>
 * <li>{@link IdAndNameCaptionProvider} calls the methods {@code getName()} and {@code getId()} on the
 * value object and returns a caption in the format "name [id]".</li>
 * </ul>
 */
@FunctionalInterface
public interface ItemCaptionProvider<T> {

    /**
     * Get the text that should be displayed for the specified value.
     * 
     * @param value The value for which we need a caption
     * @return The caption for the specified value
     */
    String getCaption(T value);

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
     * method because the type is not necessarily known in the UI selection component. When implementing
     * this interface you could ignore this method because it simply delegates to
     * {@link #getCaption(Object)} which is the type-safe variant for you. However, if anybody does
     * something nasty we would get a ClassCastException right here.
     * 
     * @param value The value for which we need a caption
     * @return The caption for the specified value
     */
    @SuppressWarnings("unchecked")
    default String getUnsafeCaption(Object value) {
        return getCaption((T)value);
    }

    /**
     * A simple caption provider that uses an item's toString() method as its caption.
     * <p>
     * It is in general no good idea to use this, except your list consists of Strings.
     * 
     */
    public class ToStringCaptionProvider implements ItemCaptionProvider<Object> {
        @Override
        public String getCaption(Object t) {
            return Optional.ofNullable(t).map(Object::toString).orElse("");
        }
    }

    /**
     * A caption provider that returns a string in the format "name" and invokes the method
     * {@code getName} to obtain these values.
     */
    public class DefaultCaptionProvider implements ItemCaptionProvider<Object> {

        @Override
        public String getCaption(Object o) {
            String name = getName(o);
            return name != null ? name : StringUtils.EMPTY;
        }

        @Nullable
        private String getName(Object value) {
            return getPropertyValue(value, "getName");
        }

        @Nullable
        private String getPropertyValue(Object value, String methodName) {
            try {
                Method method = value.getClass().getMethod(methodName);
                return (String)method.invoke(value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new IllegalStateException(
                        "Can't get value from method " + value.getClass() + "#" + methodName + " of " + value, e);
            }
        }

    }

    /**
     * A caption provider that returns a String in the format "name [id]" and invokes the methods
     * {@code getName} and {@code getId} to obtain these values.
     */
    public class IdAndNameCaptionProvider implements ItemCaptionProvider<Object> {

        @Override
        public String getCaption(Object o) {
            return getName(o) + " [" + getId(o) + "]";
        }

        @Nullable
        private String getId(Object value) {
            return getPropertyValue(value, "getId");
        }

        @Nullable
        private String getName(Object value) {
            return getPropertyValue(value, "getName");
        }

        @Nullable
        private String getPropertyValue(Object value, String methodName) {
            try {
                Method method = value.getClass().getMethod(methodName);
                return (String)method.invoke(value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new IllegalStateException(
                        "Can't get value from method " + value.getClass() + "#" + methodName + " of " + value);

            }
        }

    }

}

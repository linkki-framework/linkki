/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import javax.annotation.CheckForNull;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.AbstractSelect;

/**
 * An {@link ItemCaptionProvider} is used to get the caption for an item in an
 * {@link AbstractSelect}.
 * <p>
 * Due to the current VAADIN implementation we cannot be fully type safe.
 * <p>
 * We provide two three default implementations:
 * <ul>
 * <li>{@link DefaultCaptionProvider} calls a method {@code getName()} on the value object.</li>
 * <li>{@link ToStringCaptionProvider} simply uses the object's {@link Object#toString()} method.
 * </li>
 * <li>{@link IdAndNameCaptionProvider} calls the methods {@code getName()} and {@code getId()} on
 * the value object and returns a caption in the format "name [id]".</li>
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
     * method because vaadin only handles the type {@link Object} in {@link AbstractSelect}. When
     * implementing this interface you could ignore this method because it simply delegates to
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
     * A caption provider that returns a string in the format "name [id]" and invokes methods
     * {@code getName} and {@code getId} to obtain these values.
     */
    public class DefaultCaptionProvider implements ItemCaptionProvider<Object> {

        @Override
        public String getCaption(Object o) {
            String name = getName(o);
            return name != null ? name : StringUtils.EMPTY;
        }

        @CheckForNull
        private String getName(Object value) {
            return getPropertyValue(value, "getName");
        }

        @CheckForNull
        private String getPropertyValue(Object value, String methodName) {
            try {
                Method method = value.getClass().getMethod(methodName);
                return (String)method.invoke(value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new IllegalStateException("Can't get value from method " + methodName + ", value was " + value);
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

        @CheckForNull
        private String getId(Object value) {
            return getPropertyValue(value, "getId");
        }

        @CheckForNull
        private String getName(Object value) {
            return getPropertyValue(value, "getName");
        }

        @CheckForNull
        private String getPropertyValue(Object value, String methodName) {
            try {
                Method method = value.getClass().getMethod(methodName);
                return (String)method.invoke(value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new IllegalStateException("Can't get value from method " + methodName + ", value was " + value);
            }
        }

    }

}

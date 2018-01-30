/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.binding.aspect;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.binding.dispatcher.PropertyDispatcher;

/**
 * The linkki binding in general defines a synchronization link between a UI component and a
 * corresponding property in the presentation model. More precisely, the binding does not only
 * handle the value of the property but may also handle other information like visible, enabled,
 * read-only, required, etc. Every single kind of information is called an aspect of the binding.
 * <p>
 * Every aspect has a unique name. First this name identifies the aspect (the kind of needed
 * information) and second the name could be used to retrieve the information from the model. For
 * more information see {@link #getName()}.
 * <p>
 * In addition to the name an {@link Aspect} could have a static value. If there is no static value
 * it is called dynamic. A static value might be a value that is configured statically for example
 * in the UI field annotation. Although the value is static it might be overruled by a
 * {@link PropertyDispatcher}. Keep in mind that the existence of a static value does not mean that
 * the value is not <code>null</code>. In some cases the aspect might define a static value that is
 * the <code>null</code> value.
 */
public class Aspect<T> {

    private final String name;
    private final Value<T> value;

    private Aspect(String name, Value<T> value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Every aspect has a unique name. First this name identifies the aspect (the kind of needed
     * information) and second the name could be used to retrieve the information from the model.
     * For example the aspect whether a field is visible or not is called "visible". If a
     * {@link PropertyDispatcher} want to handle the visible-Aspect in any special case it could
     * rely on this name. An other {@link PropertyDispatcher} may want to call a method that is
     * suffixed with the name "visible".
     * <p>
     * E.g. in order to get the visibility of the property "address", the aspect would have the name
     * "visible". A {@link PropertyDispatcher} would call a method called "isAddressVisible()" to
     * retrieve the visible state for the property "address".
     * 
     * @return the name of this aspect
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the static value of the aspect if there is any. A static value might be a value that
     * is derived from the UI field annotation. It is up to the dispatcher whether this static value
     * is used or not.
     * 
     * @return the static value
     * 
     * @throws NoSuchElementException if no static value is present, if this behaviour is not needed
     *             use {@link #isStatic()}
     */
    @CheckForNull
    public T getStaticValue() {
        return value.getStatic();
    }

    /**
     * Returns the static value. If there is no static value (the aspect is dynamic) the given
     * supplier will be applied.
     * 
     * @param supplier that will be called if there is no static value available
     * @return either the static value of this aspect or the suppliers return value
     * 
     * @see #getStaticValue()
     */
    @CheckForNull
    public T getStaticValueOr(Supplier<T> supplier) {
        return value.orElseGet(supplier);
    }

    /**
     * Returns <code>true</code> if the aspect is static, otherwise <code>false</code>. The static
     * value might be <code>null</code>.
     * 
     * @return <code>true</code> whether the aspect has a static value
     */
    public boolean isStatic() {
        return value.isStatic;
    }

    /**
     * Creates a new {@link Aspect} with the same name and the given static value.
     * 
     * @param staticValue the new static value
     * @return a new {@link Aspect} containing the given static value and the same name like this
     *         {@link Aspect}
     */
    public Aspect<T> toStatic(T staticValue) {
        return ofStatic(name, staticValue);
    }

    /**
     * Creates a new dynamic {@link Aspect} with the given name and no static value.
     * 
     * @param name the name of the new aspect
     * @return a new {@link Aspect} that has no static value
     */
    public static <T> Aspect<T> newDynamic(String name) {
        return new Aspect<>(name, new Value<>(false, null));
    }

    /**
     * Creates a new static {@link Aspect} with the given name and value.
     * 
     * @param name the name of the new aspect
     * @param value the static value of the aspect
     * @return a new {@link Aspect} that has a static value
     */
    public static <T> Aspect<T> ofStatic(String name, @Nullable T value) {
        return new Aspect<>(name, new Value<>(true, value));
    }

    /**
     * Wraps a static value if there is any. This is similar to {@link Optional} but the value may
     * also be null although it is marked as being present. The existence of a value means that no
     * dynamic value should be retrieved.
     */
    private static class Value<T> {

        private final boolean isStatic;

        @Nullable
        private final T staticValue;

        private Value(boolean isStatic, @Nullable T staticValue) {
            this.isStatic = isStatic;
            this.staticValue = staticValue;
        }

        public boolean isStatic() {
            return isStatic;
        }

        @CheckForNull
        public T getStatic() {
            if (isStatic()) {
                return staticValue;
            } else {
                throw new NoSuchElementException("There is no static value");
            }
        }

        @CheckForNull
        public T orElseGet(Supplier<T> valueSupplier) {
            if (isStatic()) {
                return getStatic();
            } else {
                return valueSupplier.get();
            }
        }

    }
}

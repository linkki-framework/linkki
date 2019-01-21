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

package org.linkki.core.binding.aspect;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
 * In addition to the name an {@link Aspect} could have a value. A value might be something that is
 * configured statically for example in the UI field annotation. Although the value is present it
 * might be overruled by a {@link PropertyDispatcher}. Keep in mind that the existence of a value
 * does not mean that the value is not <code>null</code>. In some cases the aspect might define a
 * value that is the <code>null</code> value.
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
     * information) and second the name could be used to retrieve the information from the model. For
     * example the aspect whether a field is visible or not is called "visible". If a
     * {@link PropertyDispatcher} want to handle the visible-Aspect in any special case it could rely on
     * this name. An other {@link PropertyDispatcher} may want to call a method that is suffixed with
     * the name "visible".
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
     * Returns the value of the aspect if there is any. A value might be a value that is derived from
     * the UI field annotation. It is up to the dispatcher whether this value is used or not.
     * 
     * @return the value of this aspect. The value might be <code>null</code>
     * 
     * @throws NoSuchElementException if no value is present, if this behavior is not needed use
     *             {@link #isValuePresent()}
     */
    public T getValue() {
        if (isValuePresent()) {
            return value.get();
        } else {
            throw new NoSuchElementException("There is no value for the aspect " + name);
        }
    }

    /**
     * Returns the value or if there is no value the given supplier will be applied.
     * 
     * @param supplier that will be called if there is no value available
     * @return either the value of this aspect or the suppliers return value
     * 
     * @see #getValue()
     */
    public T getValueOr(Supplier<T> supplier) {
        return value.orElseGet(supplier);
    }

    /**
     * Returns <code>true</code> if the aspect has a value, otherwise <code>false</code>. The value
     * might be <code>null</code>.
     * 
     * @return <code>true</code> whether the aspect has a value
     */
    public boolean isValuePresent() {
        return value.valuePresent;
    }

    /**
     * Creates a new {@link Aspect} with the same name and the given value.
     * 
     * @param newValue the new value that should be set.
     * @return a new {@link Aspect} containing the given value and the same name like this
     *         {@link Aspect}
     */
    public Aspect<T> with(T newValue) {
        return of(name, newValue);
    }

    /**
     * Creates a new {@link Aspect} with the given name and no value.
     * 
     * @param name the name of the new aspect
     * @return a new {@link Aspect} that has no value
     */
    public static <T> Aspect<T> of(String name) {
        return new Aspect<>(name, new Value<>());
    }

    /**
     * Creates a new {@link Aspect} with the given name and value.
     * 
     * @param name the name of the new aspect
     * @param value the value of the aspect
     * @return a new {@link Aspect} that has a value
     */
    public static <T> Aspect<T> of(String name, T value) {
        return new Aspect<>(name, new Value<>(true, value));
    }

    @Override
    public String toString() {
        return String.format("Aspect: '%s', %s", name.isEmpty() ? "VALUE" : name, value);
    }

    /**
     * Wraps a value if there is any. This is similar to {@link Optional} but the value may also be null
     * although it is marked as being present.
     */
    private static class Value<T> {

        private final boolean valuePresent;

        @Nullable
        private final T value;

        protected Value(boolean valuePresent, T value) {
            this.valuePresent = valuePresent;
            this.value = value;
        }

        @SuppressFBWarnings(value = "NP_STORE_INTO_NONNULL_FIELD", justification = "SpotBugs Bug")
        protected Value() {
            this.valuePresent = false;
            this.value = null;
        }

        public boolean isValuePresent() {
            return valuePresent;
        }

        @SuppressWarnings("null")
        public T get() {
            if (isValuePresent()) {
                return value;
            } else {
                throw new NoSuchElementException("There is no value in this aspect");
            }
        }

        public T orElseGet(Supplier<T> valueSupplier) {
            if (isValuePresent()) {
                return get();
            } else {
                return valueSupplier.get();
            }
        }

        @Override
        public String toString() {
            return isValuePresent() ? "Value: '" + value + "'" : "no value present";
        }
    }
}

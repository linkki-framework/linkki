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

/**
 * Represents an aspect of a property that is bind by linkki. This class is a container of an
 * aspect's name and value. How the aspect is created is defined by {@link LinkkiAspectDefinition}.
 * <p>
 * A typical example for an aspect is the value of a property. When the value of a property is
 * changed in the model, the content of a UI component that is bind to this property have to be
 * updated accordingly. Analogously, the model has to be updated if the the value is changed by an
 * input in a UI component.
 */
public class Aspect<T> {

    private final String name;
    private final Value<T> value;

    private Aspect(String name, Value<T> value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @CheckForNull
    public T getStaticValue() {
        return value.getStatic();
    }

    @CheckForNull
    public T getStaticValueOr(Supplier<T> supplier) {
        return value.orElseGet(supplier);
    }

    /**
     * Creates a new {@link Aspect} with the same name and the given static value
     * 
     * @param staticValue the new static value
     * @return a new {@link Aspect} containing the given static value
     */
    public Aspect<T> toStatic(T staticValue) {
        return ofStatic(name, staticValue);
    }

    /**
     * Creates a new dynamic {@link Aspect} with the given name.
     * 
     * @param name name of the new aspect
     * @return a new {@link Aspect} that has no static value
     */
    public static <T> Aspect<T> ofDynamic(String name) {
        return new Aspect<>(name, new Value<>(false, null));
    }

    /**
     * Creates a new static {@link Aspect} with the given name and value.
     * 
     * @param name name of the new aspect
     * @param value static value of the aspect
     * @return a new {@link Aspect} that has a fixed static value
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

        public boolean isStaticValuePresent() {
            return isStatic;
        }

        @CheckForNull
        public T getStatic() {
            if (isStaticValuePresent()) {
                return staticValue;
            } else {
                throw new NoSuchElementException("There is no static value");
            }
        }

        @CheckForNull
        public T orElseGet(Supplier<T> valueSupplier) {
            if (isStaticValuePresent()) {
                return getStatic();
            } else {
                return valueSupplier.get();
            }
        }

    }
}

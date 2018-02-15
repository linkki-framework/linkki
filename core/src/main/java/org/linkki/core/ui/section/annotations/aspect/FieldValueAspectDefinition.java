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

package org.linkki.core.ui.section.annotations.aspect;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ClassUtils;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.data.util.AbstractProperty;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;

/**
 * Aspect definition for value change. Defines that the data source will get/set values through
 * {@link Aspect Aspects} by providing handler in
 * {@link #initModelUpdate(PropertyDispatcher, ComponentWrapper, Handler)}.
 */
public class FieldValueAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = "";

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {
        AbstractField<?> field = (AbstractField<?>)componentWrapper.getComponent();
        FieldBindingDataSource<Object> dataSource = new FieldBindingDataSource<Object>(
                propertyDispatcher.getValueClass(),
                () -> propertyDispatcher.getAspectValue(Aspect.newDynamic(NAME)),
                v -> propertyDispatcher.setAspectValue(Aspect.ofStatic(NAME, v)),
                () -> !propertyDispatcher.isWritable(Aspect.newDynamic(NAME)),
                modelUpdated);
        field.setPropertyDataSource(dataSource);

        prepareFieldToHandleNullForRequiredFields(field);
    }

    /**
     * LIN-90, LIN-95: if a field is required and the user enters blank into the field, Vaadin does not
     * transfer {@code null} into the data source. This leads to the effect that if the user enters a
     * valid value, the value is transfered to the model. If the user then enters blank, he sees an
     * empty field but the value in the model is still set to the old value.
     * <p>
     * How do we avoid this? If the field has no converter, we set invalidCommitted to {@code true}.
     * {@code null} is regarded as invalid value, but it is transferable to the model. This does not
     * work for fields with a converter. {@code null} handling is OK for those fields, but if the user
     * enters a value that cannot be converted, Vaadin tries to commit the value to the data source
     * doing so tries to convert it. This leads to an exception (as the value cannot be converted).
     * <p>
     * Example: Enter an invalid number like '123a' into a number field. We can't commit the value as it
     * is invalid and cannot be converted. To get this to work, those fields have to override
     * {@link AbstractField#validate()} to get rid of the unwanted check that leads to a validation
     * exception for {@code null} values in required fields.
     * 
     * @param field
     * 
     * @see AbstractField#validate()
     */
    @SuppressWarnings("rawtypes")
    private void prepareFieldToHandleNullForRequiredFields(AbstractField field) {
        // note: we prepare the field if it is required or not, as the required state
        // can be changed dynamically.
        boolean commitInvalid = true;
        if (field.getConverter() != null && !compatibleTypeConverter(field)) {
            ensureThatFieldsWithAConverterOverrideValidate(field);
            commitInvalid = false;
        }
        field.setInvalidCommitted(commitInvalid);
    }

    private void ensureThatFieldsWithAConverterOverrideValidate(AbstractField<?> field) {
        Method validateMethod;
        try {
            validateMethod = field.getClass().getMethod("validate");
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        if (validateMethod.getDeclaringClass().getName().startsWith("com.vaadin")) {
            throw new IllegalStateException(
                    String.format(
                                  "A field that has a converter must override validate() to disable Vaadin's required field handling! See %s#%s for further explanation",
                                  getClass().getSimpleName(),
                                  "prepareFieldToHandleNullForRequiredFields(AbstractField)"));
        }
    }

    /**
     * Some fields could have converters because they will never throw a conversion exception.
     * <ul>
     * <li>DateField only converts from Date to LocalDate (compatible data type)</li>
     * </ul>
     * 
     * @return
     */
    private boolean compatibleTypeConverter(AbstractField<?> field) {
        return field instanceof DateField;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        AbstractField<?> component = (AbstractField<?>)componentWrapper.getComponent();
        FieldBindingDataSource<?> dataSource = (FieldBindingDataSource<?>)component.getPropertyDataSource();
        return () -> {
            dataSource.fireReadOnlyStatusChange();
            dataSource.fireValueChange();
        };
    }

    @Override
    public void initialize(Annotation annotation) {
        // does nothing
    }

    /**
     * Overrides behavior of {@link AbstractProperty} so it uses the given handler to set/get values in
     * a property.
     */
    private static final class FieldBindingDataSource<T> extends AbstractProperty<T> {

        private static final long serialVersionUID = 1L;

        private final Class<?> valueClass;

        private final Supplier<T> aspectValueGetter;

        private final Consumer<T> aspectValueSetter;

        private final Supplier<Boolean> readOnlySupplier;

        private Handler uiUpdater;

        public FieldBindingDataSource(Class<?> valueClass, Supplier<T> valueGetter, Consumer<T> valueSetter,
                Supplier<Boolean> readOnlySupplier, Handler uiUpdater) {
            this.valueClass = requireNonNull(valueClass, "valueClass must not be null");
            this.aspectValueGetter = requireNonNull(valueGetter, "valueSupplier must not be null");
            this.aspectValueSetter = requireNonNull(valueSetter, "valueConsumer must not be null");
            this.readOnlySupplier = requireNonNull(readOnlySupplier, "readOnlySupplier must not be null");
            this.uiUpdater = requireNonNull(uiUpdater, "uiUpdater must not be null");
        }

        @Override
        @CheckForNull
        public T getValue() {
            return aspectValueGetter.get();
        }

        @Override
        public void setValue(@Nullable T newValue) throws com.vaadin.data.Property.ReadOnlyException {
            aspectValueSetter.accept(newValue);
            uiUpdater.apply();
        }

        @SuppressWarnings({ "unchecked", "null" })
        @Override
        public Class<? extends T> getType() {
            return (Class<? extends T>)ClassUtils.primitiveToWrapper(valueClass);
        }

        /*
         * Override for visibility in ComponentBinding
         */
        @Override
        protected void fireValueChange() {
            super.fireValueChange();
        }

        /*
         * Override for visibility in ComponentBinding
         */
        @Override
        protected void fireReadOnlyStatusChange() {
            super.fireReadOnlyStatusChange();
        }

        @Override
        public boolean isReadOnly() {
            return readOnlySupplier.get();
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            throw new UnsupportedOperationException();
        }
    }

}

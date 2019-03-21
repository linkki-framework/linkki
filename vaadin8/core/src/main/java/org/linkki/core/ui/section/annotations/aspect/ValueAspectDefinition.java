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

package org.linkki.core.ui.section.annotations.aspect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.core.vaadin8.nls.NlsText;
import org.linkki.util.handler.Handler;

import com.vaadin.data.Converter;
import com.vaadin.data.HasValue;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.HasValueChangeMode;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Aspect definition for value change. Defines that the data source will get/set values through
 * {@link Aspect Aspects} by providing a handler in
 * {@link #initModelUpdate(PropertyDispatcher, ComponentWrapper, Handler)}.
 * <p>
 * This aspect definition does not use any converter. Extend this class and override
 * {@link #getConverter(Type, Type)} to add converters to the field.
 */
public class ValueAspectDefinition implements LinkkiAspectDefinition {

    public static final String MSG_CODE_INVALID_INPUT = "valueAspectDefinition.invalidInput";

    public static final String NAME = LinkkiAspectDefinition.VALUE_ASPECT_NAME;

    @CheckForNull
    private final Converter<?, ?> fixConverter;

    public ValueAspectDefinition() {
        fixConverter = null;
    }

    public ValueAspectDefinition(Converter<?, ?> converter) {
        this.fixConverter = converter;
    }

    @Override
    public void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {

        HasValue<?> field = (HasValue<?>)componentWrapper.getComponent();

        if (field instanceof HasValueChangeMode) {
            ((HasValueChangeMode)field).setValueChangeMode(ValueChangeMode.BLUR);
        }

        Converter<Object, Object> converter = getConverter(propertyDispatcher, field);

        field.addValueChangeListener(event -> {
            if (event.isUserOriginated()) {
                Result<?> result = converter.convertToModel(event.getValue(), getValueContext());
                result.ifOk(v -> propertyDispatcher.push(Aspect.of(NAME, v)));
                modelUpdated.apply();
                result.ifError(s -> componentWrapper.setValidationMessages(getInvalidInputMessage(event.getValue())));
            }
        });
    }

    private MessageList getInvalidInputMessage(@CheckForNull Object value) {
        return new MessageList(
                Message.newWarning(MSG_CODE_INVALID_INPUT,
                                   String.format(NlsText.getString("ValueAspectDefinition.invalidInput"),
                                                 value != null ? value.toString() : "null")));
    }

    private Type getTypeOf(HasValue<?> field) {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(field.getClass(),
                                                                              HasValue.class);
        @SuppressWarnings("rawtypes")
        TypeVariable<Class<HasValue>>[] typeVariables = HasValue.class.getTypeParameters();
        return typeArguments.get(typeVariables[0]);
    }

    @SuppressWarnings("unchecked")
    private Converter<Object, Object> getConverter(PropertyDispatcher propertyDispatcher, HasValue<?> field) {
        return (Converter<Object, Object>)getConverter(getTypeOf(field),
                                                       propertyDispatcher.getValueClass());
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        @SuppressWarnings("unchecked")
        HasValue<Object> field = (HasValue<Object>)componentWrapper.getComponent();
        Converter<Object, Object> converter = getConverter(propertyDispatcher, field);
        return () -> {
            Object value = propertyDispatcher.pull(Aspect.of(NAME));
            if (value != null) {
                field.setValue(converter.convertToPresentation(value, getValueContext()));
            } else {
                field.setValue(field.getEmptyValue());
            }
        };
    }

    protected ValueContext getValueContext() {
        return new ValueContext(UiFramework.getLocale());
    }

    /**
     * Returns a converter that should be used to convert a value of model type to a value of
     * presentation type. Note that the presentation type should match the value type of the input
     * field.
     * <P>
     * Uses {@link Converter#identity()} by default. Override to add converters.
     * 
     * @param presentationType value type of the input field
     * @param modelType the model type
     * 
     * @return a converter from model type to presentation type
     * 
     * @see LinkkiConverterRegistry
     */
    protected Converter<?, ?> getConverter(Type presentationType, Type modelType) {
        if (fixConverter != null) {
            return fixConverter;
        } else {
            Converter<Object, Object> foundConverter = LinkkiConverterRegistry.getCurrent()
                    .findConverter(presentationType,
                                   modelType);
            return Optional.ofNullable(foundConverter).orElse(Converter.identity());
        }
    }

}

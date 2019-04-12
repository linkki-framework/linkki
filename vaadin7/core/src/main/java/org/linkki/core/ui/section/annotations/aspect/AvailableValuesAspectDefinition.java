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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.container.LinkkiInMemoryContainer;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.AvailableValuesProvider;
import org.linkki.util.handler.Handler;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;

/**
 * Defines aspects that update the set of available value of an {@link AbstractSelect}.
 */
public class AvailableValuesAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = "availableValues";

    private final AvailableValuesType availableValuesType;

    public AvailableValuesAspectDefinition(AvailableValuesType availableValuesType) {
        this.availableValuesType = availableValuesType;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Consumer<Collection<?>> setter = createComponentValueSetter(componentWrapper);
        Aspect<List<?>> aspect = createAspect(propertyDispatcher.getProperty(),
                                              propertyDispatcher.getValueClass());
        return () -> setter.accept(propertyDispatcher.pull(aspect));
    }

    public Aspect<List<?>> createAspect(String propertyName, Class<?> valueClass) {
        if (availableValuesType == AvailableValuesType.DYNAMIC) {
            return Aspect.of(NAME);
        } else if (availableValuesType == AvailableValuesType.NO_VALUES) {
            return Aspect.of(NAME, new ArrayList<>());
        } else {
            return Aspect.of(NAME, getValuesDerivedFromDatatype(propertyName, valueClass));
        }
    }

    protected <T extends Enum<T>> List<?> getValuesDerivedFromDatatype(String propertyName, Class<?> valueClass) {
        if (valueClass.isEnum()) {
            @SuppressWarnings("unchecked")
            Class<T> enumType = (Class<T>)valueClass;
            return AvailableValuesProvider.enumToValues(enumType,
                                                        availableValuesType == AvailableValuesType.ENUM_VALUES_INCL_NULL);
        }
        if (valueClass == Boolean.TYPE) {
            return AvailableValuesProvider.booleanPrimitiveToValues();
        }
        if (valueClass == Boolean.class) {
            return AvailableValuesProvider.booleanWrapperToValues();
        } else {
            throw new IllegalStateException(
                    "Cannot retrieve list of available values for " + valueClass.getName() + "#" + propertyName);
        }
    }

    public Consumer<Collection<?>> createComponentValueSetter(ComponentWrapper componentWrapper) {
        AbstractSelect component = ((AbstractSelect)componentWrapper.getComponent());
        LinkkiInMemoryContainer<Object> container = new LinkkiInMemoryContainer<>();
        setContainerDataSource(component, container);
        return vals -> {
            if (vals != null) {
                @SuppressWarnings("unchecked")
                Collection<Object> newItems = (Collection<Object>)vals;
                container.setItems(newItems);
            } else {
                container.setItems(Collections.emptyList());
            }
        };
    }

    /**
     * This method updates the {@link AbstractSelect#setContainerDataSource(com.vaadin.data.Container)
     * container data source} of the given component.
     * <p>
     * If a property data source is already specified this property data source may specify that the
     * component is in read only state. Setting a container data source while the component is read-only
     * would lead into an exception. To avoid this, we first set the property data source to
     * <code>null</code> and re-set it after the container is set.
     * 
     * @param container the container data source
     */
    private void setContainerDataSource(AbstractSelect component, LinkkiInMemoryContainer<Object> container) {
        Property<?> dataSource = component.getPropertyDataSource();
        boolean readOnly = component.isReadOnly();
        component.setReadOnly(false);
        component.setPropertyDataSource(null);
        component.setContainerDataSource(container);
        component.setPropertyDataSource(dataSource);
        component.setReadOnly(readOnly);
    }

    /**
     * {@link org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator} that creates a special kind of
     * {@link AvailableValuesAspectDefinition} where the configured {@link AvailableValuesType} is
     * always {@link AvailableValuesType#ENUM_VALUES_INCL_NULL}.
     */
    public static class EnumValuesInclNullCreator implements AspectDefinitionCreator<Annotation> {

        @Override
        public LinkkiAspectDefinition create(Annotation annotation) {
            return new AvailableValuesAspectDefinition(AvailableValuesType.ENUM_VALUES_INCL_NULL);
        }

    }

    /**
     * {@link org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator} that creates a special kind of
     * {@link AvailableValuesAspectDefinition} where the configured {@link AvailableValuesType} is
     * always {@link AvailableValuesType#DYNAMIC}.
     */
    public static class DynamicCreator implements AspectDefinitionCreator<Annotation> {

        @Override
        public LinkkiAspectDefinition create(Annotation annotation) {
            return new AvailableValuesAspectDefinition(AvailableValuesType.DYNAMIC);
        }

    }

}

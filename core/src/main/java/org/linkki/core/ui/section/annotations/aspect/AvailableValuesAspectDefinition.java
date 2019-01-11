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

package org.linkki.core.ui.section.annotations.aspect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyNamingConvention;
import org.linkki.core.container.LinkkiInMemoryContainer;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.util.handler.Handler;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;

/**
 * Defines aspects that update the set of available value of an {@link AbstractSelect}.
 */
public abstract class AvailableValuesAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = PropertyNamingConvention.AVAILABLE_VALUES_PROPERTY_SUFFIX;

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Consumer<@Nullable Collection<?>> setter = createComponentValueSetter(componentWrapper);
        Aspect<List<?>> aspect = createAspect(propertyDispatcher.getProperty(),
                                              propertyDispatcher.getValueClass());
        return () -> setter.accept(propertyDispatcher.pull(aspect));
    }

    public Aspect<List<?>> createAspect(String propertyName, Class<?> valueClass) {
        AvailableValuesType type = getAvailableValuesType();
        if (type == AvailableValuesType.DYNAMIC) {
            return Aspect.of(NAME);
        } else if (type == AvailableValuesType.NO_VALUES) {
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
                                                        getAvailableValuesType() == AvailableValuesType.ENUM_VALUES_INCL_NULL);
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

    public Consumer<@Nullable Collection<?>> createComponentValueSetter(ComponentWrapper componentWrapper) {
        AbstractSelect component = ((AbstractSelect)componentWrapper.getComponent());
        LinkkiInMemoryContainer<Object> container = new LinkkiInMemoryContainer<Object>();
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
     * The {@link AvailableValuesType} that is defined in the annotation.
     * 
     * @return value for {@link AvailableValuesType}
     */
    protected abstract AvailableValuesType getAvailableValuesType();
}

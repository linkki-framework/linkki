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

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.util.handler.Handler;

import com.vaadin.data.HasItems;
import com.vaadin.data.provider.ListDataProvider;

/**
 * Defines aspects that update the set of available value of an {@link HasItems}.
 */
public class AvailableValuesAspectDefinition<C extends HasItems<?>> implements LinkkiAspectDefinition {

    public static final String NAME = "availableValues";

    private final AvailableValuesType availableValuesType;

    private final BiConsumer<C, ListDataProvider<Object>> dataProviderSetter;

    public AvailableValuesAspectDefinition(AvailableValuesType availableValuesType,
            BiConsumer<C, ListDataProvider<Object>> dataProviderSetter) {
        this.availableValuesType = requireNonNull(availableValuesType, "availableValuesType must not be null");
        this.dataProviderSetter = requireNonNull(dataProviderSetter, "dataProviderSetter must not be null");
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Aspect<List<?>> aspect = createAspect(propertyDispatcher.getProperty(),
                                              propertyDispatcher.getValueClass());

        List<Object> items = new ArrayList<>();
        ListDataProvider<Object> listDataProvider = new ListDataProvider<>(items);

        setDataProvider(componentWrapper, listDataProvider);
        return () -> {
            items.clear();
            items.addAll(propertyDispatcher.pull(aspect));
            handleNullItems(componentWrapper, items);
            listDataProvider.refreshAll();
        };

    }

    /**
     * Returns an {@link Aspect} with name {@link AvailableValuesAspectDefinition#NAME}. The value of
     * this {@link Aspect} is created in dependence on the current {@link AvailableValuesType}.
     * 
     * @param propertyName is used to classify the valueClass in case of an
     *            {@link IllegalArgumentException}
     * @param valueClass is considered if the available values type is neither
     *            {@link AvailableValuesType#DYNAMIC} nor {@link AvailableValuesType#NO_VALUES} to
     *            derive the {@link Aspect}'s values from valueClasses data type
     * @return the {@link Aspect} with name {@link AvailableValuesAspectDefinition#NAME}
     */
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

    @SuppressWarnings("unchecked")
    protected void setDataProvider(ComponentWrapper componentWrapper,
            ListDataProvider<Object> dataProvider) {
        dataProviderSetter.accept((C)componentWrapper.getComponent(), dataProvider);
    }

    /**
     * The {@link AvailableValuesType} that is defined in the annotation.
     * 
     * @return value for {@link AvailableValuesType}
     */
    protected AvailableValuesType getAvailableValuesType() {
        return availableValuesType;
    }

    /**
     * Handles <code>null</code> values in the list of available values that is about to be set to the
     * component. By default, the items list is directly passed to the {@link ComponentWrapper} without
     * further processing.
     * <p>
     * Note that you have to modify the given list of values directly.
     * 
     * @param componentWrapper component of which available values should be updated
     * @param items items to be shown in the {@link ComponentWrapper}. May contain <code>null</code>.
     */
    protected void handleNullItems(ComponentWrapper componentWrapper, List<Object> items) {
        // does nothing by default
    }
}

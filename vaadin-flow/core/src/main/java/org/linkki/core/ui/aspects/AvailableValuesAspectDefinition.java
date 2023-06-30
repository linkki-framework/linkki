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

package org.linkki.core.ui.aspects;

import static org.linkki.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import com.vaadin.flow.data.provider.HasListDataView;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.AvailableValuesProvider;
import org.linkki.util.handler.Handler;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Defines an aspect that updates the set of available values of {@link HasListDataView}.
 *
 * @param <C> the component type
 */
public class AvailableValuesAspectDefinition<C> implements LinkkiAspectDefinition {

    public static final String NAME = "availableValues";

    private final AvailableValuesType availableValuesType;

    private final BiConsumer<C, List<Object>> dataProviderSetter;

    public AvailableValuesAspectDefinition(AvailableValuesType availableValuesType,
            BiConsumer<C, List<Object>> dataProviderSetter) {
        this.availableValuesType = requireNonNull(availableValuesType, "availableValuesType must not be null");
        this.dataProviderSetter = requireNonNull(dataProviderSetter, "dataProviderSetter must not be null");
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {

        // Initialize with an empty collection to be in sync with current cache.
        setDataProvider(componentWrapper, Collections.emptyList());

        if (getAvailableValuesType() == AvailableValuesType.NO_VALUES) {
            return Handler.NOP_HANDLER;
        }

        Aspect<Collection<?>> aspect = createAspect(propertyDispatcher.getValueClass());

        ItemCache cache = new ItemCache();
        return () -> updateItems(propertyDispatcher.getProperty(), cache, propertyDispatcher.pull(aspect),
                                 componentWrapper);
    }

    private void updateItems(String propertyName,
            ItemCache cache,
            @Nullable Collection<?> newItemsParam,
            ComponentWrapper componentWrapper) {
        if (newItemsParam == null) {
            if (availableValuesType != AvailableValuesType.DYNAMIC) {
                throw new IllegalStateException(
                        "Cannot retrieve list of available values for property " + propertyName);
            } else {
                // retains current value
                return;
            }
        }

        ArrayList<Object> newItems = new ArrayList<>(requireNonNull(newItemsParam, "List of available values must not be null"));

        handleNullItems(componentWrapper, newItems);
        boolean hasChanged = cache.replaceContent(newItems);
        if (hasChanged) {
            setDataProvider(componentWrapper, cache.getItems());
        }
    }

    /**
     * Returns an {@link Aspect} with name {@link AvailableValuesAspectDefinition#NAME}. The value of
     * this {@link Aspect} is created in dependence on the current {@link AvailableValuesType}.
     *
     * @param valueClass is considered if the available values type is neither
     *            {@link AvailableValuesType#DYNAMIC} nor {@link AvailableValuesType#NO_VALUES} to
     *            derive the {@link Aspect}'s values from valueClasses data type
     * @return the {@link Aspect} with name {@link AvailableValuesAspectDefinition#NAME}
     */
    public Aspect<Collection<?>> createAspect(Class<?> valueClass) {
        AvailableValuesType type = getAvailableValuesType();
        if (type == AvailableValuesType.DYNAMIC) {
            return Aspect.of(NAME);
        } else if (type == AvailableValuesType.NO_VALUES) {
            return Aspect.of(NAME, new ArrayList<>());
        } else {
            return Aspect.of(NAME, getValuesDerivedFromDatatype(valueClass));
        }
    }

    @CheckForNull
    protected <T extends Enum<T>> List<?> getValuesDerivedFromDatatype(Class<?> valueClass) {
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
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected void setDataProvider(ComponentWrapper componentWrapper,
            List<Object> data) {
        dataProviderSetter.accept((C)componentWrapper.getComponent(), data);
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
    protected void handleNullItems(ComponentWrapper componentWrapper, List<?> items) {
        // does nothing by default
    }
}

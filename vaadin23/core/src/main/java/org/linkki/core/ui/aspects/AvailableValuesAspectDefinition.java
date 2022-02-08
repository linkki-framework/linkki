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
import java.util.List;
import java.util.function.BiConsumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.AvailableValuesProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.DefaultCaptionProvider;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.provider.ListDataProvider;

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

    private final ItemCaptionProvider<?> itemCaptionProvider;

    public AvailableValuesAspectDefinition(AvailableValuesType availableValuesType,
            BiConsumer<C, List<Object>> dataProviderSetter) {
        this(availableValuesType, dataProviderSetter, new DefaultCaptionProvider());
    }

    public AvailableValuesAspectDefinition(AvailableValuesType availableValuesType,
            BiConsumer<C, List<Object>> dataProviderSetter,
            ItemCaptionProvider<?> itemCaptionProvider) {
        this.availableValuesType = requireNonNull(availableValuesType, "availableValuesType must not be null");
        this.dataProviderSetter = requireNonNull(dataProviderSetter, "dataProviderSetter must not be null");
        this.itemCaptionProvider = requireNonNull(itemCaptionProvider, "itemCaptionProvider must not be null");
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Aspect<Collection<?>> aspect = createAspect(propertyDispatcher.getProperty(),
                                                    propertyDispatcher.getValueClass());

        ItemCache cache = new ItemCache(itemCaptionProvider);
        return () -> updateItems(cache, propertyDispatcher.pull(aspect), componentWrapper);
    }

    private void updateItems(ItemCache cache,
            @Nullable Collection<?> newItemsParam,
            ComponentWrapper componentWrapper) {
        ArrayList<Object> newItems = new ArrayList<>(
                requireNonNull(newItemsParam, "List of available values must not be null"));
        handleNullItems(componentWrapper, newItems);

        boolean hasChanged = cache.replaceContent(newItems);
        if (hasChanged) {
            refreshAll(componentWrapper, cache.getItems());
        }
    }

    /**
     * Refreshes all items including all the captions of available and selected items.
     *
     * @implNote This implementation always set a new {@link ListDataProvider} to get potential updated
     *           captions. Resetting the {@link ListDataProvider} performs better than updating the
     *           existing list provider using {@link ListDataProvider#refreshItem(Object)} for every
     *           item. Unlike the name suggests, the method {@link ListDataProvider#refreshAll()} does
     *           not update the captions at all.
     */
    protected void refreshAll(ComponentWrapper componentWrapper, List<Object> items) {
        setDataProvider(componentWrapper, items);
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
    public Aspect<Collection<?>> createAspect(String propertyName, Class<?> valueClass) {
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

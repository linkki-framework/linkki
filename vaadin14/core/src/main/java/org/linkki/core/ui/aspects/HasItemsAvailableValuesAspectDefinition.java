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

import java.util.List;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.binder.HasFilterableDataProvider;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializablePredicate;

/**
 * An {@link AvailableValuesAspectDefinition} for {@link Component components} that implement
 * {@link HasItems}.
 */
public class HasItemsAvailableValuesAspectDefinition extends AvailableValuesAspectDefinition<HasItems<?>> {

    public HasItemsAvailableValuesAspectDefinition(AvailableValuesType availableValuesType) {
        super(availableValuesType, HasItemsAvailableValuesAspectDefinition::setDataProvider);
    }

    @SuppressWarnings("unchecked")
    private static void setDataProvider(HasItems<?> component, ListDataProvider<Object> listDataProvider) {
        if (component instanceof HasDataProvider) {
            ((HasDataProvider<Object>)component).setDataProvider(listDataProvider);
        } else if (component instanceof ComboBox) {
            ((ComboBox<Object>)component).setDataProvider(listDataProvider);
        } else if (component instanceof HasFilterableDataProvider) {
            ((HasFilterableDataProvider<Object, SerializablePredicate<Object>>)component)
                    .setDataProvider(listDataProvider);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void handleNullItems(ComponentWrapper componentWrapper, List<?> items) {
        Object component = componentWrapper.getComponent();
        if (component instanceof ComboBox<?>) {
            boolean hasNullItem = items.removeIf(i -> i == null);
            ((ComboBox<Object>)component).setAllowCustomValue(hasNullItem);
        } else if (component instanceof Select<?>) {
            boolean hasNullItem = items.removeIf(i -> i == null);
            ((Select<Object>)component).setEmptySelectionAllowed(hasNullItem);
        }
    }
}

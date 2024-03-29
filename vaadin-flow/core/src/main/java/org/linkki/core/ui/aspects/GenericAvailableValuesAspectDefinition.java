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

package org.linkki.core.ui.aspects;

import java.util.List;
import java.util.Objects;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.HasListDataView;

/**
 * An {@link AvailableValuesAspectDefinition} for {@link Component components} that implement
 * {@link HasListDataView}.
 */
public class GenericAvailableValuesAspectDefinition
        extends AvailableValuesAspectDefinition<HasListDataView<Object, ?>> {

    public GenericAvailableValuesAspectDefinition(AvailableValuesType availableValuesType) {
        super(availableValuesType, HasListDataView::setItems);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void handleNullItems(ComponentWrapper componentWrapper, List<?> items) {
        Object component = componentWrapper.getComponent();
        if (component instanceof ComboBox<?>) {
            // cannot be put before if because null gets removed from method callers
            boolean hasNullItem = items.removeIf(Objects::isNull);
            ((ComboBox<Object>)component).setAllowCustomValue(hasNullItem);
        } else if (component instanceof Select<?>) {
            // cannot be put before if because null gets removed from method callers
            boolean hasNullItem = items.removeIf(Objects::isNull);
            ((Select<Object>)component).setEmptySelectionAllowed(hasNullItem);
        }
    }
}

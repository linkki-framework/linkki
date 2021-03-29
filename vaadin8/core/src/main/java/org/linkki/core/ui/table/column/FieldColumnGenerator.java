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

package org.linkki.core.ui.table.column;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.ElementDescriptor;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;

import edu.umd.cs.findbugs.annotations.Nullable;

/** Column generator that generates a column for a field of a PMO. */
@SuppressWarnings("deprecation")
class FieldColumnGenerator implements com.vaadin.v7.ui.Table.ColumnGenerator {

    private static final long serialVersionUID = 1L;

    private final PropertyElementDescriptors elementDescriptors;
    private final BindingContext bindingContext;

    @Nullable
    private Binding binding;

    public FieldColumnGenerator(PropertyElementDescriptors elementDescriptors,
            BindingContext bindingContext) {
        this.elementDescriptors = requireNonNull(elementDescriptors, "elementDescriptors must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
    }

    @Override
    public Object generateCell(com.vaadin.v7.ui.Table source,
            Object itemId,
            Object columnId) {
        requireNonNull(itemId, "itemId must not be null");
        ElementDescriptor elementDescriptor = elementDescriptors.getDescriptor(itemId);
        Component component = (Component)elementDescriptor.newComponent(itemId);
        component.addStyleName(LinkkiTheme.BORDERLESS);
        component.addStyleName(LinkkiTheme.TABLE_CELL);
        if (component instanceof HasValue) {
            component.setWidthFull();
        }

        bindingContext.bind(itemId, elementDescriptor, new LabelComponentWrapper(component));

        return component;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

}

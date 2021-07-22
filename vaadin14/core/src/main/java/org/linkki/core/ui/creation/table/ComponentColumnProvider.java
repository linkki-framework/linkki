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

package org.linkki.core.ui.creation.table;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Method;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.function.ValueProvider;

/** Column generator that generates a column for a field of a PMO. */
class ComponentColumnProvider<ROW> implements ValueProvider<ROW, Component> {

    private static final long serialVersionUID = 1L;

    private final Method method;
    private final BindingContext bindingContext;

    public ComponentColumnProvider(Method method,
            BindingContext bindingContext) {
        this.method = requireNonNull(method, "method must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
    }

    @Override
    public Component apply(ROW source) {
        NoLabelComponentWrapper wrapper = UiCreator
                .createUiElement(method, source, bindingContext,
                                 c -> new NoLabelComponentWrapper((Component)c, WrapperType.FIELD));
        Component component = wrapper.getComponent();

        if (component instanceof HasValue && component instanceof HasSize) {
            ((HasSize)component).setWidthFull();
        }

        return component;
    }
}

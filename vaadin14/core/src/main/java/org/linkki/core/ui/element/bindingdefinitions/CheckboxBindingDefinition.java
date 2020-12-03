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
package org.linkki.core.ui.element.bindingdefinitions;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;

/**
 * {@link BindingDefinition} for {@link UICheckBox}.
 */
public class CheckboxBindingDefinition implements BindingDefinition {

    private final UICheckBox uiCheckbox;

    public CheckboxBindingDefinition(UICheckBox uiCheckbox) {
        this.uiCheckbox = requireNonNull(uiCheckbox, "uiCheckbox must not be null");
    }

    @Override
    public Component newComponent() {
        Checkbox newCheckbox = ComponentFactory.newCheckbox();
        return newCheckbox;
    }

    @Override
    public String label() {
        return uiCheckbox.label();
    }

    @Override
    public EnabledType enabled() {
        return uiCheckbox.enabled();
    }

    @Override
    public RequiredType required() {
        return uiCheckbox.required();
    }

    @Override
    public VisibleType visible() {
        return uiCheckbox.visible();
    }

    @Override
    public String modelObject() {
        return uiCheckbox.modelObject();
    }

    @Override
    public String modelAttribute() {
        return uiCheckbox.modelAttribute();
    }

}
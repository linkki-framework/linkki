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
package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.defaults.uielement.aspects.types.EnabledType;
import org.linkki.core.defaults.uielement.aspects.types.RequiredType;
import org.linkki.core.defaults.uielement.aspects.types.VisibleType;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

/**
 * {@link BindingDefinition} for {@link UITextField}.
 */
public class TextFieldBindingDefinition implements BindingDefinition {

    private final UITextField uiTextField;

    public TextFieldBindingDefinition(UITextField uiTextField) {
        this.uiTextField = requireNonNull(uiTextField, "uiTextField must not be null");
    }

    @Override
    public Component newComponent() {
        TextField field = ComponentFactory.newTextfield();
        if (uiTextField.columns() > 0) {
            field.setColumns(uiTextField.columns());
        } else {
            field.setWidth("100%");
        }
        if (uiTextField.maxLength() > 0) {
            field.setMaxLength(uiTextField.maxLength());
        }
        return field;
    }

    @Override
    public int position() {
        return uiTextField.position();
    }

    @Override
    public String label() {
        return uiTextField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiTextField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiTextField.required();
    }

    @Override
    public VisibleType visible() {
        return uiTextField.visible();
    }

    @Override
    public String modelAttribute() {
        return uiTextField.modelAttribute();
    }

    @Override
    public String modelObject() {
        return uiTextField.modelObject();
    }
}
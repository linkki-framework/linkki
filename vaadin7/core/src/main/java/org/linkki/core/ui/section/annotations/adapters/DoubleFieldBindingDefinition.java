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
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.components.DoubleField;
import org.linkki.core.ui.section.annotations.UIDoubleField;
import org.linkki.core.uiframework.UiFramework;

import com.vaadin.ui.Component;

/**
 * {@link BindingDefinition} for {@link UIDoubleField}.
 */
public class DoubleFieldBindingDefinition implements BindingDefinition {

    private final UIDoubleField uiDoubleField;

    public DoubleFieldBindingDefinition(UIDoubleField uiDoubleField) {
        this.uiDoubleField = requireNonNull(uiDoubleField, "uiDoubleField must not be null");
    }

    @Override
    public Component newComponent() {
        DoubleField field = new DoubleField(uiDoubleField.format(), UiFramework.getLocale());
        if (uiDoubleField.maxLength() > 0) {
            field.setMaxLength(uiDoubleField.maxLength());
            field.setColumns(uiDoubleField.maxLength() + 2);
        }

        return field;
    }

    @Override
    public String label() {
        return uiDoubleField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiDoubleField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiDoubleField.required();
    }

    @Override
    public VisibleType visible() {
        return uiDoubleField.visible();
    }

    @Override
    public String modelAttribute() {
        return uiDoubleField.modelAttribute();
    }

    @Override
    public String modelObject() {
        return uiDoubleField.modelObject();
    }
}
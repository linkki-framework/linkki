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

import org.linkki.core.ui.UiFramework;
import org.linkki.core.ui.components.IntegerField;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIIntegerField;
import org.linkki.core.ui.section.annotations.VisibleType;

import com.vaadin.ui.Component;

/**
 * {@link BindingDefinition} for {@link UIIntegerField}.
 */
public class IntegerFieldBindingDefinition implements BindingDefinition {

    private final UIIntegerField uiIntegerField;

    public IntegerFieldBindingDefinition(UIIntegerField uiIntegerField) {
        this.uiIntegerField = requireNonNull(uiIntegerField, "uiIntegerField must not be null");
    }

    @Override
    public Component newComponent() {
        IntegerField field = new IntegerField(uiIntegerField.format(), UiFramework.getLocale());
        if (uiIntegerField.maxLength() > 0) {
            field.setMaxLength(uiIntegerField.maxLength());
            field.setColumns(uiIntegerField.maxLength() + 2);
        }
        return field;
    }

    @Override
    public int position() {
        return uiIntegerField.position();
    }

    @Override
    public String label() {
        return uiIntegerField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiIntegerField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiIntegerField.required();
    }

    @Override
    public VisibleType visible() {
        return uiIntegerField.visible();
    }

    @Override
    public String modelAttribute() {
        return uiIntegerField.modelAttribute();
    }

    @Override
    public String modelObject() {
        return uiIntegerField.modelObject();
    }
}
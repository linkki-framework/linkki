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
package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UICheckBox;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;

public class CheckboxBindingDefinition implements BindingDefinition {

    private final UICheckBox uiCheckBox;

    public CheckboxBindingDefinition(UICheckBox uiCheckBox) {
        this.uiCheckBox = requireNonNull(uiCheckBox, "uiCheckBox must not be null");
    }

    @Override
    public Component newComponent() {
        CheckBox newCheckBox = ComponentFactory.newCheckBox();
        return newCheckBox;
    }

    @Override
    public int position() {
        return uiCheckBox.position();
    }

    @Override
    public String label() {
        return uiCheckBox.label();
    }

    @Override
    public EnabledType enabled() {
        return uiCheckBox.enabled();
    }

    @Override
    public RequiredType required() {
        return uiCheckBox.required();
    }

    @Override
    public VisibleType visible() {
        return uiCheckBox.visible();
    }

    @Override
    public String modelObject() {
        return uiCheckBox.modelObject();
    }

    @Override
    public String modelAttribute() {
        return uiCheckBox.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiCheckBox.noLabel();
    }
}
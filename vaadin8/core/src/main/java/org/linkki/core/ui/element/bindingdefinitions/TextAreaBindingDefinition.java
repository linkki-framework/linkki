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
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.linkki.core.ui.component.ComponentFactory;
import org.linkki.core.ui.element.annotation.UITextArea;

import com.vaadin.ui.Component;

/**
 * {@link BindingDefinition} for {@link UITextArea}.
 */
public class TextAreaBindingDefinition implements BindingDefinition {

    private final UITextArea uiTextArea;

    public TextAreaBindingDefinition(UITextArea uiTextArea) {
        this.uiTextArea = requireNonNull(uiTextArea, "uiTextArea must not be null");
    }

    @Override
    public Component newComponent() {
        return ComponentFactory.newTextArea(uiTextArea.maxLength(), uiTextArea.width(), uiTextArea.rows());
    }

    @Override
    public int position() {
        return uiTextArea.position();
    }

    @Override
    public String label() {
        return uiTextArea.label();
    }

    @Override
    public EnabledType enabled() {
        return uiTextArea.enabled();
    }

    @Override
    public RequiredType required() {
        return uiTextArea.required();
    }

    @Override
    public VisibleType visible() {
        return uiTextArea.visible();
    }

    @Override
    public String modelAttribute() {
        return uiTextArea.modelAttribute();
    }

    @Override
    public String modelObject() {
        return uiTextArea.modelObject();
    }
}
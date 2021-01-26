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

import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.ui.Component;

/**
 * {@link org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition} for
 * {@link UITextArea}.
 * 
 * @deprecated since 1.4.0 because this concept was replaced and this implementation was moved into
 *             {@link UITextArea}.<br>
 *             See "Custom UI element annotation" at
 *             <a href="https://doc.linkki-framework.org/">https://doc.linkki-framework.org/</a> for
 *             more information.
 */
@Deprecated
public class TextAreaBindingDefinition
        implements org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition {

    private final UITextArea uiTextArea;

    public TextAreaBindingDefinition(UITextArea uiTextArea) {
        this.uiTextArea = requireNonNull(uiTextArea, "uiTextArea must not be null");
    }

    @Override
    public Component newComponent() {
        return ComponentFactory.newTextArea(uiTextArea.maxLength(), uiTextArea.width(), uiTextArea.rows());
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
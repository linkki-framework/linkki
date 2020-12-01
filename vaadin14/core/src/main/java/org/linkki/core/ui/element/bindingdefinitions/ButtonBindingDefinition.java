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

import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.ui.Button;

/**
 * The adapter to provide access to an {@link UIButton} annotation through the definition interface.
 */
public class ButtonBindingDefinition implements BindingDefinition {

    private final UIButton buttonAnnotation;

    public ButtonBindingDefinition(UIButton annotation) {
        this.buttonAnnotation = annotation;
    }

    @Override
    public Button newComponent() {
        Button button = ComponentFactory.newButton();
        if (buttonAnnotation.showIcon()) {
            button.setIcon(buttonAnnotation.icon());
        }
        for (String styleName : buttonAnnotation.styleNames()) {
            button.addStyleName(styleName);
        }
        if (buttonAnnotation.shortcutKeyCode() != -1) {
            button.setClickShortcut(buttonAnnotation.shortcutKeyCode(), buttonAnnotation.shortcutModifierKeys());
        }
        return button;
    }

    @Override
    public String label() {
        return buttonAnnotation.label();
    }

    @Override
    public EnabledType enabled() {
        return buttonAnnotation.enabled();
    }

    @Override
    public VisibleType visible() {
        return buttonAnnotation.visible();
    }

    @Override
    public RequiredType required() {
        return RequiredType.NOT_REQUIRED;
    }

    @Override
    public String modelObject() {
        return ModelObject.DEFAULT_NAME;
    }

    @Override
    public String modelAttribute() {
        return "";
    }
}

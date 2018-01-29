/*
 * Copyright Faktor Zehn AG.
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

import org.linkki.core.exception.LinkkiRuntimeException;
import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;

public class ComboboxBindingDefinition implements UIFieldDefinition {

    private final UIComboBox uiComboField;

    public ComboboxBindingDefinition(UIComboBox uiComboField) {
        this.uiComboField = requireNonNull(uiComboField, "uiComboField must not be null");
    }

    @Override
    public Component newComponent() {
        LinkkiComboBox comboBox = ComponentFactory.newComboBox();
        comboBox.setItemCaptionProvider(getItemCaptionProvider());
        comboBox.setNullSelectionAllowed(false);
        comboBox.setWidth(uiComboField.width());
        return comboBox;
    }

    @Override
    public int position() {
        return uiComboField.position();
    }

    @Override
    public String label() {
        return uiComboField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiComboField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiComboField.required();
    }

    @Override
    public VisibleType visible() {
        return uiComboField.visible();
    }

    @Override
    public String modelAttribute() {
        return uiComboField.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiComboField.noLabel();
    }

    private ItemCaptionProvider<?> getItemCaptionProvider() {
        try {
            return uiComboField.itemCaptionProvider().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiRuntimeException(e);
        }
    }

    @Override
    public String modelObject() {
        return uiComboField.modelObject();
    }

}
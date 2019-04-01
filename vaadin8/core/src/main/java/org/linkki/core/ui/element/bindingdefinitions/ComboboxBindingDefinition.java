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

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.linkki.core.ui.component.ComponentFactory;
import org.linkki.core.ui.element.annotation.UIComboBox;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

/**
 * {@link BindingDefinition} for {@link UIComboBox}.
 */
public class ComboboxBindingDefinition implements BindingDefinition {

    private final UIComboBox uiComboField;

    public ComboboxBindingDefinition(UIComboBox uiComboField) {
        this.uiComboField = requireNonNull(uiComboField, "uiComboField must not be null");
    }

    @Override
    public Component newComponent() {
        ComboBox<?> comboBox = ComponentFactory.newComboBox();
        comboBox.setItemCaptionGenerator(getItemCaptionProvider()::getUnsafeCaption);
        comboBox.setEmptySelectionCaption(getItemCaptionProvider().getNullCaption());
        comboBox.setWidth(uiComboField.width());
        comboBox.setPopupWidth(null);
        comboBox.setPageLength(0);
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

    private ItemCaptionProvider<?> getItemCaptionProvider() {
        try {
            return uiComboField.itemCaptionProvider().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiBindingException(
                    "Cannot instantiate item caption provider " + uiComboField.itemCaptionProvider().getName()
                            + " using default constructor.",
                    e);
        }
    }

    @Override
    public String modelObject() {
        return uiComboField.modelObject();
    }

}
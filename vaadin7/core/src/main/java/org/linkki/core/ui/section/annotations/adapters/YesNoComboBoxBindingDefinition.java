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

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.defaults.uielement.ItemCaptionProvider;
import org.linkki.core.defaults.uielement.aspects.types.EnabledType;
import org.linkki.core.defaults.uielement.aspects.types.RequiredType;
import org.linkki.core.defaults.uielement.aspects.types.VisibleType;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.section.annotations.UIYesNoComboBox;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;

/**
 * {@link BindingDefinition} for {@link UIYesNoComboBox}.
 */
public class YesNoComboBoxBindingDefinition implements BindingDefinition {

    private final UIYesNoComboBox uiYesNoComboBox;

    public YesNoComboBoxBindingDefinition(UIYesNoComboBox uiYesNoComboBox) {
        this.uiYesNoComboBox = requireNonNull(uiYesNoComboBox, "uiYesNoComboBox must not be null"); //$NON-NLS-1$
    }

    @Override
    public Component newComponent() {
        LinkkiComboBox comboBox = ComponentFactory.newComboBox();
        comboBox.setItemCaptionProvider(getItemCaptionProvider());
        comboBox.setNullSelectionAllowed(false);
        comboBox.setWidth(uiYesNoComboBox.width());
        return comboBox;
    }

    @Override
    public int position() {
        return uiYesNoComboBox.position();
    }

    @Override
    public String label() {
        return uiYesNoComboBox.label();
    }

    @Override
    public EnabledType enabled() {
        return uiYesNoComboBox.enabled();
    }

    @Override
    public RequiredType required() {
        return uiYesNoComboBox.required();
    }

    @Override
    public VisibleType visible() {
        return uiYesNoComboBox.visible();
    }

    @Override
    public String modelAttribute() {
        return uiYesNoComboBox.modelAttribute();
    }

    private ItemCaptionProvider<?> getItemCaptionProvider() {
        try {
            return uiYesNoComboBox.itemCaptionProvider().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiBindingException(
                    "Cannot instantiate item caption provider " + uiYesNoComboBox.itemCaptionProvider().getName()
                            + " using default constructor.",
                    e);
        }
    }

    @Override
    public String modelObject() {
        return uiYesNoComboBox.modelObject();
    }

}
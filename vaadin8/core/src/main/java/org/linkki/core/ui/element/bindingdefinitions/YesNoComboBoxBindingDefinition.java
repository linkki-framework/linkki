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
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.element.annotation.UIYesNoComboBox;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

/**
 * {@link org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition} for
 * {@link UIYesNoComboBox}.
 * 
 * @deprecated since 1.4.0 because this concept was replaced and this implementation was moved into
 *             {@link UIYesNoComboBox}.<br>
 *             See "Custom UI element annotation" at
 *             <a href="https://doc.linkki-framework.org/">https://doc.linkki-framework.org/</a> for
 *             more information.
 */
@Deprecated
public class YesNoComboBoxBindingDefinition
        implements org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition {

    private final UIYesNoComboBox uiYesNoComboBox;

    public YesNoComboBoxBindingDefinition(UIYesNoComboBox uiYesNoComboBox) {
        this.uiYesNoComboBox = requireNonNull(uiYesNoComboBox, "uiYesNoComboBox must not be null"); //$NON-NLS-1$
    }

    @Override
    public Component newComponent() {
        ComboBox<Object> comboBox = ComponentFactory.newComboBox();
        comboBox.setItemCaptionGenerator(getItemCaptionProvider()::getUnsafeCaption);
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setWidth(uiYesNoComboBox.width());
        return comboBox;
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

    @SuppressWarnings("unchecked")
    private ItemCaptionProvider<Object> getItemCaptionProvider() {
        try {
            return (ItemCaptionProvider<Object>)uiYesNoComboBox.itemCaptionProvider().newInstance();
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
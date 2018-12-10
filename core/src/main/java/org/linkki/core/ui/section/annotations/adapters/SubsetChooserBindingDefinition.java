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

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.components.SubsetChooser;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UISubsetChooser;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

public class SubsetChooserBindingDefinition implements BindingDefinition {

    private final UISubsetChooser uiSubsetChooser;

    public SubsetChooserBindingDefinition(UISubsetChooser uiTwinColSelect) {
        this.uiSubsetChooser = uiTwinColSelect;
    }

    @Override
    public SubsetChooser newComponent() {
        SubsetChooser subsetChooser = ComponentFactory.newSubsetChooser();
        subsetChooser.setItemCaptionProvider(getItemCaptionProvider());
        subsetChooser.setNullSelectionAllowed(true);
        subsetChooser.setWidth(uiSubsetChooser.width());
        subsetChooser.setLeftColumnCaption(uiSubsetChooser.leftColumnCaption());
        subsetChooser.setRightColumnCaption(uiSubsetChooser.rightColumnCaption());
        return subsetChooser;
    }

    @Override
    public int position() {
        return uiSubsetChooser.position();
    }

    @Override
    public String label() {
        return uiSubsetChooser.label();
    }

    @Override
    public EnabledType enabled() {
        return uiSubsetChooser.enabled();
    }

    @Override
    public RequiredType required() {
        return uiSubsetChooser.required();
    }

    @Override
    public VisibleType visible() {
        return uiSubsetChooser.visible();
    }

    @Override
    public String modelAttribute() {
        return uiSubsetChooser.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiSubsetChooser.noLabel();
    }

    private ItemCaptionProvider<?> getItemCaptionProvider() {
        try {
            return uiSubsetChooser.itemCaptionProvider().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiBindingException(
                    "Cannot instantiate item caption provider " + uiSubsetChooser.itemCaptionProvider().getName()
                            + " using default constructor.",
                    e);
        }
    }

    @Override
    public String modelObject() {
        return uiSubsetChooser.modelObject();
    }

}
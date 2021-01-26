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
import org.linkki.core.ui.element.annotation.UISubsetChooser;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.ui.TwinColSelect;

/**
 * {@link org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition} for
 * {@link UISubsetChooser}.
 * 
 * @deprecated since 1.4.0 because this concept was replaced and this implementation was moved into
 *             {@link UISubsetChooser}.<br>
 *             See "Custom UI element annotation" at
 *             <a href="https://doc.linkki-framework.org/">https://doc.linkki-framework.org/</a> for
 *             more information.
 */
@Deprecated
public class SubsetChooserBindingDefinition
        implements org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition {

    private final UISubsetChooser uiSubsetChooser;

    public SubsetChooserBindingDefinition(UISubsetChooser uiSubsetChooser) {
        this.uiSubsetChooser = requireNonNull(uiSubsetChooser, "uiSubsetChooser must not be null");
    }

    @Override
    public TwinColSelect<?> newComponent() {
        TwinColSelect<Object> subsetChooser = ComponentFactory.newTwinColSelect();
        subsetChooser.setItemCaptionGenerator(getItemCaptionProvider()::getUnsafeCaption);
        subsetChooser.setWidth(uiSubsetChooser.width());
        subsetChooser.setLeftColumnCaption(uiSubsetChooser.leftColumnCaption());
        subsetChooser.setRightColumnCaption(uiSubsetChooser.rightColumnCaption());
        return subsetChooser;
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

    @SuppressWarnings("unchecked")
    private ItemCaptionProvider<Object> getItemCaptionProvider() {
        try {
            return (ItemCaptionProvider<Object>)uiSubsetChooser.itemCaptionProvider().newInstance();
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
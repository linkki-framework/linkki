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
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.annotation.Nullable;

import org.linkki.core.binding.aspect.AspectUpdaters;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelBinding implements ElementBinding {

    private final Label label;
    private final PropertyDispatcher propertyDispatcher;
    private AspectUpdaters aspects;

    /**
     * Creates the binding for a label. If that label is used to display the non-editable value of a
     * property, there might be another label naming that property, which is passed as the first
     * parameter and will be set visible depending on the bound label's visibility.
     * 
     * @param labelTextForLabel the label's label (for example the property name if the label is a
     *            property value)
     * @param label the label to be bound
     * @param propertyDispatcher the dispatcher responsible to retrieve the displayed value and possibly
     *            visibility and tooltip information
     */
    public LabelBinding(@Nullable Label labelTextForLabel, Label label, PropertyDispatcher propertyDispatcher,
            List<LinkkiAspectDefinition> aspectDefinitions) {
        this.label = requireNonNull(label, "label must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");

        aspects = new AspectUpdaters(aspectDefinitions, propertyDispatcher,
                new LabelComponentWrapper(labelTextForLabel, label),
                Handler.NOP_HANDLER);
    }

    @Override
    public Component getBoundComponent() {
        return label;
    }

    @Override
    public void updateFromPmo() {
        aspects.updateUI();
    }

    /**
     * We do not support messages on buttons at the moment.
     */
    @Override
    public MessageList displayMessages(@Nullable MessageList messages) {
        return new MessageList();
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

}

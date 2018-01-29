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

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.binding.aspect.AspectUpdaters;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

public class ButtonBinding implements ElementBinding, Serializable {

    private static final long serialVersionUID = 1L;

    private final Button button;
    private final Optional<Label> label;
    private final PropertyDispatcher propertyDispatcher;
    private final Handler updateUi;

    private boolean bindCaption;

    private AspectUpdaters aspects;

    /**
     * Creates a new {@link ButtonBinding}.
     * 
     * @param label the button's label (optional)
     * @param button the {@link Button} to be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the model
     *            object
     * @param modelChanged a {@link Handler} that is called when this {@link Binding} desires an update
     *            of the UI because the model has changed. Usually the {@link BindingContext#updateUI()}
     *            method.
     * @param bindCaption indicates whether the button's caption should be bound. <code>true</code>
     *            updates the caption. <code>false</code> prevents caption updates and thus also
     *            prevents a caption to be requested from the property dispatcher.
     */
    public ButtonBinding(@Nullable Label label, Button button, PropertyDispatcher propertyDispatcher,
            Handler modelChanged, boolean bindCaption, List<LinkkiAspectDefinition> aspectDefinitions) {
        this.label = Optional.ofNullable(label);
        this.button = requireNonNull(button, "button must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        this.updateUi = requireNonNull(modelChanged, "updateUi must not be null");
        this.bindCaption = bindCaption;
        button.addClickListener(this::buttonClickCallback);

        aspects = new AspectUpdaters(aspectDefinitions, propertyDispatcher, new LabelComponentWrapper(label, button),
                modelChanged);
    }

    @Override
    public void updateFromPmo() {
        button.setEnabled(isEnabled());
        boolean visible = isVisible();
        button.setVisible(visible);
        label.ifPresent(l -> l.setVisible(visible));
        if (bindCaption) {
            button.setCaption(getCaption());
        }

        aspects.updateUI();
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

    public boolean isEnabled() {
        return propertyDispatcher.isEnabled();
    }

    public boolean isVisible() {
        return propertyDispatcher.isVisible();
    }

    @CheckForNull
    public String getCaption() {
        return propertyDispatcher.getCaption();
    }

    private void buttonClickCallback(@SuppressWarnings("unused") ClickEvent event) {
        propertyDispatcher.invoke();
        updateUi.apply();
    }

    @Override
    public Button getBoundComponent() {
        return button;
    }

    /**
     * We do not support messages on buttons at the moment.
     */
    @Override
    public MessageList displayMessages(@Nullable MessageList messages) {
        return new MessageList();
    }
}

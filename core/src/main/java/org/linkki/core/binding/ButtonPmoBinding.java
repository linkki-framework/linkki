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
import java.lang.annotation.Annotation;

import javax.annotation.Nullable;

import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.EnabledAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class ButtonPmoBinding implements ElementBinding, Serializable {

    private static final long serialVersionUID = 1L;

    private final Button button;
    private final PropertyDispatcher propertyDispatcher;
    private final Handler modelChanged;

    private Handler uiUpdater;

    /**
     * Creates a new {@link ButtonPmoBinding}.
     * 
     * @param button the {@link Button} to be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the
     *            model object
     * @param modelChanged a {@link Handler} that is called when this {@link Binding} desires an
     *            update of the UI. Usually the {@link BindingContext#updateUI()} method.
     */
    public ButtonPmoBinding(Button button, PropertyDispatcher propertyDispatcher, Handler modelChanged) {
        this.button = requireNonNull(button, "button must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");
        this.modelChanged = requireNonNull(modelChanged, "updateUI must not be null");

        uiUpdater = new ButtonPmoAspectDefinition().createUiUpdater(propertyDispatcher,
                                                                    new ButtonPmoButtonWrapper(button));
        button.addClickListener(this::buttonClickCallback);
    }

    public static Button createBoundButton(BindingContext bindingContext, ButtonPmo pmo) {
        requireNonNull(bindingContext, "bindingContext must not be null");
        requireNonNull(pmo, "pmo must not be null");

        Button button = ComponentFactory.newButton(pmo.getButtonIcon(), pmo.getStyleNames());
        bindingContext.bind(pmo, button);
        return button;
    }

    @Override
    public void updateFromPmo() {
        button.setVisible(isVisible());
        uiUpdater.apply();
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
    }

    public boolean isVisible() {
        return propertyDispatcher.isVisible();
    }

    private void buttonClickCallback(@SuppressWarnings("unused") ClickEvent event) {
        propertyDispatcher.invoke();
        modelChanged.apply();
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

    private static class ButtonPmoAspectDefinition extends CompositeAspectDefinition {
        public ButtonPmoAspectDefinition() {
            super(new ButtonPmoEnabledAspectDefinition());
        }
    }

    private static class ButtonPmoEnabledAspectDefinition extends EnabledAspectDefinition {

        @Override
        public void initialize(Annotation annotation) {
            // does nothing
        }

        @Override
        public EnabledType getEnabledType() {
            return EnabledType.DYNAMIC;
        }
    }

    private static class ButtonPmoButtonWrapper implements ComponentWrapper {

        private Button wrappedButton;

        public ButtonPmoButtonWrapper(Button button) {
            wrappedButton = button;
        }

        @Override
        public void setLabel(String labelText) {
            wrappedButton.setCaption(labelText);
        }

        @Override
        public void setEnabled(boolean enabled) {
            wrappedButton.setEnabled(enabled);
        }

        @Override
        public void setVisible(boolean visible) {
            wrappedButton.setVisible(visible);
        }

        @Override
        public void setTooltip(String text) {
            wrappedButton.setDescription(text);
        }

        @Override
        public Button getComponent() {
            return wrappedButton;
        }
    }
}

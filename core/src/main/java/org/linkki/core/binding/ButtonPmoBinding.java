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
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.section.annotations.aspect.ButtonInvokeAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.EnabledAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.VisibleAspectDefinition;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;

public class ButtonPmoBinding implements ElementBinding, Serializable {

    private static final long serialVersionUID = 1L;

    private final Button button;
    private final PropertyDispatcher propertyDispatcher;
    private Handler uiUpdater;

    /**
     * Creates a new {@link ButtonPmoBinding}.
     * 
     * @param button the {@link Button} to be bound
     * @param propertyDispatcher the {@link PropertyDispatcher} handling the bound property in the model
     *            object
     * @param modelUpdated a {@link Handler} that is called when this {@link Binding} desires an update
     *            of the UI. Usually the {@link BindingContext#updateUI()} method.
     */
    public ButtonPmoBinding(Button button, PropertyDispatcher propertyDispatcher, Handler modelUpdated) {
        this.button = requireNonNull(button, "button must not be null");
        this.propertyDispatcher = requireNonNull(propertyDispatcher, "propertyDispatcher must not be null");

        ButtonPmoButtonWrapper buttonWrapper = new ButtonPmoButtonWrapper(button);
        ButtonPmoAspectDefinition aspectDefinition = new ButtonPmoAspectDefinition();
        aspectDefinition.initModelUpdate(propertyDispatcher, buttonWrapper, modelUpdated);
        uiUpdater = aspectDefinition.createUiUpdater(propertyDispatcher, buttonWrapper);
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
        uiUpdater.apply();
    }

    @Override
    public PropertyDispatcher getPropertyDispatcher() {
        return propertyDispatcher;
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
            super(new ButtonPmoEnabledAspectDefinition(),
                    new ButtonPmoVisibleAspectDefinition(),
                    new ButtonPmoInvokeAspectDefinition());
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

    private static class ButtonPmoVisibleAspectDefinition extends VisibleAspectDefinition {

        @Override
        public void initialize(Annotation annotation) {
            // does nothing
        }

        @Override
        public VisibleType getVisibleType() {
            return VisibleType.DYNAMIC;
        }
    }

    /**
     * Cannot use {@link ButtonInvokeAspectDefinition} as {@link ButtonPmo} uses an empty String as
     * fixed property name (see
     * {@link PropertyDispatcherFactory#createDispatcherChain(ButtonPmo, org.linkki.core.binding.dispatcher.PropertyBehaviorProvider)}),
     * thus the invoke button has a fixed aspect name of {@link ButtonPmo#onClick()}.
     */
    private static class ButtonPmoInvokeAspectDefinition implements LinkkiAspectDefinition {

        public static final String NAME = "onClick";

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return Handler.NOP_HANDLER;
        }

        @Override
        public void initModelUpdate(PropertyDispatcher propertyDispatcher,
                ComponentWrapper componentWrapper,
                Handler modelUpdated) {
            Button button = (Button)componentWrapper.getComponent();
            button.addClickListener(e -> {
                propertyDispatcher.push(Aspect.of(NAME));
                modelUpdated.apply();
            });
        }

        @Override
        public void initialize(Annotation annotation) {
            // does nothing
        }
    }

    private static class ButtonPmoButtonWrapper implements ComponentWrapper {

        private static final long serialVersionUID = 1L;

        private Button wrappedButton;

        public ButtonPmoButtonWrapper(Button button) {
            wrappedButton = button;
        }

        @Override
        public void setId(String id) {
            wrappedButton.setId(id);
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

        @Override
        public void setValidationMessages(MessageList messagesForProperty) {
            // do nothing
        }
    }
}

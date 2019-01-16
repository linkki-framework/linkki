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
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.property.BoundProperty;
import org.linkki.core.ui.components.CaptionComponentWrapper;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.components.WrapperType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.section.annotations.aspect.ButtonInvokeAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.EnabledAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.VisibleAspectDefinition;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;

public class ButtonPmoBinding {

    private ButtonPmoBinding() {
        // no instances
    }

    public static Button createBoundButton(BindingContext bindingContext, ButtonPmo pmo) {
        requireNonNull(bindingContext, "bindingContext must not be null");
        requireNonNull(pmo, "pmo must not be null");

        Button button = ComponentFactory.newButton(pmo.getButtonIcon(), pmo.getStyleNames());
        ComponentWrapper buttonWrapper = new CaptionComponentWrapper<>("buttonPmo", button, WrapperType.FIELD);
        bindingContext.bind(pmo, BoundProperty.of(""), Arrays.asList(new ButtonPmoAspectDefinition()),
                            buttonWrapper);
        return button;
    }

    static class ButtonPmoAspectDefinition extends CompositeAspectDefinition {
        public ButtonPmoAspectDefinition() {
            super(new EnabledAspectDefinition(EnabledType.DYNAMIC),
                    new VisibleAspectDefinition(VisibleType.DYNAMIC),
                    new ButtonPmoInvokeAspectDefinition());
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

    }

}

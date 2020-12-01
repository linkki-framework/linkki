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
package org.linkki.core.ui.creation.section;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.ui.aspects.ButtonInvokeAspectDefinition;
import org.linkki.core.ui.wrapper.CaptionComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;

public class ButtonPmoBinder {

    private ButtonPmoBinder() {
        // no instances
    }

    public static Button createBoundButton(BindingContext bindingContext, Object pmo) {
        requireNonNull(bindingContext, "bindingContext must not be null");
        requireNonNull(pmo, "pmo must not be null");

        Button button = ComponentFactory.newButton();
        ComponentWrapper buttonWrapper = new CaptionComponentWrapper("buttonPmo", button, WrapperType.FIELD);
        bindingContext.bind(pmo, BoundProperty.of(""), Arrays.asList(new ButtonPmoAspectDefinition()),
                            buttonWrapper);
        return button;
    }

    static class ButtonPmoAspectDefinition extends CompositeAspectDefinition {
        public ButtonPmoAspectDefinition() {
            super(new EnabledAspectDefinition(EnabledType.DYNAMIC),
                    new VisibleAspectDefinition(VisibleType.DYNAMIC),
                    new ButtonPmoInvokeAspectDefinition(),
                    new ButtonPmoIconAspectDefinition(),
                    new ButtonPmoStyleAspectDefinition());
        }
    }

    /**
     * Cannot use {@link ButtonInvokeAspectDefinition} as {@link ButtonPmo} uses an empty String as
     * fixed property name (see
     * {@link PropertyDispatcherFactory#createDispatcherChain(ButtonPmo, org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider)}),
     * thus the invoke button has a fixed aspect name of {@link ButtonPmo#onClick()}.
     */
    private static class ButtonPmoInvokeAspectDefinition implements LinkkiAspectDefinition {

        public static final String NAME = "onClick";

        ButtonPmoInvokeAspectDefinition() {
            // to avoid generating synthetic accessor
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return Handler.NOP_HANDLER;
        }

        @Override
        public void initModelUpdate(PropertyDispatcher propertyDispatcher,
                ComponentWrapper componentWrapper,
                Handler modelUpdated) {
            Button button = (Button)componentWrapper.getComponent();
            button.setDisableOnClick(true);
            button.addClickListener(e -> {
                propertyDispatcher.push(Aspect.of(NAME));
                modelUpdated.apply();
            });
        }

    }

    private static class ButtonPmoIconAspectDefinition extends ModelToUiAspectDefinition<Resource> {

        ButtonPmoIconAspectDefinition() {
            // to avoid generating synthetic accessor
        }

        @Override
        public Aspect<Resource> createAspect() {
            return Aspect.of("buttonIcon");
        }

        @Override
        public Consumer<Resource> createComponentValueSetter(ComponentWrapper componentWrapper) {
            return ((Button)componentWrapper.getComponent())::setIcon;
        }

    }

    private static class ButtonPmoStyleAspectDefinition extends StaticModelToUiAspectDefinition<Collection<String>> {

        ButtonPmoStyleAspectDefinition() {
            // to avoid generating synthetic accessor
        }

        @Override
        public Aspect<Collection<String>> createAspect() {
            return Aspect.of("styleNames");
        }

        @Override
        public Consumer<Collection<String>> createComponentValueSetter(ComponentWrapper componentWrapper) {
            return l -> l.forEach(((Button)componentWrapper.getComponent())::addStyleName);
        }

    }

}

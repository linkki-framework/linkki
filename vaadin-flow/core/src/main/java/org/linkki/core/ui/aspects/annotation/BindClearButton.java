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

package org.linkki.core.ui.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.annotation.BindClearButton.ClearButtonAspectDefinitionCreator;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.shared.HasClearButton;

/**
 * Makes the clear button visible for fields that support it.
 *
 * @implNote This annotation is evaluated only once upon creation. The visibility of the clear button cannot be
 * defined dynamically.
 */
@LinkkiAspect(ClearButtonAspectDefinitionCreator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BindClearButton {

    class ClearButtonAspectDefinitionCreator implements AspectDefinitionCreator<BindClearButton> {

        @Override
        public LinkkiAspectDefinition create(BindClearButton annotation) {
            return new ClearButtonAspectDefinition();
        }
    }

    class ClearButtonAspectDefinition implements LinkkiAspectDefinition {

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            switch (componentWrapper.getComponent()) {
                case HasClearButton component -> component.setClearButtonVisible(true);
                case DateTimePicker component -> component.getChildren().filter(HasClearButton.class::isInstance)
                        .map(HasClearButton.class::cast)
                        .forEach(f -> f.setClearButtonVisible(true));
                default -> throw new IllegalArgumentException(
                        "@BindClearButton cannot be used with " +
                                componentWrapper.getComponent().getClass().getSimpleName() +
                                " as it does not support clear buttons.");
            }

            return Handler.NOP_HANDLER;
        }

    }
}

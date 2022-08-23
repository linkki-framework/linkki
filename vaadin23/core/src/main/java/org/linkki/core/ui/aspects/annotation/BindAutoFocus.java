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
package org.linkki.core.ui.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.dom.Element;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.util.Consumers;

/**
 * Sets the
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTML/Global_attributes/autofocus">autofocus</a>
 * property of a {@link HasValue} UI element to {@code true} when the ElementBinding is created.
 * <b>Usage:</b>
 * <p>
 * Should only be used on one visible, {@link HasValue#isReadOnly() editable} UI element.
 * 
 * <pre>
 * {@literal @BindAutoFocus}<br>{@literal @UITextField(position = 10)}<br> public void text(){}
 * </pre>
 * 
 * <b>Limitations:</b>
 * <ul>
 * <li>UI elements that can be {@link BindReadOnly.ReadOnlyType#DYNAMIC dynamically}
 * {@link HasValue#isReadOnly() read-only} will also be highlighted.
 * <li>does not work in combination with {@link UIRadioButtons}
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@LinkkiAspect(BindAutoFocus.BindAutoFocusAspectDefinitionCreator.class)
public @interface BindAutoFocus {

    /**
     * Aspect definition creator for the {@link BindAutoFocus} annotation.
     */
    class BindAutoFocusAspectDefinitionCreator implements AspectDefinitionCreator<BindAutoFocus> {

        @Override
        public LinkkiAspectDefinition create(BindAutoFocus annotation) {
            return new BindAutoFocusAspectDefinition();
        }
    }

    /**
     * Aspect that sets an {@link Element}'s {@code autofocus} property to {@code true}.
     */
    class BindAutoFocusAspectDefinition extends StaticModelToUiAspectDefinition<Boolean> {

        private static final String AUTO_FOCUS = "autofocus";

        @Override
        public Aspect<Boolean> createAspect() {
            return Aspect.of(AUTO_FOCUS, true);
        }

        @Override
        public Consumer<Boolean> createComponentValueSetter(ComponentWrapper componentWrapper) {
            if (componentWrapper.getComponent() instanceof HasValue
                    && !((HasValue<?, ?>)componentWrapper.getComponent()).isReadOnly()
                    && componentWrapper.getComponent() instanceof HasElement) {
                // in case of a DateTimePicker the first child (datePicker) is autofocused.
                if (componentWrapper.getComponent() instanceof DateTimePicker) {
                    return bool -> ((DateTimePicker)componentWrapper.getComponent())
                            .getElement()
                            .getChildren()
                            .findFirst()
                            .ifPresent(e -> e.setProperty(AUTO_FOCUS, bool));
                } else {
                    return bool -> ((HasElement)componentWrapper.getComponent())
                            .getElement()
                            .setProperty(AUTO_FOCUS, bool);
                }
            }
            return Consumers.nopConsumer();
        }
    }
}

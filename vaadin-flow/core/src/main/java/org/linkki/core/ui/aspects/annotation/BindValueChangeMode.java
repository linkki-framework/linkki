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
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;

import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;

/**
 * Sets the {@link ValueChangeMode valueChangeMode} property of a {@link HasValueChangeMode} UI
 * element when the ElementBinding is created. <b>Usage:</b>
 *
 * <pre>
 * {@literal @BindValueChangeMode(EAGER)}<br>{@literal @UITextField(position = 10)}<br> public void text(){}
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindValueChangeMode.BindValueChangeModeAspectDefinitionCreator.class)
public @interface BindValueChangeMode {

    /**
     * Specifies the way a fields value on the client side is synchronized with the server side.
     */
    ValueChangeMode value();

    class BindValueChangeModeAspectDefinitionCreator implements AspectDefinitionCreator<BindValueChangeMode> {

        @Override
        public LinkkiAspectDefinition create(BindValueChangeMode annotation) {
            return new BindValueChangeModeAspectDefinition(annotation.value());
        }

    }

    /**
     * Aspect that sets an {@link Element}'s {@link ValueChangeMode} property.
     */
    class BindValueChangeModeAspectDefinition extends StaticModelToUiAspectDefinition<ValueChangeMode> {

        public static final String NAME = "valueChangeMode";

        private final ValueChangeMode value;

        public BindValueChangeModeAspectDefinition(ValueChangeMode value) {
            this.value = value;
        }

        @Override
        public Aspect<ValueChangeMode> createAspect() {
            return Aspect.of(NAME, value);
        }

        @Override
        public Consumer<ValueChangeMode> createComponentValueSetter(ComponentWrapper componentWrapper) {
            var hasValueChangeMode = (HasValueChangeMode)componentWrapper.getComponent();
            return hasValueChangeMode::setValueChangeMode;
        }

    }

}

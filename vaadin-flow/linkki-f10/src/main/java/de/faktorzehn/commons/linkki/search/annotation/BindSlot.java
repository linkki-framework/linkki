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
package de.faktorzehn.commons.linkki.search.annotation;

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

import com.vaadin.flow.component.Component;

/**
 * Annotation to set the attribute "slot" of a component's element. This is used to place a
 * {@link Component} in a specific slot in the parent.
 * 
 * @deprecated The {@code @BindSlot} annotation is available in linkki since version 2.3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@LinkkiAspect(BindSlot.SlotAspectDefinitionCreator.class)
@Deprecated(since = "22.12")
public @interface BindSlot {

    String value();

    @Deprecated(since = "22.12")
    class SlotAspectDefinitionCreator implements AspectDefinitionCreator<BindSlot> {

        public static final String SLOT_ATTRIBUTE = "slot";

        @Override
        public LinkkiAspectDefinition create(BindSlot annotation) {
            return new ElementAttributeAspectDefinition(SLOT_ATTRIBUTE, annotation.value());
        }
    }

    @Deprecated(since = "22.12")
    class ElementAttributeAspectDefinition extends StaticModelToUiAspectDefinition<String> {

        public static final String ATTRIBUTE_ASPECT_NAME_SUFFIX = "Attribute";

        private final String attribute;
        private final String value;

        public ElementAttributeAspectDefinition(String attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }

        @Override
        public Aspect<String> createAspect() {
            return Aspect.of(attribute + ATTRIBUTE_ASPECT_NAME_SUFFIX, value);
        }

        @Override
        public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
            return v -> ((Component)componentWrapper.getComponent()).getElement().setAttribute(attribute, value);
        }
    }
}

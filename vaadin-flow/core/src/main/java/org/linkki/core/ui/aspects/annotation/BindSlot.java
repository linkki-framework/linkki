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
import org.linkki.core.ui.aspects.ElementAttributeAspectDefinition;

/**
 * Binds the annotated UI element to a
 * <a href="https://html.spec.whatwg.org/multipage/scripting.html#the-slot-element">slot element</a>
 * within its parent component.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@LinkkiAspect(BindSlot.SlotAspectDefinitionCreator.class)
public @interface BindSlot {

    /**
     * The name of the slot to bind the UI element to.
     */
    String value();

    class SlotAspectDefinitionCreator implements AspectDefinitionCreator<BindSlot> {

        public static final String SLOT_ATTRIBUTE = "slot";

        @Override
        public LinkkiAspectDefinition create(BindSlot annotation) {
            return new ElementAttributeAspectDefinition(SLOT_ATTRIBUTE, annotation.value());
        }
    }
}

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
import org.linkki.core.ui.aspects.BindClosableAspectDefinition;
import org.linkki.core.ui.layout.annotation.UIFormSection;
import org.linkki.core.ui.layout.annotation.UISection;

/**
 * This aspect can be applied to a {@link UISection} or {@link UIFormSection} to create a closable
 * section.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@LinkkiAspect(BindClosable.BindClosableAspectDefinitionCreator.class)
public @interface BindClosable {

    /**
     * Specifies whether the section should be initially closed.
     */
    boolean initial() default false;

    class BindClosableAspectDefinitionCreator implements AspectDefinitionCreator<BindClosable> {

        @Override
        public LinkkiAspectDefinition create(BindClosable annotation) {
            return new BindClosableAspectDefinition(annotation.initial());
        }
    }
}

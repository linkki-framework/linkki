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
import org.linkki.core.ui.aspects.HeightAspectDefinition;

/**
 * Annotation to bind a height to a UI Component.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@LinkkiAspect(BindHeight.BindHeightAspectDefinitionCreator.class)
public @interface BindHeight {
    /**
     * Defines the height as a CSS-compatible string, such as {@code "100px"}, {@code "50%"}, or
     * {@code "10em"}.
     */
    String value();

    class BindHeightAspectDefinitionCreator implements AspectDefinitionCreator<BindHeight> {

        @Override
        public LinkkiAspectDefinition create(BindHeight annotation) {
            return new HeightAspectDefinition(annotation.value());
        }
    }
}

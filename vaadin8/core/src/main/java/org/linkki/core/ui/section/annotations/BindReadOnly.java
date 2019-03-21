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

package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.section.annotations.BindReadOnly.BindReadOnlyAspectDefinitionCreator;
import org.linkki.core.ui.section.annotations.aspect.BindReadOnlyAspectDefinition;

/**
 * Binds read-only behavior to UI elements. The annotation can be used in combination with
 * UI-annotations or {@link Bind}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindReadOnlyAspectDefinitionCreator.class)
public @interface BindReadOnly {

    /**
     * If and how the read-only state of the UI element is bound to the PMO.
     *
     * @return the read only type
     */
    ReadOnlyType value() default ReadOnlyType.ALWAYS;


    /** The type how the read-only state of an UI component is bound. */
    enum ReadOnlyType {

        /**
         * Always displays as read-only.
         */
        ALWAYS,

        /**
         * Behavior as it would be without this annotation.
         */
        DERIVED,

        /**
         * Looks for a method "is[PropertyName]ReadOnly()" to determine if it is read-only.
         */
        DYNAMIC;
    }

    class BindReadOnlyAspectDefinitionCreator implements AspectDefinitionCreator<BindReadOnly> {

        @Override
        public LinkkiAspectDefinition create(BindReadOnly annotation) {
            return new BindReadOnlyAspectDefinition(annotation.value());
        }

    }

}

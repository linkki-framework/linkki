/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.binding.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.annotations.Bind.BindAvailableValuesAspectDefinition;
import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.binding.aspect.definition.AvailableValuesAspectDefinition;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.VisibleType;

/**
 * @author ortmann
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindAvailableValuesAspectDefinition.class)
public @interface Bind {

    /** The name of the PMO's property to which the UI element is bound. */
    String pmoProperty();

    /** If and how the enabled state of the UI element is bound to the PMO. */
    EnabledType enabled() default EnabledType.ENABLED;

    /** If and how the visible state of the UI element is bound to the PMO. */
    VisibleType visible() default VisibleType.VISIBLE;

    /**
     * If and how the required state of the UI element is bound to the PMO. Ignored for UI elements that
     * cannot be required, e.g. buttons.
     */
    RequiredType required() default RequiredType.NOT_REQUIRED;

    /**
     * If and how the available values are bound to the PMO. Relevant only for UI elements that have
     * available values (e.g. combo boxes), ignored for all other elements.
     */
    AvailableValuesType availableValues() default AvailableValuesType.NO_VALUES;

    class BindAvailableValuesAspectDefinition extends AvailableValuesAspectDefinition {

        @SuppressWarnings("null")
        private Bind bindAnnotation;

        @Override
        public void initialize(Annotation annotation) {
            bindAnnotation = (Bind)annotation;
        }

        @Override
        protected AvailableValuesType getAvailableValuesType() {
            return bindAnnotation.availableValues();
        }

    }
}

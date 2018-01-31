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

package org.linkki.core.ui.section.annotations.aspect;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.definition.AvailableValuesAspectDefinition;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.EnabledAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.aspect.definition.RequiredAspectDefinition;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;

/**
 * Aspect definitions for the {@link Bind} annotation.
 */
public class BindAnnotationAspecDefinition extends CompositeAspectDefinition {

    public BindAnnotationAspecDefinition() {
        super(createAspectDefinitions());
    }

    private static List<LinkkiAspectDefinition> createAspectDefinitions() {
        BindEnabledAspectDefinition enabledTypeAspectDefinition = new BindEnabledAspectDefinition();
        return Arrays.asList(
                             new BindAvailableValuesAspectDefinition(),
                             enabledTypeAspectDefinition,
                             new BindRequiredAspectDefinition(enabledTypeAspectDefinition));
    }


    private static class BindAvailableValuesAspectDefinition extends AvailableValuesAspectDefinition {

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

    private static class BindEnabledAspectDefinition extends EnabledAspectDefinition {

        @SuppressWarnings("null")
        private Bind bindAnnotation;

        @Override
        public void initialize(Annotation annotation) {
            bindAnnotation = (Bind)annotation;
        }

        @Override
        public EnabledType getEnabledType() {
            return bindAnnotation.enabled();
        }
    }

    private static class BindRequiredAspectDefinition extends RequiredAspectDefinition {

        @SuppressWarnings("null")
        private Bind bindAnnotation;

        public BindRequiredAspectDefinition(EnabledAspectDefinition enabledTypeAspectDefinition) {
            super(enabledTypeAspectDefinition);
        }

        @Override
        public void initialize(Annotation annotation) {
            bindAnnotation = (Bind)annotation;
        }

        @Override
        public RequiredType getRequiredType() {
            return bindAnnotation.required();
        }

    }
}

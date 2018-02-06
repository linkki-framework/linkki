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
import org.linkki.core.binding.aspect.definition.FieldValueAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.aspect.definition.RequiredAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Label;

/**
 * Aspect definitions for the {@link Bind} annotation.
 */
public class BindAnnotationAspectDefinition extends CompositeAspectDefinition {

    public BindAnnotationAspectDefinition() {
        super(createAspectDefinitions());
    }

    private static List<LinkkiAspectDefinition> createAspectDefinitions() {
        BindEnabledAspectDefinition enabledTypeAspectDefinition = new BindEnabledAspectDefinition();
        return Arrays.asList(new BindAvailableValuesAspectDefinition(),
                             enabledTypeAspectDefinition,
                             new BindRequiredAspectDefinition(enabledTypeAspectDefinition),
                             new BindFieldValueAspectDefinition(),
                             new BindLabelValueAspectDefinition());
    }

    private static class BindAvailableValuesAspectDefinition extends AvailableValuesAspectDefinition {

        @SuppressWarnings("null")
        private Bind bindAnnotation;

        @Override
        public void initialize(Annotation annotation) {
            bindAnnotation = (Bind)annotation;
        }

        @Override
        protected boolean ignoreNonAbstractSelect() {
            return true;
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

    /**
     * Subclass of {@link FieldValueAspectDefinition} to avoid exception if the annotated element is
     * not an {@link AbstractField}. This is necessary as {@link Bind} annotation can be used for
     * various UI components. In case {@link FieldValueAspectDefinition} is used for a custom
     * annotation, it is more helpful to throw an exception to remind the implementer that the
     * annotated element is not an {@link AbstractField} thus the aspect definition would do
     * nothing.
     */
    private static class BindFieldValueAspectDefinition extends FieldValueAspectDefinition {

        @Override
        public void initModelUpdate(PropertyDispatcher propertyDispatcher,
                ComponentWrapper componentWrapper,
                Handler modelUpdated) {
            if (isApplicableFor(componentWrapper)) {
                super.initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated);
            }
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            if (isApplicableFor(componentWrapper)) {
                return super.createUiUpdater(propertyDispatcher, componentWrapper);
            } else {
                return Handler.NOP_HANDLER;
            }
        }

        public boolean isApplicableFor(ComponentWrapper componentWrapper) {
            return componentWrapper.getComponent() instanceof AbstractField;
        }
    }

    private static class BindLabelValueAspectDefinition extends LabelValueAspectDefinition {

        @Override
        public void initModelUpdate(PropertyDispatcher propertyDispatcher,
                ComponentWrapper componentWrapper,
                Handler modelUpdated) {
            if (isApplicableFor(componentWrapper)) {
                super.initModelUpdate(propertyDispatcher, componentWrapper, modelUpdated);
            }
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            if (isApplicableFor(componentWrapper)) {
                return super.createUiUpdater(propertyDispatcher, componentWrapper);
            } else {
                return Handler.NOP_HANDLER;
            }
        }

        public boolean isApplicableFor(ComponentWrapper componentWrapper) {
            return componentWrapper.getComponent() instanceof Label;
        }
    }
}

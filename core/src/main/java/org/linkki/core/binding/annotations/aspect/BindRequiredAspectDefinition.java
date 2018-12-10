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

package org.linkki.core.binding.annotations.aspect;

import java.lang.annotation.Annotation;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.aspect.EnabledAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.RequiredAspectDefinition;

/**
 * Required aspect binding definition for {@link Bind} annotation.
 */
class BindRequiredAspectDefinition extends RequiredAspectDefinition {

    private Bind bindAnnotation;

    @SuppressWarnings("null")
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
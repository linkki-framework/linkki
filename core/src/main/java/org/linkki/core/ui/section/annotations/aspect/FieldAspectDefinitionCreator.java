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

package org.linkki.core.ui.section.annotations.aspect;

import java.lang.annotation.Annotation;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.LinkkiBindingDefinition;

/**
 * Aspect definition creator for all annotations that are annotated with
 * {@link LinkkiBindingDefinition}.
 */
public class FieldAspectDefinitionCreator implements LinkkiAspect.Creator<@NonNull Annotation> {

    @Override
    public LinkkiAspectDefinition create(Annotation annotation) {
        BindingDefinition bindingDefinition = BindingDefinition.from(annotation);

        EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(bindingDefinition.enabled());
        RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                bindingDefinition.required(),
                enabledAspectDefinition);

        return new CompositeAspectDefinition(new LabelAspectDefinition(bindingDefinition.label()),
                enabledAspectDefinition,
                requiredAspectDefinition,
                new VisibleAspectDefinition(bindingDefinition.visible()),
                new FieldValueAspectDefinition(),
                new ReadOnlyAspectDefinition());
    }

}

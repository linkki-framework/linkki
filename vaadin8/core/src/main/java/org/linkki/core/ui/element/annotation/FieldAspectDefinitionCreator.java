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

package org.linkki.core.ui.element.annotation;

import java.lang.annotation.Annotation;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;

/**
 * Aspect definition creator for all annotations that are annotated with
 * {@link org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition}.
 * 
 * @deprecated since 1.4.0 because this concept was replaced. See "Custom UI element annotation" at
 *             <a href="https://doc.linkki-framework.org/">https://doc.linkki-framework.org/</a> for
 *             more information.
 */
@Deprecated
public class FieldAspectDefinitionCreator implements AspectDefinitionCreator<Annotation> {

    @Override
    public LinkkiAspectDefinition create(Annotation annotation) {
        org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition bindingDefinition = org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition
                .from(annotation);

        EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(bindingDefinition.enabled());
        RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                bindingDefinition.required(),
                enabledAspectDefinition);

        return new CompositeAspectDefinition(new LabelAspectDefinition(bindingDefinition.label()),
                enabledAspectDefinition,
                requiredAspectDefinition,
                new VisibleAspectDefinition(bindingDefinition.visible()),
                new DerivedReadOnlyAspectDefinition());
    }

}

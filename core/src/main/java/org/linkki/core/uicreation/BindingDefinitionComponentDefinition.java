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

package org.linkki.core.uicreation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;

/**
 * Creates a component based on a {@link BindingDefinition}.
 */
public class BindingDefinitionComponentDefinition implements LinkkiComponentDefinition {

    private BindingDefinition bindingDefinition;

    public BindingDefinitionComponentDefinition(Annotation annotation,
            @SuppressWarnings("unused") AnnotatedElement annotatedElement) {
        bindingDefinition = BindingDefinition.from(annotation);
    }

    @Override
    public Object createComponent(Object pmo) {
        return bindingDefinition.newComponent();
    }

    @Override
    public int getPosition() {
        return bindingDefinition.position();
    }

    public static class Creator implements ComponentDefinitionCreator {

        @Override
        public BindingDefinitionComponentDefinition create(Annotation annotation,
                AnnotatedElement annotatedElement) {
            return new BindingDefinitionComponentDefinition(annotation, annotatedElement);
        }
    }
}

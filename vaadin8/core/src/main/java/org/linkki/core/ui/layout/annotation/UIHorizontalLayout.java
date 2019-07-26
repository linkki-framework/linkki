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

package org.linkki.core.ui.layout.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.ui.layout.ComponentContainerLayoutDefinition.ComponentContainerLayoutDefinitionCreateor;
import org.linkki.core.ui.layout.HorizontalLayoutComponentDefinition;
import org.linkki.core.ui.layout.VerticalAlignment;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout.HorizontalLayoutComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@LinkkiLayout(ComponentContainerLayoutDefinitionCreateor.class)
@LinkkiComponent(HorizontalLayoutComponentDefinitionCreator.class)
public @interface UIHorizontalLayout {

    VerticalAlignment alignment() default VerticalAlignment.MIDDLE;

    class HorizontalLayoutComponentDefinitionCreator implements ComponentDefinitionCreator<UIHorizontalLayout> {
        @Override
        public LinkkiComponentDefinition create(UIHorizontalLayout annotation, AnnotatedElement annotatedElement) {
            return new HorizontalLayoutComponentDefinition(annotation.alignment());
        }
    }
}

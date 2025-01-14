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
package de.faktorzehn.commons.linkki.search.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.framework.ui.component.Headline;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
@LinkkiComponent(UISearchLayoutHeadline.HeadlineComponentDefinitionCreator.class)
@LinkkiAspect(UISearchLayoutHeadline.HeadlineAspectCreator.class)
@LinkkiPositioned
public @interface UISearchLayoutHeadline {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position() default 0;

    class HeadlineComponentDefinitionCreator implements ComponentDefinitionCreator<UISearchLayoutHeadline> {

        @Override
        public LinkkiComponentDefinition create(UISearchLayoutHeadline annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                Headline headline = new Headline();
                headline.getContent().setPadding(false);
                return headline;
            };
        }

    }

    class HeadlineAspectCreator implements AspectDefinitionCreator<UISearchLayoutHeadline> {

        @Override
        public LinkkiAspectDefinition create(UISearchLayoutHeadline annotation) {
            return new ModelToUiAspectDefinition<String>() {

                @Override
                public Aspect<String> createAspect() {
                    return Aspect.of(LinkkiAspectDefinition.VALUE_ASPECT_NAME);
                }

                @Override
                public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
                    return t -> ((Headline)componentWrapper.getComponent()).setTitle(t);
                }
            };
        }

    }

}

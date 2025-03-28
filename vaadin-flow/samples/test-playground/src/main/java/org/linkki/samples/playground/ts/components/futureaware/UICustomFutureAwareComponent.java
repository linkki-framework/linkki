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

package org.linkki.samples.playground.ts.components.futureaware;

import java.lang.annotation.Annotation;
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
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.FutureAwareAspectDefinition;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;

import com.vaadin.flow.component.html.Div;

@LinkkiAspect(UICustomFutureAwareComponent.CustomFutureAwareAspectDefinitionCreator.class)
@LinkkiComponent(UICustomFutureAwareComponent.CustomFutureAwareComponentDefinitionCreator.class)
@LinkkiPositioned
@LinkkiBoundProperty(BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UICustomFutureAwareComponent {

    @LinkkiPositioned.Position
    int position();

    class CustomFutureAwareComponentDefinitionCreator implements ComponentDefinitionCreator<Annotation> {

        @Override
        public LinkkiComponentDefinition create(Annotation annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                var div = new Div();
                div.getElement().setAttribute(LinkkiTheme.ATTR_FUTURE_AWARE, true);
                return div;
            };
        }
    }

    class CustomFutureAwareAspectDefinitionCreator implements AspectDefinitionCreator<Annotation> {

        @Override
        public LinkkiAspectDefinition create(Annotation annotation) {
            return new CustomFutureAwareAspectDefinition();
        }
    }

    class CustomFutureAwareAspectDefinition extends FutureAwareAspectDefinition<String> {

        @Override
        public Aspect<String> createAspect() {
            return Aspect.of(LinkkiAspectDefinition.VALUE_ASPECT_NAME);
        }

        @Override
        protected Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
            Div div = (Div)componentWrapper.getComponent();
            return div::setText;
        }

        @Override
        protected String getValueOnError() {
            return null;
        }

    }
}

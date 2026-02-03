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
package org.linkki.framework.ui.component;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.util.handler.Handler;

/**
 * Provides two ways to create a {@link Headline}. A simple {@link Headline} with only a title is created when the title
 * is given as the return value of the annotated method. Alternatively, the method can also return a
 * {@link HeadlinePmo}. Besides the mandatory title, a {@link HeadlinePmo} may also contain an optional pmo that is
 * added to the title and another optional pmo that is added at the end.
 *
 * @since 2.8.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
@LinkkiComponent(UIHeadline.HeadlineComponentDefinitionCreator.class)
@LinkkiAspect(UIHeadline.HeadlineComponentAspectCreator.class)
@LinkkiPositioned
public @interface UIHeadline {

    /**
     * Defines the order in which this element is displayed in a layout.
     */
    @LinkkiPositioned.Position
    int position() default 0;

    class HeadlineComponentDefinitionCreator implements ComponentDefinitionCreator<UIHeadline> {

        @Override
        public LinkkiComponentDefinition create(UIHeadline annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                var headline = new Headline();
                headline.getContent().setPadding(false);
                return headline;
            };
        }
    }

    class HeadlineComponentAspectCreator implements AspectDefinitionCreator<UIHeadline> {

        @Override
        public LinkkiAspectDefinition create(UIHeadline annotation) {
            return new HeadlineComponentTitleAspectDefinition();
        }
    }

    class HeadlineComponentTitleAspectDefinition implements LinkkiAspectDefinition {

        @Override
        public void initModelUpdate(PropertyDispatcher propertyDispatcher,
                                    ComponentWrapper componentWrapper,
                                    Handler modelChanged) {
            var headline = (Headline)componentWrapper.getComponent();
            var aspectValue = requireNonNull(propertyDispatcher.pull(Aspect.of(VALUE_ASPECT_NAME)));

            if (aspectValue instanceof String title) {
                headline.setTitle(title);
            } else if (aspectValue instanceof HeadlinePmo headlinePmo) {
                ((Headline)componentWrapper.getComponent())
                        .setTitle(requireNonNull(headlinePmo.getHeaderTitle()));
                var bindingContext = new BindingContext.BindingContextBuilder()
                        .afterModelChangedHandler(modelChanged)
                        .build();
                if (headlinePmo.getTitleComponentPmo() != null) {
                    headline.addToTitle(VaadinUiCreator.createComponent(headlinePmo.getTitleComponentPmo(),
                                                                        bindingContext));
                }
                if (headlinePmo.getRightComponentPmo() != null) {
                    headline.getContent()
                            .addToEnd(VaadinUiCreator.createComponent(headlinePmo.getRightComponentPmo(),
                                                                      bindingContext));
                }
            } else {
                throw new IllegalStateException("Aspect method must either return a String or a HeadlinePmo");
            }
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return () -> {
                var aspectValue = requireNonNull(propertyDispatcher.pull(Aspect.of(VALUE_ASPECT_NAME)),
                                                 "Aspect method must not return null");
                var headline = (Headline)componentWrapper.getComponent();
                if (aspectValue instanceof String title) {
                    headline.setTitle(title);
                } else if (aspectValue instanceof HeadlinePmo headlinePmo) {
                    headline.setTitle(headlinePmo.getHeaderTitle());
                } else {
                    throw new IllegalStateException("Aspect method must either return a String or a HeadlinePmo");
                }
            };
        }
    }
}

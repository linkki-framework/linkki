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

package org.linkki.core.ui.layout;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.function.Function;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.wrapper.CaptionComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Defines a {@link ComponentContainer} that contains {@link CaptionComponentWrapper
 * CaptionComponentWrappers}.
 */
public class ComponentContainerLayoutDefinition implements LinkkiLayoutDefinition {

    private final Function<Component, ComponentWrapper> componentWrapperCreator;

    public ComponentContainerLayoutDefinition() {
        this.componentWrapperCreator = c -> new CaptionComponentWrapper(c, WrapperType.FIELD);
    }

    @Override
    public void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
        UiCreator.<Component, ComponentWrapper> createUiElements(pmo, bindingContext,
                                                                 c -> componentWrapperCreator.apply(c))//
                .map(ComponentWrapper::getComponent)
                .map(Component.class::cast)
                .forEach(c -> ((ComponentContainer)parentComponent).addComponent(c));
    }

    public static class ComponentContainerLayoutDefinitionCreateor implements LayoutDefinitionCreator<Annotation> {

        @Override
        public LinkkiLayoutDefinition create(Annotation annotation, AnnotatedElement annotatedElement) {
            return new ComponentContainerLayoutDefinition();
        }

    }
}

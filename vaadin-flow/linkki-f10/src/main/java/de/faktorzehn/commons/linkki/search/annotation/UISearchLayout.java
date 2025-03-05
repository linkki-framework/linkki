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

import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.EmptyPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import com.vaadin.flow.component.Component;

/**
 * @deprecated moved to linkki-search-vaadin-flow. Use org.linkki.search.annotation.UISearchLayout instead
 */
@Deprecated(since = "2.8.0")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@LinkkiComponent(UISearchLayout.SearchComponentDefinitionCreator.class)
@LinkkiLayout(UISearchLayout.SearchLayoutDefinitionCreator.class)
@LinkkiBoundProperty(EmptyPropertyCreator.class)
public @interface UISearchLayout {

    static class SearchComponentDefinitionCreator implements ComponentDefinitionCreator<UISearchLayout> {
        @Override
        public LinkkiComponentDefinition create(UISearchLayout annotation, AnnotatedElement annotatedElement) {
            return pmo -> new de.faktorzehn.commons.linkki.search.component.SearchLayout();
        }
    }

    static class SearchLayoutDefinitionCreator implements LayoutDefinitionCreator<UISearchLayout> {

        @Override
        public LinkkiLayoutDefinition create(UISearchLayout annotation, AnnotatedElement annotatedElement) {
            return (parentComponent, pmo, bindingContext) -> {
                UiCreator
                        .<Component, ComponentWrapper> createUiElements(pmo, bindingContext,
                                                                        NoLabelComponentWrapper::new)//
                        .map(ComponentWrapper::getComponent)//
                        .map(Component.class::cast)//
                        .forEach(c -> ((Component)parentComponent).getElement().appendChild(c.getElement()));
            };
        }
    }
}
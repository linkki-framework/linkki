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

import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uicreation.layout.LinkkiLayout;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiLayout(NestedPmoMethodLayoutDefinitionCreator.class)
@LinkkiComponent(UISearchResultLayout.SearchResultComponentDefinitionCreator.class)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
@LinkkiPositioned
public @interface UISearchResultLayout {

    @LinkkiPositioned.Position
    int position();

    static class SearchResultComponentDefinitionCreator implements ComponentDefinitionCreator<UISearchResultLayout> {
        @Override
        public LinkkiComponentDefinition create(UISearchResultLayout annotation, AnnotatedElement annotatedElement) {
            return pmo -> new SearchResultLayout();
        }
    }

    @Tag("search-result-layout")
    @CssImport("./src/search-result-layout.css")
    class SearchResultLayout extends FlexLayout {

        private static final long serialVersionUID = 1L;

        public SearchResultLayout() {
            setWidth("unset");
        }

    }

}

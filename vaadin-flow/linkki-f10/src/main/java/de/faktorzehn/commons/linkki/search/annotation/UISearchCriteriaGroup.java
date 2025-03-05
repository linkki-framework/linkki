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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uicreation.layout.LinkkiLayout;

import de.faktorzehn.commons.linkki.search.component.SearchCriteriaGroup;

/**
 * Use this annotation to create a group of search criteria within your search criteria PMO.
 * <p>
 * The PMO itself does not have to be annotated with any layout annotation, if there is any layout
 * annotation at the PMO class it will be ignored.
 *
 * @deprecated moved to linkki-search-vaadin-flow. Use org.linkki.search.annotation.UISearchCriteriaGroup instead
 */
@Deprecated(since = "2.8.0")
@Retention(RUNTIME)
@Target(METHOD)
@LinkkiPositioned
@LinkkiComponent(UISearchCriteriaGroup.SearchCriteriaGroupComponentDefinitionCreator.class)
@LinkkiLayout(NestedPmoMethodLayoutDefinitionCreator.class)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
public @interface UISearchCriteriaGroup {

    @LinkkiPositioned.Position
    int position();

    String caption() default "";

    class SearchCriteriaGroupComponentDefinitionCreator implements ComponentDefinitionCreator<UISearchCriteriaGroup> {

        @Override
        public LinkkiComponentDefinition create(UISearchCriteriaGroup annotation, AnnotatedElement annotatedElement) {
            return pmo -> new SearchCriteriaGroup(annotation.caption());
        }
    }
}

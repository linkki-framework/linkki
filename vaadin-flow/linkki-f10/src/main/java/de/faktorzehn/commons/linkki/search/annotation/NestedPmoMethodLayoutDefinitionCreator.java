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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.util.reflection.accessor.MemberAccessors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;

/**
 * @deprecated moved to linkki-search-vaadin-flow. Use
 *             org.linkki.search.annotation.NestedPmoMethodLayoutDefinitionCreator instead
 */
@Deprecated(since = "2.8.0")
public class NestedPmoMethodLayoutDefinitionCreator implements LayoutDefinitionCreator<Annotation> {

    @Override
    public LinkkiLayoutDefinition create(Annotation annotation, AnnotatedElement annotatedElement) {
        return (parentComponent, pmo, bindingContext) -> {
            final Object nestedPmo = MemberAccessors.getValue(pmo, (Member)annotatedElement);

            UiCreator
                    .<Component, ComponentWrapper> createUiElements(nestedPmo, bindingContext,
                                                                    c -> new LabelComponentWrapper(c,
                                                                            WrapperType.COMPONENT))//
                    .map(ComponentWrapper::getComponent)//
                    .map(Component.class::cast)//
                    .forEach(c -> ((HasElement)parentComponent).getElement().appendChild(c.getElement()));
        };
    }
}
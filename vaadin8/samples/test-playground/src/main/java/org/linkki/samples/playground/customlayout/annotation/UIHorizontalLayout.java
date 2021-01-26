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

package org.linkki.samples.playground.customlayout.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.EmptyPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.wrapper.CaptionComponentWrapper;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.samples.playground.customlayout.annotation.UIHorizontalLayout.HorizontalComponentDefinitonCreator;
import org.linkki.samples.playground.customlayout.annotation.UIHorizontalLayout.HorizontalLayoutDefinitionCreator;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

// tag::custom-ui-layout-annotation[]
@Retention(RUNTIME) // <1>
@Target(TYPE) // <1>
@LinkkiComponent(HorizontalComponentDefinitonCreator.class) // <2>
@LinkkiLayout(HorizontalLayoutDefinitionCreator.class) // <3>
@LinkkiBoundProperty(EmptyPropertyCreator.class) // <4>
public @interface UIHorizontalLayout {

    // end::custom-ui-layout-annotation[]
    // tag::component-definition[]
    public static class HorizontalComponentDefinitonCreator
            implements ComponentDefinitionCreator<UIHorizontalLayout> {

        @Override
        public LinkkiComponentDefinition create(UIHorizontalLayout annotation, AnnotatedElement annotatedElement) {
            return (p) -> new HorizontalLayout();
        }
    }
    // end::component-definition[]

    // tag::layout-definition[]
    public static class HorizontalLayoutDefinitionCreator implements LayoutDefinitionCreator<UIHorizontalLayout> {

        @Override
        public LinkkiLayoutDefinition create(UIHorizontalLayout annotation, AnnotatedElement annotatedElement) {
            return (pc, pmo, bc) -> createChildren(pc, pmo, bc);
        }

        private void createChildren(Object parentComponent, Object pmo, BindingContext bindingContext) {
            HorizontalLayout horizonalLayout = (HorizontalLayout)parentComponent;
            horizonalLayout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);

            UiCreator.createUiElements(pmo, bindingContext,
                                       c -> new CaptionComponentWrapper((Component)c, WrapperType.COMPONENT))
                    .forEach(w -> horizonalLayout.addComponent(w.getComponent()));
        }
    }

    // end::layout-definition[]
}
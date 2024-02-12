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
package org.linkki.samples.playground.customlayout.annotation;

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
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.samples.playground.customlayout.BindSlotLayout;

import com.vaadin.flow.component.Component;

/**
 * Applies the {@link BindSlotLayout} to a PMO.
 */
// tag::bindSlot-layout-annotation[]
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@LinkkiComponent(UIBindSlotLayout.BindSlotComponentDefinitionCreator.class)
@LinkkiLayout(UIBindSlotLayout.BindSlotLayoutDefinitionCreator.class)
@LinkkiBoundProperty(EmptyPropertyCreator.class)
@LinkkiPositioned
public @interface UIBindSlotLayout {

    class BindSlotComponentDefinitionCreator implements ComponentDefinitionCreator<UIBindSlotLayout> {
        @Override
        public LinkkiComponentDefinition create(UIBindSlotLayout annotation, AnnotatedElement annotatedElement) {
            return pmo -> new BindSlotLayout();
        }
    }

    class BindSlotLayoutDefinitionCreator implements LayoutDefinitionCreator<UIBindSlotLayout> {

        @Override
        public LinkkiLayoutDefinition create(UIBindSlotLayout annotation, AnnotatedElement annotatedElement) {
            return (parentComponent, pmo, bindingContext) -> UiCreator
                    .<Component, ComponentWrapper> createUiElements(pmo, bindingContext, NoLabelComponentWrapper::new)
                    .map(ComponentWrapper::getComponent)
                    .map(Component.class::cast)
                    .forEach(c -> ((Component)parentComponent).getElement().appendChild(c.getElement()));
        }
    }
}
// end::bindSlot-layout-annotation[]

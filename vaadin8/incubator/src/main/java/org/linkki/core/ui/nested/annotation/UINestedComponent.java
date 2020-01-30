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

package org.linkki.core.ui.nested.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.nested.annotation.UINestedComponent.NestedAspecDefinitionCreator;
import org.linkki.core.ui.nested.annotation.UINestedComponent.NestedComponentDefinitionCreator;
import org.linkki.core.ui.nested.annotation.UINestedComponent.NestedLayoutDefinitionCreator;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uicreation.layout.LayoutAnnotationReader;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.util.MemberAccessors;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * Embeds another PMO in the current layout.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiLayout(NestedLayoutDefinitionCreator.class)
@LinkkiComponent(NestedComponentDefinitionCreator.class)
@LinkkiPositioned
@LinkkiAspect(NestedAspecDefinitionCreator.class)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
public @interface UINestedComponent {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI element */
    String label() default "";

    /**
     * Specifies the width of the field using a number and a CSS unit, for example "5em" or "50%".
     * <p>
     * This value is set to 100% by default, which means it takes up all available space.
     * 
     * @see Sizeable#setWidth(String)
     */
    String width() default "100%";

    static class NestedComponentDefinitionCreator implements ComponentDefinitionCreator<UINestedComponent> {
        @Override
        public LinkkiComponentDefinition create(UINestedComponent annotation, AnnotatedElement annotatedElement) {
            return pmo -> new CssLayout();
        }
    }

    static class NestedLayoutDefinitionCreator implements LayoutDefinitionCreator<UINestedComponent> {

        @Override
        public LinkkiLayoutDefinition create(UINestedComponent annotation, AnnotatedElement annotatedElement) {
            return (parentComponent, pmo, bindingContext) -> {
                CssLayout cssLayout = (CssLayout)parentComponent;
                Object nestedPmo = MemberAccessors.getValue(pmo, (Member)annotatedElement);
                Component component = (Component)ComponentAnnotationReader.findComponentDefinition(nestedPmo.getClass())
                        .get().createComponent(nestedPmo);

                component.setWidth(annotation.width());
                cssLayout.addComponent(component);
                cssLayout.setWidth(annotation.width());

                ContainerBinding containerBinding = bindingContext
                        .bindContainer(nestedPmo, BoundProperty.empty(),
                                       AspectAnnotationReader.createAspectDefinitionsFor(nestedPmo.getClass()),
                                       UiFramework.getComponentWrapperFactory().createComponentWrapper(component));

                LayoutAnnotationReader.findLayoutDefinition(nestedPmo.getClass()).get()
                        .createChildren(component, nestedPmo, containerBinding);
            };
        }
    }

    static class NestedAspecDefinitionCreator implements AspectDefinitionCreator<UINestedComponent> {

        @Override
        public LinkkiAspectDefinition create(UINestedComponent annotation) {
            return new LabelAspectDefinition(annotation.label());
        }

    }

}

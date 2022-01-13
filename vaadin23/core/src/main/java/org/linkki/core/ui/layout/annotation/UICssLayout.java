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

package org.linkki.core.ui.layout.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.EmptyPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.ComponentContainerLayoutDefinition.ComponentContainerLayoutDefinitionCreateor;
import org.linkki.core.ui.layout.annotation.UICssLayout.CssLayoutComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.Div;

// TODO LIN-2060
/***
 * Marks a group of components in a {@link Div}. It provides a simple DOM structure, which can be freely
 * styled by CSS. There is no predefined layouting information. Notable differences to other layouts are
 * <ul>
 * <li>Simpler DOM structure.
 * <li>Full developer control without the restriction of templates.
 * <li>Developer is responsible for cross-browser consistency
 * <li>Relative size calculations may differ in some situations
 * <li>Faster rendering in some situations
 * </ul>
 * To use this, annotate a class with {@link UICssLayout UICssLayout} that includes regular UI element
 * annotations like {@link UITextField @UITextField} on its methods. Call
 * {@link VaadinUiCreator#createComponent(Object, org.linkki.core.binding.BindingContext)} with an
 * instance of that class to create the {@link UICssLayout} and its children.
 * <p>
 * Use the {@link #styleNames()} attribute to designate one or more css classes for this component. If
 * no names are given, the Vaadin default CSS class name is "csslayout".
 * 
 * @implNote Uses {@link HasStyle#setClassName(String)} to set the style class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@LinkkiComponent(CssLayoutComponentDefinitionCreator.class)
@LinkkiLayout(ComponentContainerLayoutDefinitionCreateor.class)
@LinkkiBoundProperty(EmptyPropertyCreator.class)
public @interface UICssLayout {

    /**
     * It is recommended that you declare a CSS class in order to style this and contained elements.
     * These style names are statically applied to the layout. In order to get dynamic style names use
     * the functionality of {@link BindStyleNames @BindStyleNames}.
     */
    String[] styleNames() default {};

    public static class CssLayoutComponentDefinitionCreator implements ComponentDefinitionCreator<UICssLayout> {

        @Override
        public LinkkiComponentDefinition create(UICssLayout annotation, AnnotatedElement annotatedElement) {
            Div layoutCmpt = new Div();
            layoutCmpt.addClassNames(annotation.styleNames());
            return (pmo) -> layoutCmpt;
        }
    }

}

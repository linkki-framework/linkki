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

import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.ComponentContainerLayoutDefinition.ComponentContainerLayoutDefinitionCreateor;
import org.linkki.core.ui.layout.HorizontalLayoutComponentDefinition;
import org.linkki.core.ui.layout.VerticalAlignment;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout.HorizontalLayoutComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Marks a group of components horizontally placed next to each other in a {@link HorizontalLayout}.
 * <p>
 * To use this, annotate a class with {@link UIHorizontalLayout @UIHorizontalLayout} that includes
 * regular UI element annotations like {@link UITextField @UITextField} on its methods. Call
 * {@link VaadinUiCreator#createComponent(Object, org.linkki.core.binding.BindingContext)} with an
 * instance of that class to create the {@link HorizontalLayout} and its children.
 * <p>
 * You can adjust the {@link VerticalAlignment vertical alignment} of the children relative to each
 * other with the {@link #alignment()} attribute.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@LinkkiLayout(ComponentContainerLayoutDefinitionCreateor.class)
@LinkkiComponent(HorizontalLayoutComponentDefinitionCreator.class)
public @interface UIHorizontalLayout {

    /**
     * The {@link VerticalAlignment vertical alignment} of the {@link HorizontalLayout}'s children
     * relative to each other.
     */
    VerticalAlignment alignment() default VerticalAlignment.MIDDLE;

    /**
     * The state of{@link HorizontalLayout} padding. {@link HorizontalLayout#setPadding(boolean)}
     */
    boolean padding() default false;

    /**
     * The state of{@link HorizontalLayout} spacing. {@link HorizontalLayout#setSpacing(boolean)}
     */
    boolean spacing() default true;

    class HorizontalLayoutComponentDefinitionCreator implements ComponentDefinitionCreator<UIHorizontalLayout> {
        @Override
        public LinkkiComponentDefinition create(UIHorizontalLayout annotation, AnnotatedElement annotatedElement) {
            return new HorizontalLayoutComponentDefinition(annotation.alignment(),
                    annotation.spacing(), annotation.padding());
        }
    }
}

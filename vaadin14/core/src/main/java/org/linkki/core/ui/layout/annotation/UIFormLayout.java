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
import org.linkki.core.ui.layout.annotation.UIFormLayout.FormLayoutComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;

import com.vaadin.flow.component.formlayout.FormLayout;


/**
 * Marks a group of components vertically placed below each other in a two-column {@link FormLayout}
 * with captions to the left of the input fields.
 * <p>
 * To use this, annotate a class with {@link UIFormLayout @UIFormLayout} that includes regular UI
 * element annotations like {@link UITextField @UITextField} on its methods. Call
 * {@link VaadinUiCreator#createComponent(Object, org.linkki.core.binding.BindingContext)} with an
 * instance of that class to create the {@link FormLayout} and its children.
 */
@LinkkiLayout(ComponentContainerLayoutDefinitionCreateor.class)
@LinkkiComponent(FormLayoutComponentDefinitionCreator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UIFormLayout {

    class FormLayoutComponentDefinitionCreator implements ComponentDefinitionCreator<UIFormLayout> {
        @Override
        public LinkkiComponentDefinition create(UIFormLayout annotation, AnnotatedElement annotatedElement) {
            return pmo -> new FormLayout();
        }
    }
}

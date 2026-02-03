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
package org.linkki.framework.ui.component;

import static org.linkki.core.ui.creation.VaadinUiCreator.createComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.Binder;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

/**
 * Creates a {@link Headline} with a title and an optional PMO that is added to the title as well as an optional PMO
 * that is added at the end.
 *
 * @since 2.10.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@LinkkiComponent(UIHeadlineLayout.HeadlineComponentDefinitionCreator.class)
@LinkkiLayout(UIHeadlineLayout.HeadlineLayoutDefinitionCreator.class)
@LinkkiBoundProperty(BoundPropertyCreator.EmptyPropertyCreator.class)
@Inherited
public @interface UIHeadlineLayout {

    class HeadlineComponentDefinitionCreator implements ComponentDefinitionCreator<UIHeadlineLayout> {
        @Override
        public LinkkiComponentDefinition create(UIHeadlineLayout annotation, AnnotatedElement annotatedElement) {
            return pmo -> new Headline();
        }
    }

    class HeadlineLayoutDefinitionCreator implements LayoutDefinitionCreator<UIHeadlineLayout> {

        @Override
        public LinkkiLayoutDefinition create(UIHeadlineLayout annotation, AnnotatedElement annotatedElement) {
            return (parent, pmo, bindingContext) -> {
                var headline = (Headline)parent;
                var headlinePmo = (HeadlinePmo)pmo;
                if (headlinePmo.getTitleComponentPmo() != null) {
                    headline.addToTitle(createComponent(headlinePmo.getTitleComponentPmo(), bindingContext));
                }
                if (headlinePmo.getRightComponentPmo() != null) {
                    headline.getContent()
                            .addToEnd(createComponent(headlinePmo.getRightComponentPmo(), bindingContext));
                }
                new Binder(headline, headlinePmo).setupBindings(bindingContext);
            };
        }
    }

}

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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.EmptyPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.nls.PmoNlsService;
import org.linkki.core.ui.creation.section.SectionLayoutDefinition;
import org.linkki.core.ui.layout.annotation.UIFormSection.SectionComponentDefinitonCreator;
import org.linkki.core.ui.layout.annotation.UIFormSection.SectionLayoutDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.vaadin.component.section.BaseSection;
import org.linkki.core.vaadin.component.section.LinkkiSection;

/**
 * Responsible for creating a {@link BaseSection} in the UI from the annotated PMO class that may
 * include other UI elements.
 */
@LinkkiComponent(SectionComponentDefinitonCreator.class)
@LinkkiLayout(SectionLayoutDefinitionCreator.class)
@LinkkiBoundProperty(EmptyPropertyCreator.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface UIFormSection {

    /** The caption text for the section. */
    String caption() default "";

    /** Whether or not the section can be collapsed by the user. */
    boolean closeable() default false;

    /** Defines in how many columns the items should be displayed. */
    int columns() default 1;

    public static class SectionComponentDefinitonCreator implements ComponentDefinitionCreator<UIFormSection> {

        @Override
        public LinkkiComponentDefinition create(UIFormSection uiFormSection, AnnotatedElement annotatedElement) {
            return pmo -> {
                BaseSection baseSection = new BaseSection(
                        PmoNlsService.get().getSectionCaption(pmo.getClass(), uiFormSection.caption()),
                        uiFormSection.closeable(), uiFormSection.columns());
                baseSection.getElement().getThemeList().add(LinkkiSection.THEME_VARIANT_FORM);
                return baseSection;
            };
        }
    }

    public static class SectionLayoutDefinitionCreator implements LayoutDefinitionCreator<UIFormSection> {

        @Override
        public LinkkiLayoutDefinition create(UIFormSection annotation, AnnotatedElement annotatedElement) {
            return SectionLayoutDefinition.DEFAULT;
        }

    }
}

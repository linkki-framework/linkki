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

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.EmptyPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.ui.creation.section.SectionComponentDefiniton;
import org.linkki.core.ui.creation.section.SectionLayoutDefinition;
import org.linkki.core.ui.layout.annotation.UISection.SectionComponentDefinitonCreator;
import org.linkki.core.ui.layout.annotation.UISection.SectionLayoutDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import com.vaadin.shared.ui.grid.GridConstants.Section;

/**
 * Responsible for creating a {@link Section} in the UI from the annotated PMO class that may include
 * other UI-Elements.
 */
@LinkkiComponent(SectionComponentDefinitonCreator.class)
@LinkkiLayout(SectionLayoutDefinitionCreator.class)
@LinkkiBoundProperty(EmptyPropertyCreator.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface UISection {

    /**
     * Layout for the section, i.e. whether fields are displayed horizontally or in vertical columns.
     */
    SectionLayout layout() default SectionLayout.COLUMN;

    /**
     * Number of columns if the {@link SectionLayout#COLUMN} layout is used. Ignored if an other layout
     * is used.
     */
    int columns() default 1;

    /** The caption text for the section. */
    String caption() default "";

    /** Whether or not the section can be collapsed by the user. */
    boolean closeable() default false;

    /** Defines the order in which UI-Elements are displayed. */
    int position() default 0;

    public static class SectionComponentDefinitonCreator implements ComponentDefinitionCreator {

        @Override
        public LinkkiComponentDefinition create(Annotation annotation, AnnotatedElement annotatedElement) {
            UISection uiSection = (UISection)annotation;
            return new SectionComponentDefiniton(uiSection.layout(), uiSection.caption(), uiSection.closeable(),
                    uiSection.columns(), uiSection.position());
        }

    }

    public static class SectionLayoutDefinitionCreator implements LayoutDefinitionCreator {

        @Override
        public LinkkiLayoutDefinition create(Annotation annotation, AnnotatedElement annotatedElement) {
            return SectionLayoutDefinition.DEFAULT;
        }

    }
}

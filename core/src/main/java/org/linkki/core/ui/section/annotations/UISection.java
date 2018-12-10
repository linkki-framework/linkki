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
package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.shared.ui.grid.GridConstants.Section;

/**
 * Responsible for creating a {@link Section} in the UI from the annotated PMO class that may
 * include other UI-Elements.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UISection {

    /** Layout for the section, i.e. if fields are displayed horizontally or in vertical columns. */
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

}

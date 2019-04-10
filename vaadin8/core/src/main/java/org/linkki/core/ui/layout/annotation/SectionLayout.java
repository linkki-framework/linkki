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

import org.linkki.core.ui.component.section.CustomLayoutSection;

/**
 * The options for the layout pattern of a section
 */
public enum SectionLayout {

    /** Displays section elements next to each other in a row */
    HORIZONTAL,

    /** Displays section elements stacked in columns */
    COLUMN,

    /**
     * Displays the section using a {@link CustomLayoutSection}.
     * <p>
     * With the {@link CustomLayoutSection} you need to specify a HTML file that defines the layout.
     * <p>
     * The name of the HTML file is the simple name of your PMO class. It has to be located in your
     * theme folder in the subfolder layouts, for example
     * <code>src/main/webapp/VAADIN/themes/valo/layouts</code>. The HTML file should contain
     * <code>div</code> elements with the attribute <code>location</code> as placeholder for the labels
     * and components. Use the PMO property name as placeholder name for the component and the PMO
     * property name with the suffix <code>-label</code> as placeholder name for the label.
     */
    CUSTOM;

}

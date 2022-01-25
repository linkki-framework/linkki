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

import java.util.Arrays;
import java.util.List;

import org.linkki.core.ui.creation.section.SectionLayoutDefinition;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.vaadin.component.section.LinkkiSection;

/**
 * The options for the layout pattern of a section
 */
public enum SectionLayout {

    /**
     * Displays section elements next to each other in a row. Labels are displayed on top of the
     * components.
     * <p>
     * Note that with this option, several components such as {@link UIButton}, {@link UICheckBox},
     * {@link UILabel} and {@link UILink} do not support labels.
     */
    HORIZONTAL(SectionLayoutDefinition.LABEL_ON_TOP, LinkkiSection.THEME_VARIANT_HORIZONTAL),

    /**
     * Displays section elements stacked in a column. Labels are displayed on the left of the
     * components.
     * <p>
     * <em>Consider using {@link UIFormSection} instead.</em>
     * 
     * @deprecated Use {@link #FORM} instead
     **/
    @Deprecated(since = "2.0.0")
    COLUMN(SectionLayoutDefinition.DEFAULT),

    /**
     * Displays section elements stacked in a column. Labels are displayed on the left of the
     * components.
     * <p>
     * <em>Consider using {@link UIFormSection} instead.</em>
     **/
    FORM(SectionLayoutDefinition.DEFAULT),

    /**
     * Displays section elements stacked in a column. Labels are displayed on top of the component.
     * <p>
     * Note that with this option, several components such as {@link UIButton}, {@link UICheckBox},
     * {@link UILabel} and {@link UILink} do not support labels.
     */
    VERTICAL(SectionLayoutDefinition.LABEL_ON_TOP);

    private final SectionLayoutDefinition sectionLayoutDefinition;
    private final String[] themeNames;

    private SectionLayout(SectionLayoutDefinition sectionLayoutDefinition,
            String... themeNames) {
        this.sectionLayoutDefinition = sectionLayoutDefinition;
        this.themeNames = themeNames;
    }

    public SectionLayoutDefinition getSectionLayoutDefinition() {
        return sectionLayoutDefinition;
    }

    public List<String> getThemeNames() {
        return Arrays.asList(themeNames);
    }

}

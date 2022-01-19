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

import org.linkki.core.ui.creation.section.SectionLayoutDefinition;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;

import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;

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
    HORIZONTAL(FlexDirection.ROW, SectionLayoutDefinition.LABEL_ON_TOP),

    /**
     * Displays section elements stacked in a column. Labels are displayed on the left of the
     * components.
     * <p>
     * <em>Consider using {@link UIFormSection} instead.</em>
     **/
    COLUMN(FlexDirection.COLUMN, SectionLayoutDefinition.DEFAULT);

    private final FlexDirection flexDirection;
    private final SectionLayoutDefinition sectionLayoutDefinition;

    private SectionLayout(FlexDirection flexDirection, SectionLayoutDefinition sectionLayoutDefinition) {
        this.flexDirection = flexDirection;
        this.sectionLayoutDefinition = sectionLayoutDefinition;
    }

    public FlexDirection getFlexDirection() {
        return flexDirection;
    }

    public SectionLayoutDefinition getSectionLayoutDefinition() {
        return sectionLayoutDefinition;
    }

}

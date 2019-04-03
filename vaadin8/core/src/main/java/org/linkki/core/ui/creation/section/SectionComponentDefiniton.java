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

package org.linkki.core.ui.creation.section;

import java.util.Optional;

import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.nls.PmoNlsService;
import org.linkki.core.ui.component.section.BaseSection;
import org.linkki.core.ui.component.section.CustomLayoutSection;
import org.linkki.core.ui.component.section.FormSection;
import org.linkki.core.ui.component.section.HorizontalSection;
import org.linkki.core.ui.layout.annotation.SectionLayout;

import com.vaadin.ui.Button;

/**
 * Defines how {@link BaseSection} instances are created.
 * 
 * @see SectionLayoutDefinition SectionLayoutDefinition for how the section is poulated with UI
 *      components
 */
public class SectionComponentDefiniton implements LinkkiComponentDefinition {

    public static final SectionComponentDefiniton DEFAULT = new SectionComponentDefiniton(SectionLayout.COLUMN, "",
            false, 1, 0);

    private final SectionLayout layout;
    private final String caption;
    private final boolean closeable;
    private final int columns;
    private final int position;

    public SectionComponentDefiniton(SectionLayout layout, String caption, boolean closeable, int columns,
            int position) {
        super();
        this.layout = layout;
        this.caption = caption;
        this.closeable = closeable;
        this.columns = columns;
        this.position = position;
    }

    @Override
    public BaseSection createComponent(Object pmo) {
        Optional<Button> editButton = Optional.empty();
        // wird erst in SectionLayoutDefinition#createChildren erzeugt, bleibt aber erst mal Teil der
        // API der Sections
        String nlsCaption = PmoNlsService.get().getSectionCaption(pmo.getClass(), this.caption);
        switch (layout) {
            case COLUMN:
                return new FormSection(nlsCaption,
                        closeable,
                        editButton,
                        columns);
            case HORIZONTAL:
                return new HorizontalSection(nlsCaption,
                        closeable,
                        editButton);
            case CUSTOM:
                return new CustomLayoutSection(pmo.getClass().getSimpleName(), nlsCaption,
                        closeable,
                        editButton);
            default:
                throw new IllegalStateException("unknown SectionLayout#" + layout);
        }
    }

    @Override
    public int getPosition() {
        return position;
    }

}

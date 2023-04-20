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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.dispatcher.staticvalue.StaticValueNlsService;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.vaadin.component.section.BaseSection;
import org.linkki.core.vaadin.component.section.GridSection;
import org.linkki.core.vaadin.component.section.LinkkiSection;

/**
 * Defines how {@link LinkkiSection sections} are created.
 * 
 * @see SectionLayoutDefinition SectionLayoutDefinition for how the section is poulated with UI
 *      components
 */
public class SectionComponentDefiniton implements LinkkiComponentDefinition {

    public static final SectionComponentDefiniton DEFAULT = new SectionComponentDefiniton(SectionLayout.FORM, "",
            false);

    private final SectionLayout layout;
    private final String caption;
    private final boolean closeable;

    public SectionComponentDefiniton(SectionLayout layout, String caption, boolean closeable) {
        this.layout = layout;
        this.caption = caption;
        this.closeable = closeable;
    }

    @Override
    public Object createComponent(Object pmo) {
        return createComponent(pmo.getClass());
    }

    private Object createComponent(Class<?> pmoClass) {
        var nlsCaption = StaticValueNlsService.getInstance().getString(pmoClass, StringUtils.EMPTY,
                                                               StaticValueNlsService.CAPTION_KEY, this.caption);

        if (ContainerPmo.class.isAssignableFrom(pmoClass)) {
            return createTableSection(nlsCaption);
        } else {
            BaseSection baseSection = new BaseSection(nlsCaption, closeable);
            baseSection.getElement().getThemeList().addAll(layout.getThemeNames());
            return baseSection;
        }
    }

    private GridSection createTableSection(String nlsCaption) {
        return new GridSection(nlsCaption, closeable);
    }

}

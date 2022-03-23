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
package org.linkki.core.ui.theme;

import com.vaadin.flow.theme.Theme;

/**
 * Constants for names and CSS class constants used by default linkki theme.
 */
public class LinkkiTheme {

    /**
     * The name of the linkki theme name. Should be used in {@link Theme @Theme} annotation.
     */
    public static final String LINKKI_THEME = "linkki";

    /**
     * Applies a card style to all LinkkiSections inside of AbstractPages.
     */
    public static final String VARIANT_CARD_SECTION_PAGES = "card-section-pages";

    /**
     * A theme variant for smaller input fields and less space.
     */
    public static final String VARIANT_COMPACT = "compact";

    /** Style for the caption of a section. */
    public static final String SECTION_CAPTION_TEXT = "linkki-section-caption-text";

    /** Style for the label part of a wrapped component */
    public static final String COMPONENTWRAPPER_LABEL = "linkki-label-caption";

    /** Style for tables created by linkki */
    public static final String TABLE = "linkki-table";

    /** A component that contains an icon **/
    public static final String HAS_ICON = "linkki-has-icon";

    private LinkkiTheme() {
        // prevent initialization
    }

}

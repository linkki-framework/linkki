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
package org.linkki.core.ui.theme;

import org.linkki.core.vaadin.component.base.LinkkiText;

import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

/**
 * Constants for names and CSS class constants used by default linkki theme.
 */
public class LinkkiTheme {

    /**
     * The name of the linkki theme name. Should be used in {@link Theme @Theme} annotation.
     */
    public static final String LINKKI_THEME = "linkki";

    /**
     * Applies {@link org.linkki.core.vaadin.component.section.LinkkiSection#THEME_VARIANT_CARD} to
     * all contained sections.
     */
    public static final String VARIANT_CARD_SECTIONS = "card-sections";

    /** A theme variant for smaller input fields and less space. */
    public static final String VARIANT_COMPACT = "compact";

    /** Style for the caption of a section. */
    public static final String SECTION_CAPTION_TEXT = "linkki-section-caption-text";

    /** Style for the label part of a wrapped component */
    public static final String COMPONENTWRAPPER_LABEL = "linkki-label-caption";

    /** Style for tables created by linkki */
    public static final String TABLE = "linkki-table";

    /** Style class for table footer that should be bold. */
    public static final String GRID_FOOTER_BOLD = "linkki-grid-footer-bold";

    /**
     * A component that contains an icon
     *
     * @deprecated the style class is not used by linkki anymore and will be removed.
     **/
    @Deprecated(since = "2.1.1")
    public static final String HAS_ICON = "linkki-has-icon";

    /**
     * Style class for labels that should look like placeholders in table sections.
     *
     * @deprecated Use {@link Text#TEXT_PLACEHOLDER} instead.
     **/
    @Deprecated(since = "2.5.0")
    public static final String PLACEHOLDER_LABEL = "placeholder-label";

    private LinkkiTheme() {
        // prevent instantiation
    }

    /**
     * Style classes for {@link LinkkiText labels}. Additional text styles can be found in
     * {@link LumoUtility.TextColor}.
     *
     * @implNote The purpose of this utility class is to consolidate styling constants for the
     *           {@link LinkkiText} component. Future enhancements or modifications related to
     *           {@link LinkkiText} styling should also be added here.
     *           <p>
     *           This class is placed here instead of LinkkiText to make it easier to find.
     */
    public static final class Text {

        /** Makes a label look like a placeholder in table sections. */
        public static final String TEXT_PLACEHOLDER = "text-placeholder";

        /**
         * Show icon of a {@link LinkkiText} in the success color. For success colored text, use
         * {@link TextColor#SUCCESS}.
         */
        public static final String ICON_SUCCESS = "icon-success";

        /**
         * Show icon of a {@link LinkkiText} in the warning color. For warning colored text, use
         * {@link TextColor#WARNING}.
         */
        public static final String ICON_WARNING = "icon-warning";

        /**
         * Show icon of a {@link LinkkiText} in the error color. For error colored text, use
         * {@link TextColor#ERROR}.
         */
        public static final String ICON_ERROR = "icon-error";

        /**
         * Show icon in the info color.
         */
        public static final String ICON_INFO = "icon-info";

        /**
         * Show text and icon in the info color.
         */
        public static final String TEXT_INFO = "text-info";

        private Text() {
            // prevent instantiation
        }
    }
}

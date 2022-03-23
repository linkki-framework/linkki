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
package org.linkki.core.defaults.style;

/**
 * Constants for the CSS styles used throughout the application.
 * 
 * @deprecated since 2.0 use framework specific LinkkiTheme class, for example
 *             org.linkki.core.ui.theme.LinkkiTheme
 */
@Deprecated
public class LinkkiTheme {

    /**
     * Applies a card style to all LinkkiSections inside of AbstractPages.
     */
    public static final String VARIANT_CARD_SECTION_PAGES = "card-section-pages";

    /** Style for a horizontal spacer */
    public static final String HORIZONTAL_SPACER = "linkki-horizontal-spacer";

    /**
     * Style for the section layout component
     * 
     * @deprecated Use the tag "linkki-section" to select sections instead.
     */
    @Deprecated(since = "2.0.0")
    public static final String SECTION = "linkki-section";

    /**
     * @deprecated This class is no longer applied to any parts of a section.
     */
    @Deprecated(since = "2.0.0")
    public static final String SECTION_CAPTION = "linkki-section-caption";

    /** Style for the caption of a section. */
    public static final String SECTION_CAPTION_TEXT = "linkki-section-caption-text";

    /**
     * @deprecated This class is no longer applied to any parts of a section.
     */
    @Deprecated(since = "2.0.0")
    public static final String SECTION_CAPTION_LINE = "linkki-section-caption-line";

    /** Style for the label part of a wrapped component */
    public static final String COMPONENTWRAPPER_LABEL = "linkki-label-caption";
    public static final String LABEL_FIXED_WIDTH = "linkki-label-fixed-width";
    public static final String LABEL_FIXED_WIDTH_LONG = "linkki-label-fixed-width-long";

    /**
     * Marks labels of label component wrappers to show a required indicator.
     */
    public static final String REQUIRED_LABEL_COMPONENT_WRAPPER = "linkki-required";

    /**
     * Style for components that should be rendered without a border, for example components in tables
     */
    public static final String BORDERLESS = "borderless";

    /** Style for tables created by linkki */
    public static final String TABLE = "linkki-table";

    /** Style for a single cell within a linkki table */
    public static final String TABLE_CELL = "linkki-table-cell";

    /** A button that is styled as a text, without additional height. **/
    public static final String BUTTON_TEXT = "linkki-button-text";

    /** A component that contains an icon **/
    public static final String HAS_ICON = "linkki-has-icon";

    private LinkkiTheme() {
        // prevent initialization
    }

}

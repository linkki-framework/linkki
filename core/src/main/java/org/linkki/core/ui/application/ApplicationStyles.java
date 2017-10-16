/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.application;

import org.linkki.core.ui.section.AbstractSection;

/**
 * Constant for the CSS styles used throughout the application.
 */
public class ApplicationStyles {

    /**
     * Style for the header section of the application.
     */
    public static final String APPLICATION_HEADER = "linkki-application-header";

    /**
     * Style for the menu section of the application.
     */
    public static final String MENU_WRAPPER = "linkki-menu-wrapper";

    public static final String HORIZONTAL_SPACER = "linkki-horizontal-spacer";

    public static final String FIXED_HORIZONTAL_SPACER = "linkki-fixed-horizontal-spacer";

    /**
     * Style for the top-level items of the application's main menu in the header.
     */
    public static final String APPLICATION_MENU = "linkki-application-menu";

    /**
     * Style for the caption/header of a modal dialog.
     */
    public static final String DIALOG_CAPTION = "linkki-dialog-caption";

    /**
     * Style for the body of a modal dialog.
     */
    public static final String DIALOG_CONTENT = "linkki-dialog-content";

    /**
     * Style for the caption of a section.
     * 
     * @see AbstractSection
     */
    public static final String SECTION_CAPTION = "linkki-section-caption";

    /**
     * Style for the caption of a section.
     * 
     * @see AbstractSection
     */
    public static final String SECTION_CAPTION_TEXT = "linkki-section-caption-text";

    /**
     * Style for the line separating the section's header/caption from the body/content.
     * 
     * @see AbstractSection
     */
    public static final String SECTION_CAPTION_LINE = "linkki-section-caption-line";
    /**
     * Style for the line separating the section's header/caption from the body/content.
     * 
     * @see AbstractSection
     */
    public static final String LOGIN_PANEL_CAPTION = "linkki-login-panel-caption";

    public static final String LOGIN_PANEL = "linkki-login-panel";

    public static final String BORDERLESS = "borderless";

    public static final String TABLE = "linkki-table";

    public static final String TABLE_CELL = "linkki-table-cell";

    public static final String SPACING_HORIZONTAL_SECTION = "horizontal-section-spacing";

    public static final String DIALOG_BUTTON_BAR = "linkki-dialog-button-bar";

    public static final String MESSAGE_ROW = "linkki-message-row";

    private ApplicationStyles() {
        // prevent initialization
    }

}

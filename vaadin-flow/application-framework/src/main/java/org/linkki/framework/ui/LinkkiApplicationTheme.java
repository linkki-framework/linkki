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
package org.linkki.framework.ui;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.framework.ui.application.ApplicationFooter;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.component.Headline;
import org.linkki.framework.ui.component.MessageUiComponents;
import org.linkki.framework.ui.dialogs.ApplicationInfoDialog;
import org.linkki.framework.ui.dialogs.OkCancelDialog;

public final class LinkkiApplicationTheme {

    /**
     * Style for the {@link ApplicationHeader header} of the application.
     */
    public static final String APPLICATION_HEADER = "linkki-application-header";

    /** Style class for elements in {@link ApplicationHeader} that are right-aligned */
    public static final String APPLICATION_HEADER_RIGHT = "linkki-application-header-right";

    /**
     * Style for the top-level items of the application's main menu in the header.
     */
    public static final String APPLICATION_MENU = "linkki-application-menu";

    /**
     * Style for the {@link ApplicationFooter footer} of the application.
     */
    public static final String APPLICATION_FOOTER = "linkki-application-footer";

    /** Style for the caption/header of a modal {@link OkCancelDialog dialog}. */
    public static final String DIALOG_CAPTION = "linkki-dialog-caption";

    /** Style for the body of a modal {@link OkCancelDialog dialog}. */
    public static final String DIALOG_CONTENT = "linkki-dialog-content";

    /** Style for the layout that contains the buttons in a dialog */
    public static final String DIALOG_BUTTON_BAR = "linkki-dialog-button-bar";

    /**
     * Prefix for {@link Message} label style classes. The errorLevel in lower case is used as suffix
     *
     * @deprecated Use icon styles in {@link org.linkki.core.ui.theme.LinkkiTheme.Text} instead.
     */
    @Deprecated(since = "2.5.0")
    public static final String MESSAGE_PREFIX = "linkki-message-"; // $NON-NLS-1$

    /** Style class prefix {@link Message} labels. The errorLevel in lower case is used as suffix */
    public static final String MESSAGE_LIST_STYLE = "linkki-message-list"; // $NON-NLS-1$

    /**
     * Style for a label that represents a single {@link Message}, created by
     * {@link MessageUiComponents#createMessageComponent(Message)}.
     */
    public static final String MESSAGE_LABEL = "linkki-message-label";

    /** Style for a table that shows a {@link MessageList}, created by {@link MessageUiComponents}. */
    public static final String MESSAGE_TABLE = "linkki-message-table";

    /** Style for a row in a message table, @see {@link #MESSAGE_TABLE} */
    public static final String MESSAGE_ROW = "linkki-message-row";

    /** Style class for {@link Headline} */
    public static final String HEADLINE = "linkki-headline"; // $NON-NLS-1$

    /** Style class for {@link ApplicationInfoDialog} */
    public static final String APPLICATION_INFO_DIALOG = "linkki-application-info-dialog"; // $NON-NLS-1$

    /**
     * Style class for table footer that may have sums and should be right aligned and bold.
     * 
     * @deprecated Use {@link #GRID_FOOTER_BOLD} instead. Additionally,
     *             {@link UITableColumn#textAlign()} must be set to {@link TextAlignment#RIGHT} for
     *             right-alignment.
     */
    @Deprecated(since = "2.0.0")
    public static final String GRID_FOOTER_SUM = "linkki-grid-footer-sum"; // $NON-NLS-1$

    /**
     * Style class for table footer that should be bold.
     * 
     * @deprecated Use {@link LinkkiTheme#GRID_FOOTER_BOLD} instead.
     */
    @Deprecated(since = "2.5.0")
    public static final String GRID_FOOTER_BOLD = "linkki-grid-footer-bold"; // $NON-NLS-1$

    /**
     * Style class for {@link UILabel}s, that should be right aligned.
     * <p>
     * For tables, {@link UITableColumn#textAlign()} should be used instead.
     *
     * @deprecated Use {@link com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment#RIGHT} instead.
     */
    @Deprecated(since = "2.5.0")
    public static final String TEXT_RIGHT_ALIGNED = "linkki-right-aligned";

    private LinkkiApplicationTheme() {
        // This is just a utility class used to list constants and therefore should not be
        // instantiated
    }


}

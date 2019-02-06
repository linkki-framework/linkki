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
package org.linkki.framework.ui;

import org.linkki.core.message.Message;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.component.Headline;
import org.linkki.framework.ui.component.sidebar.SidebarLayout;
import org.linkki.framework.ui.dialogs.ApplicationInfoDialog;
import org.linkki.framework.ui.dialogs.OkCancelDialog;

public final class LinkkiStyles {

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

    /** Style class for {@link SidebarLayout} */
    public static final String SIDEBAR_LAYOUT = "linkki-sidebar-layout"; //$NON-NLS-1$

    /** Style class for the sidebar of {@link SidebarLayout} */
    public static final String SIDEBAR = "linkki-sidebar"; //$NON-NLS-1$

    /** Style class for selected icons in the sidebar of {@link SidebarLayout} */
    public static final String SIDEBAR_SELECTED = "selected"; //$NON-NLS-1$

    /** Style class for the content in {@link SidebarLayout} */
    public static final String SIDEBAR_CONTENT = "linkki-sidebar-content"; //$NON-NLS-1$

    /** Style for the caption/header of a modal {@link OkCancelDialog dialog}. */
    public static final String DIALOG_CAPTION = "linkki-dialog-caption";

    /** Style for the body of a modal {@link OkCancelDialog dialog}. */
    public static final String DIALOG_CONTENT = "linkki-dialog-content";

    /**
     * Prefix for {@link Message} label style classes. The errorLevel in lower case is used as
     * suffix
     */
    public static final String MESSAGE_PREFIX = "linkki-message-"; //$NON-NLS-1$

    /** Style class prefix {@link Message} labels. The errorLevel in lower case is used as suffix */
    public static final String MESSAGE_LIST_STYLE = "linkki-message-list"; //$NON-NLS-1$

    /** Style class for {@link Headline} */
    public static final String HEADLINE = "linkki-headline"; //$NON-NLS-1$

    /** Style class for {@link ApplicationInfoDialog} */
    public static final String APPLICATION_INFO_DIALOG = "linkki-application-info-dialog"; //$NON-NLS-1$

    private LinkkiStyles() {
        // This is just a utility class used to list constants and therefore should not be
        // instantiated
    }


}

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
package org.linkki.framework.ui.application;

import javax.enterprise.inject.Specializes;

import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.framework.ui.LinkkiStyles;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.nls.NlsText;

import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * An empty header bar that is displayed on the top of an {@link ApplicationFrame}.
 * <p>
 * This header can be extended to include further functionalities such as an {@link ApplicationMenu}
 * or information about the user. The specialized subclass must be annotated with
 * {@link Specializes} to take effect as this class is injected by {@link ApplicationFrame}.
 * <p>
 * Elements that should be right aligned in the {@link ApplicationHeader} can be wrapped in a layout
 * that uses the style name {@link LinkkiStyles#APPLICATION_HEADER_RIGHT}.
 */
@UIScoped
public class ApplicationHeader extends HorizontalLayout {

    private static final long serialVersionUID = 1L;

    /**
     * Initialize UI components.
     */
    protected void init() {
        addStyleName(LinkkiStyles.APPLICATION_HEADER);
        setMargin(new MarginInfo(true, false, true, false));
    }

    /**
     * Creates a {@link Command} that can be used for a logout {@link MenuItem item} in a
     * {@link MenuBar}.
     */
    protected Command newLogoutCommand() {
        return selectedItem -> {
            getUI().getPage().setLocation("./logout"); //$NON-NLS-1$
        };
    }

    protected void createHelpSection(HorizontalLayout menuWrapper) {
        MenuBar menuBar = new MenuBar();
        menuBar.setSizeUndefined();
        menuBar.addStyleName(ApplicationStyles.BORDERLESS);

        MenuItem item = menuBar.addItem("", FontAwesome.QUESTION_CIRCLE, null); //$NON-NLS-1$
        item.addItem(NlsText.getString("ApplicationHeader.Help"), null); //$NON-NLS-1$

        menuWrapper.addComponent(menuBar);
    }
}

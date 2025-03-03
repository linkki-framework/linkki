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
package org.linkki.framework.ui.application;

import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.server.Command;

/**
 * {@link ApplicationHeader} containing a user menu item.
 * <p>
 * The class is abstract to implement the necessary details like {@link #getUsername()}.
 *
 * @since 2.8.0
 */
public abstract class UserAwareApplicationHeader extends ApplicationHeader {

    private static final long serialVersionUID = 1L;
    private static final String APPMENU_USER_ID = "appmenu-user";
    private static final String APPMENU_LOGOUT_ID = "appmenu-logout";
    private static final String LOGOUT = "logout";
    private static final String APPLICATIONHEADER_LOGOUT = "ApplicationHeader.Logout";

    protected UserAwareApplicationHeader(ApplicationInfo applicationInfo,
            Sequence<ApplicationMenuItemDefinition> menuItemDefinitions) {
        super(applicationInfo, menuItemDefinitions);
    }

    /**
     * The property is used to display the current user in the {@link ApplicationHeader}.
     *
     * Returns the name of the current user.
     */
    public abstract String getUsername();

    @Override
    protected MenuBar createRightMenuBar() {
        var menuBar = super.createRightMenuBar();
        addUserMenu(menuBar);
        return menuBar;
    }

    /**
     * Creates a user menu item in the given parent.
     *
     * @apiNote Override {@link #addUserMenuItems(MenuItem)} to add sub menu items to the created
     *          user menu.
     */
    protected MenuItem addUserMenu(MenuBar parent) {
        var userMenu = parent.addItem(VaadinIcon.USER.create());
        userMenu.add(getUsername());
        userMenu.setId(APPMENU_USER_ID);
        addUserMenuItems(userMenu);
        return userMenu;
    }

    /**
     * Adds sub menu items to the given <code>userMenu</code> that is created in
     * {@link #addUserMenu(MenuBar)}.
     */
    protected void addUserMenuItems(MenuItem userMenu) {
        var logoutMenuItem = userMenu.getSubMenu().addItem(NlsText.getString(APPLICATIONHEADER_LOGOUT),
                                                           newLogoutCommand());
        logoutMenuItem.setId(APPMENU_LOGOUT_ID);
    }

    /**
     * Returns a {@link Command} that can be used for a logout {@link MenuItem item} in a
     * {@link MenuBar}.
     */
    protected ComponentEventListener<ClickEvent<MenuItem>> newLogoutCommand() {
        return e -> getUI()
                .ifPresent(ui -> ui.getPage().setLocation(getLogoutUrl()));
    }

    /**
     * Returns the logoutUrl. The default path is contextRootRelativePath + /logout
     *
     * @apiNote Override {@link #getLogoutUrl()} to customize the redirection to the logoutUrl.
     */
    protected String getLogoutUrl() {
        return getUI().map(ui -> ui.getInternals().getContextRootRelativePath() + LOGOUT).orElse("");
    }

}

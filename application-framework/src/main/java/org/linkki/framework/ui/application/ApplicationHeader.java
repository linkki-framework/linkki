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

import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.LinkkiStyles;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.dialogs.ApplicationInfoDialog;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.framework.ui.pmo.ApplicationInfoPmo;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A navigation header bar that is displayed on the top of an {@link ApplicationLayout}. By default,
 * it consists of the {@link ApplicationMenu} on the left as well as a right aligned
 * {@link MenuBar}.
 * <p>
 * {@link ApplicationHeader} can be styled with the CSS class
 * {@link LinkkiStyles#APPLICATION_HEADER}. The right aligned {@link MenuBar} uses the style name
 * {@link LinkkiStyles#APPLICATION_HEADER_RIGHT} additionally.
 * 
 * @implNote The methods {@link #createRightMenuBar()}, {@link #addHelpMenu(MenuBar)} and
 *           {@link #addHelpMenuItems(MenuItem)} can be used to create an
 *           {@link ApplicationInfoDialog} menu item in the right {@link MenuBar}.
 *           <p>
 *           The methods {@link #createRightMenuBar()}, {@link #addUserMenu(String, MenuBar)},
 *           {@link #addUserMenuItems(MenuItem)} and {@link #newLogoutCommand()} can be used to
 *           create a user logout menu item in the right {@link MenuBar}.
 * 
 * @see ApplicationMenu
 */
public class ApplicationHeader extends HorizontalLayout {

    private static final long serialVersionUID = 1L;

    private final ApplicationMenu applicationMenu;

    public ApplicationHeader(ApplicationMenu applicationMenu) {
        this.applicationMenu = applicationMenu;
        addStyleName(LinkkiStyles.APPLICATION_HEADER);
        setMargin(new MarginInfo(true, false, true, false));
    }

    /**
     * Initialize UI components.
     * 
     * @implSpec Avoid overriding this method if possible. By default, this method assumes that the
     *           header consists of a left floating and a right floating group of UI components.
     *           These elements are added by {@link #addLeftComponents()} and
     *           {@link #addRightComponents()} respectively.
     */
    protected void init() {
        addLeftComponents();
        addRightComponents();
    }

    /**
     * Adds navigation elements on the left of the header. Adds the {@link ApplicationMenu} by
     * default.
     * 
     * @implSpec Only override this method if you do not need the {@link ApplicationMenu} or need to
     *           add elements other than {@link ApplicationMenu}. The {@link ApplicationMenu} itself
     *           is passed to this {@link ApplicationHeader}'s constructor.
     * 
     * @see ApplicationMenu for further information about its configuration.
     */
    protected void addLeftComponents() {
        addComponent(applicationMenu);
    }

    /**
     * Adds right aligned navigation elements in the header. Adds a {@link MenuBar} and calls
     * {@link #createRightMenuBar()} to add items by default.
     * 
     * @implSpec Override {@link #createRightMenuBar()} to add elements to the {@link MenuBar}.
     */
    protected void addRightComponents() {
        MenuBar rightMenuBar = createRightMenuBar();
        addComponent(rightMenuBar);
        setExpandRatio(rightMenuBar, 1);
    }

    /**
     * Creates a right aligned {@link MenuBar} that is added to the {@link ApplicationHeader} by
     * {@link #addRightComponents()}.
     * 
     * @implNote The created {@link MenuBar} contains a {@link #addHelpMenu(MenuBar) help menu item}
     *           by default.
     */
    protected MenuBar createRightMenuBar() {
        MenuBar rightMenuBar = new MenuBar();

        rightMenuBar.setStyleName(LinkkiStyles.APPLICATION_HEADER_RIGHT);
        rightMenuBar.setSizeUndefined();
        rightMenuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        rightMenuBar.addStyleName(ValoTheme.MENUBAR_SMALL);

        addHelpMenu(rightMenuBar);

        return rightMenuBar;
    }

    /**
     * Creates a help menu item in the given parent.
     * 
     * @implSpec Override {@link #addHelpMenuItems(MenuItem)} to add sub menu items to the created
     *           {@link #addHelpMenu(MenuBar) help menu item}.
     */
    protected MenuItem addHelpMenu(MenuBar parent) {
        MenuItem helpMenu = parent.addItem("", FontAwesome.QUESTION_CIRCLE, null); // $NON-NLS-1$
        addHelpMenuItems(helpMenu);
        return helpMenu;
    }

    /**
     * Adds by default the {@link ApplicationInfoDialog about sub menu item} created by
     * {@link #addApplicationInfoMenuItem(MenuItem)} to the given <code>helpMenu</code> that is
     * created in {@link #addHelpMenu(MenuBar)}.
     * 
     * @implSpec Override to add more sub menu items.
     *           <p>
     *           Extend {@link ApplicationInfoPmo} and override
     *           {@link #createApplicationInfoPmo(ApplicationConfig)} to customize the created
     *           {@link ApplicationInfoDialog}.
     */
    protected void addHelpMenuItems(MenuItem helpMenu) {
        addApplicationInfoMenuItem(helpMenu);
    }

    /**
     * Adds an {@link ApplicationInfoDialog about sub menu item} to the given <code>helpMenu</code>
     * that is created in {@link #addHelpMenu(MenuBar)}.
     */
    protected void addApplicationInfoMenuItem(MenuItem helpMenu) {
        helpMenu.addItem(NlsText.getString("ApplicationHeader.Info"), FontAwesome.INFO_CIRCLE, //$NON-NLS-1$
                         i -> new ApplicationInfoDialog(
                                 createApplicationInfoPmo(LinkkiUi.getCurrentApplicationConfig())).open());
    }

    /**
     * Creates the {@link ApplicationInfoPmo}.
     * 
     * @see ApplicationInfoPmo for further information about its configuration.
     */
    protected ApplicationInfoPmo createApplicationInfoPmo(ApplicationConfig config) {
        return new ApplicationInfoPmo(config);
    }

    /**
     * Creates a user menu item in the given parent.
     * 
     * @implSpec Override {@link #addUserMenuItems(MenuItem)} to add sub menu items to the created
     *           user menu.
     */
    protected MenuItem addUserMenu(String username, MenuBar parent) {
        MenuItem userMenu = parent.addItem(username, FontAwesome.USER, null); // $NON-NLS-1$
        addUserMenuItems(userMenu);
        return userMenu;
    }

    /**
     * Adds sub menu items to the given <code>userMenu</code> that is created in
     * {@link #addUserMenu(String, MenuBar)}.
     */
    protected void addUserMenuItems(MenuItem userMenu) {
        userMenu.addItem(NlsText.getString("ApplicationHeader.Logout"), newLogoutCommand()); //$NON-NLS-1$
    }

    /**
     * Returns a {@link Command} that can be used for a logout {@link MenuItem item} in a
     * {@link MenuBar}.
     */
    protected Command newLogoutCommand() {
        return selectedItem -> {
            getUI().getPage().setLocation("./logout"); //$NON-NLS-1$
        };
    }
}

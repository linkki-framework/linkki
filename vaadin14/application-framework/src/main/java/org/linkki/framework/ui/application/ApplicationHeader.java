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
package org.linkki.framework.ui.application;

import static org.linkki.util.Objects.requireNonNull;

import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.dialogs.ApplicationInfoDialog;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.framework.ui.pmo.ApplicationInfoPmo;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.Command;

/**
 * A navigation header bar that is displayed on the top of an {@link ApplicationLayout}. By default, it
 * consists of the {@link ApplicationMenu} on the left as well as a right aligned {@link MenuBar}.
 * 
 * @implSpec {@link ApplicationHeader} can be styled with the CSS class
 *           {@link LinkkiApplicationTheme#APPLICATION_HEADER}. The right aligned {@link MenuBar} uses
 *           the style name {@link LinkkiApplicationTheme#APPLICATION_HEADER_RIGHT} additionally.
 * 
 * @implNote The methods {@link #createRightMenuBar()}, {@link #addHelpMenu(MenuBar)} and
 *           {@link #addHelpMenuItems(MenuItem)} can be used to create an {@link ApplicationInfoDialog}
 *           menu item in the right {@link MenuBar}.
 *           <p>
 *           The methods {@link #createRightMenuBar()}, {@link #addUserMenu(String, MenuBar)},
 *           {@link #addUserMenuItems(MenuItem)} and {@link #newLogoutCommand()} can be used to create a
 *           user logout menu item in the right {@link MenuBar}.
 * 
 * @see ApplicationMenu
 */
public class ApplicationHeader extends Composite<HorizontalLayout> {

    private static final long serialVersionUID = 1L;

    private final ApplicationInfo applicationInfo;
    private final Sequence<ApplicationMenuItemDefinition> menuItemDefinitions;

    public ApplicationHeader(ApplicationInfo applicationInfo,
            Sequence<ApplicationMenuItemDefinition> menuItemDefinitions) {
        this.applicationInfo = requireNonNull(applicationInfo, "applicationInfo must not be null");
        this.menuItemDefinitions = requireNonNull(menuItemDefinitions, "menuItemDefinitions must not be null");
        getContent().addClassName(LinkkiApplicationTheme.APPLICATION_HEADER);
        getContent().setWidthFull();
    }

    /**
     * Initialize UI components.
     * 
     * @implNote Avoid overriding this method if possible. By default, this method assumes that the
     *           header consists of a left floating and a right floating group of UI components.
     * 
     * @implSpec These elements are added by {@link #addLeftComponents()} and
     *           {@link #addRightComponents(HorizontalLayout)} respectively.
     */
    public void init() {
        addLeftComponents();
        addRightComponents();
    }

    /**
     * Adds navigation elements on the left of the header. Adds the {@link ApplicationMenu} by default.
     * 
     * @implNote Only override this method if you do not need the {@link ApplicationMenu} or need to add
     *           elements other than {@link ApplicationMenu}. The {@link ApplicationMenu} itself is
     *           passed to this {@link ApplicationHeader}'s constructor.
     * 
     * @see ApplicationMenu for further information about its configuration.
     */
    protected void addLeftComponents() {
        ApplicationMenu applicationMenu = new ApplicationMenu(getMenuItemDefinitions().list());
        getContent().add(applicationMenu);
        getContent().setFlexGrow(1, applicationMenu);
    }

    /**
     * Creates a {@link HorizontalLayout} and calls {@link #addRightComponents(HorizontalLayout)} to add
     * items to it.
     */
    private void addRightComponents() {
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.addClassName(LinkkiApplicationTheme.APPLICATION_HEADER_RIGHT);
        wrapper.setAlignItems(Alignment.BASELINE);
        wrapper.setSpacing(true);

        addRightComponents(wrapper);

        getContent().add(wrapper);
    }

    /**
     * Calls {@link #createRightMenuBar()} to add items by default.
     * 
     * @param parent a right aligned layout to which the components should be added
     * 
     * @implNote Override {@link #createRightMenuBar()} to add elements to the {@link MenuBar}.
     */
    protected void addRightComponents(HorizontalLayout parent) {
        parent.add(createRightMenuBar());
    }

    /**
     * Creates a right aligned {@link MenuBar} that is added to the {@link ApplicationHeader} by
     * {@link #addRightComponents(HorizontalLayout)}.
     * 
     * @implSpec The created {@link MenuBar} contains a {@link #addHelpMenu(MenuBar) help menu item} by
     *           default.
     */
    protected MenuBar createRightMenuBar() {
        MenuBar rightMenuBar = new MenuBar();
        rightMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
        rightMenuBar.addClassNames(LinkkiApplicationTheme.APPLICATION_MENU, LinkkiTheme.BORDERLESS);
        addHelpMenu(rightMenuBar);

        return rightMenuBar;
    }

    /**
     * Creates a help menu item in the given parent.
     * 
     * @implNote Override {@link #addHelpMenuItems(MenuItem)} to add sub menu items to the created
     *           {@link #addHelpMenu(MenuBar) help menu item}.
     */
    protected MenuItem addHelpMenu(MenuBar parent) {
        MenuItem helpMenu = parent.addItem(VaadinIcon.QUESTION_CIRCLE.create());
        addHelpMenuItems(helpMenu);
        return helpMenu;
    }

    /**
     * Adds by default the {@link ApplicationInfoDialog about sub menu item} created by
     * {@link #addApplicationInfoMenuItem(MenuItem)} to the given <code>helpMenu</code> that is created
     * in {@link #addHelpMenu(MenuBar)}.
     * 
     * @implNote Override to add more sub menu items.
     *           <p>
     *           Extend {@link ApplicationInfoPmo} and override {@link #createApplicationInfoPmo()} to
     *           customize the created {@link ApplicationInfoDialog}.
     */
    protected void addHelpMenuItems(MenuItem helpMenu) {
        addApplicationInfoMenuItem(helpMenu);
    }

    /**
     * Adds an {@link ApplicationInfoDialog about sub menu item} to the given <code>helpMenu</code> that
     * is created in {@link #addHelpMenu(MenuBar)}.
     */
    protected void addApplicationInfoMenuItem(MenuItem helpMenu) {
        helpMenu.getSubMenu()
                .addItem(NlsText.getString("ApplicationHeader.Info"), i -> new ApplicationInfoDialog(
                        createApplicationInfoPmo()).open());
    }

    /**
     * Creates the {@link ApplicationInfoPmo} using {@link #getApplicationInfo()}.
     * 
     * @see ApplicationInfoPmo for further information about its configuration.
     */
    protected ApplicationInfoPmo createApplicationInfoPmo() {
        return new ApplicationInfoPmo(getApplicationInfo());
    }

    /**
     * Creates a user menu item in the given parent.
     * 
     * @implNote Override {@link #addUserMenuItems(MenuItem)} to add sub menu items to the created user
     *           menu.
     */
    protected MenuItem addUserMenu(String username, MenuBar parent) {
        MenuItem userMenu = parent.addItem(VaadinIcon.USER.create()); // $NON-NLS-1$
        userMenu.add(username);
        addUserMenuItems(userMenu);
        return userMenu;
    }

    /**
     * Adds sub menu items to the given <code>userMenu</code> that is created in
     * {@link #addUserMenu(String, MenuBar)}.
     */
    protected void addUserMenuItems(MenuItem userMenu) {
        userMenu.getSubMenu().addItem(NlsText.getString("ApplicationHeader.Logout"), newLogoutCommand()); // $NON-NLS-1$
    }

    /**
     * Returns a {@link Command} that can be used for a logout {@link MenuItem item} in a
     * {@link MenuBar}.
     */
    protected ComponentEventListener<ClickEvent<MenuItem>> newLogoutCommand() {
        return e -> getUI().ifPresent(ui -> ui.getPage().setLocation("./logout"));
    }

    /**
     * Returns the {@link ApplicationMenuItemDefinition menu item definitions} for the
     * {@link ApplicationMenu}.
     */
    protected Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return menuItemDefinitions;
    }

    /**
     * Returns the {@link ApplicationInfo} that is used in {@link #createApplicationInfoPmo()}.
     */
    protected ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }
}

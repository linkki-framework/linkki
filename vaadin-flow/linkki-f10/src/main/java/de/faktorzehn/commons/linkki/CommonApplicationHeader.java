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

package de.faktorzehn.commons.linkki;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.util.HtmlSanitizer;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.application.UserAwareApplicationHeader;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.Command;

/**
 * {@link ApplicationHeader} that displays the application environment provided by the system
 * property {@value #PROPERTY_APPLICATION_ENVIRONMENT}. The {@link Span} displaying the property can
 * be styled with the class name {@value #STYLE_LABEL_APPLICATION_ENVIRONMENT}.
 * 
 * The class is abstract to implement the necessary details like {@link #getUsername()}.
 *
 * @deprecated Use {@link ApplicationHeader} or {@link UserAwareApplicationHeader} instead.
 */
@Deprecated(since = "2.8.0")
public abstract class CommonApplicationHeader extends ApplicationHeader {

    public static final String PROPERTY_APPLICATION_ENVIRONMENT = "de.faktorzehn.application-environment";
    public static final String STYLE_LABEL_APPLICATION_ENVIRONMENT = "f10-application-environment";

    private static final long serialVersionUID = 1L;
    private static final String APPMENU_USER_ID = "appmenu-user";
    private static final String APPMENU_LOGOUT_ID = "appmenu-logout";
    private static final String LOGOUT = "logout";
    private static final String APPLICATIONHEADER_LOGOUT = "ApplicationHeader.Logout";

    public CommonApplicationHeader(ApplicationInfo applicationInfo,
            Sequence<ApplicationMenuItemDefinition> menuItemDefinitions) {
        super(applicationInfo, menuItemDefinitions);
    }

    /**
     * The property is used to display the current environment in the {@link ApplicationHeader}.
     * 
     * Returns the value of the property {@value #PROPERTY_APPLICATION_ENVIRONMENT} if it is set as
     * an application property or as environment variable or else an empty String.
     * 
     * @apiNote Override {@link #getEnvironmentLabel()} to customize the presentation of the
     *          application environment.
     */
    public String getEnvironmentLabel() {
        String environment = System.getProperty(PROPERTY_APPLICATION_ENVIRONMENT,
                                                System.getenv(PROPERTY_APPLICATION_ENVIRONMENT));
        return StringUtils.defaultIfEmpty(environment, "");
    }

    /**
     * The property is used to display the current user in the {@link ApplicationHeader}.
     * 
     * Returns the name of the current user.
     * 
     * @apiNote Override {@link #getEnvironmentLabel()} to customize the presentation of the current
     *          user .
     */
    public abstract String getUsername();

    /**
     * Adds a {@link Span} displaying the value of the {@value #PROPERTY_APPLICATION_ENVIRONMENT}
     * property if it is set in the environment.
     * 
     * @apiNote Override {@link #createApplicationEnvironmentLabel(String)} to customize the
     *          presentation of the application environment.
     */
    @Override
    protected void addRightComponents(HorizontalLayout parent) {
        String applicationEnvironment = getEnvironmentLabel();
        if (StringUtils.isNotBlank(applicationEnvironment)) {
            Span label = createApplicationEnvironmentLabel(applicationEnvironment);
            parent.add(label);
        }
        super.addRightComponents(parent);
    }

    @Override
    protected MenuBar createRightMenuBar() {
        var menuBar = super.createRightMenuBar();
        addUserMenu(menuBar);
        return menuBar;
    }

    /**
     * Creates a {@link Span} displaying the given {@code applicationEnvironment}.
     * <p>
     * This value will be handled as HTML content hence can contain HTML tags and attributes that
     * are whitelisted by the used {@link HtmlSanitizer#sanitizeText(String) HTML sanitizer}.
     * 
     * @param applicationEnvironment the value of the system variable indicating the application
     *            environment (can be HTML content)
     * 
     * @return a {@link Span} displaying the given {@code applicationEnvironment}
     */
    protected Span createApplicationEnvironmentLabel(String applicationEnvironment) {
        Span label = new Span();
        label.getElement().setProperty("innerHTML", HtmlSanitizer.sanitizeText(applicationEnvironment));
        label.addClassName(STYLE_LABEL_APPLICATION_ENVIRONMENT);
        return label;
    }

    /**
     * Creates a user menu item in the given parent.
     * 
     * @apiNote Override {@link #addUserMenuItems(MenuItem)} to add sub menu items to the created
     *          user menu.
     */
    protected MenuItem addUserMenu(MenuBar parent) {
        MenuItem userMenu = parent.addItem(VaadinIcon.USER.create()); // $NON-NLS-1$
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
        MenuItem logoutMenuItem = userMenu.getSubMenu().addItem(NlsText.getString(APPLICATIONHEADER_LOGOUT),
                                                                newLogoutCommand()); // $NON-NLS-1$
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

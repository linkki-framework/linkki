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

import static org.linkki.util.Objects.requireNonNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.util.HtmlSanitizer;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.application.menu.ThemeVariantToggleMenuItemDefinition;
import org.linkki.framework.ui.dialogs.ApplicationInfoDialog;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.framework.ui.pmo.ApplicationInfoPmo;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A navigation header bar that is displayed on the top of an {@link ApplicationLayout}. By default,
 * it consists of the {@link ApplicationMenu} on the left as well as a right aligned
 * {@link MenuBar}.
 * <p>
 * It displays the application environment provided by the system property
 * {@value #PROPERTY_APPLICATION_ENVIRONMENT} or {@value #PROPERTY_F10_APPLICATION_ENVIRONMENT} if
 * the first is not set. The {@link Span} displaying the property can be styled with the class name
 * {@value #STYLE_LABEL_APPLICATION_ENVIRONMENT}.
 * 
 * @implSpec {@link ApplicationHeader} can be styled with the CSS class
 *           {@link LinkkiApplicationTheme#APPLICATION_HEADER}. The right aligned {@link MenuBar}
 *           uses the style name {@link LinkkiApplicationTheme#APPLICATION_HEADER_RIGHT}
 *           additionally.
 * 
 * @implNote The methods {@link #createRightMenuBar()}, {@link #addHelpMenu(MenuBar)} and
 *           {@link #addHelpMenuItems(MenuItem)} can be used to create an
 *           {@link ApplicationInfoDialog} menu item in the right {@link MenuBar}.
 *           <p>
 *           The methods {@link #createRightMenuBar()}, can for example be used to create a user
 *           logout menu item in the right {@link MenuBar}.
 * 
 * @see ApplicationMenu
 */
public class ApplicationHeader extends Composite<HorizontalLayout> {

    public static final String PROPERTY_APPLICATION_ENVIRONMENT = "linkki.application-environment";
    public static final String PROPERTY_F10_APPLICATION_ENVIRONMENT = "de.faktorzehn.application-environment";
    public static final String STYLE_LABEL_APPLICATION_ENVIRONMENT = "linkki-application-environment";

    static final String APPMENU_RIGHT_ID = "appmenu-right";
    static final String APPMENU_HELP_ID = "appmenu-help";
    static final String APPMENU_INFO_ID = "appmenu-info";
    static final String APPMENU_THEME_ID = "appmenu-theme";

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
     * The property is used to display the current environment in the {@link ApplicationHeader}.
     * <p>
     * Returns the value of the property {@value #PROPERTY_APPLICATION_ENVIRONMENT} if it is set as
     * an application property or as environment variable or else the value of the property
     * {@value #PROPERTY_F10_APPLICATION_ENVIRONMENT}. If neither of the properties is set, the
     * method returns {@code null}.
     *
     * @apiNote Override {@link #getEnvironmentLabel()} to customize the presentation of the
     *          application environment.
     */
    @CheckForNull
    public String getEnvironmentLabel() {
        var linkkiEnvironment = System.getProperty(PROPERTY_APPLICATION_ENVIRONMENT,
                                                   System.getenv(PROPERTY_APPLICATION_ENVIRONMENT));
        var f10Environment = System.getProperty(PROPERTY_F10_APPLICATION_ENVIRONMENT,
                                                System.getenv(PROPERTY_F10_APPLICATION_ENVIRONMENT));

        return linkkiEnvironment != null ? linkkiEnvironment : f10Environment;
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
     * Adds navigation elements on the left of the header. Adds the {@link ApplicationMenu} by
     * default.
     * 
     * @implNote Only override this method if you do not need the {@link ApplicationMenu} or need to
     *           add elements other than {@link ApplicationMenu}. The {@link ApplicationMenu} itself
     *           is passed to this {@link ApplicationHeader}'s constructor.
     * 
     * @see ApplicationMenu for further information about its configuration.
     */
    protected void addLeftComponents() {
        ApplicationMenu applicationMenu = new ApplicationMenu(getMenuItemDefinitions().list());
        getContent().add(applicationMenu);
        getContent().setFlexGrow(1, applicationMenu);
    }

    /**
     * Creates a {@link HorizontalLayout} and calls {@link #addRightComponents(HorizontalLayout)} to
     * add items to it.
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
     * Adds a {@link Span} displaying the value returned by {@link #getEnvironmentLabel()} and calls
     * {@link #createRightMenuBar()} to add menu items by default.
     * 
     * @param parent a right aligned layout to which the components should be added
     * 
     * @implNote Override {@link #createRightMenuBar()} to add elements to the {@link MenuBar}.
     */
    protected void addRightComponents(HorizontalLayout parent) {
        String applicationEnvironment = getEnvironmentLabel();
        if (StringUtils.isNotBlank(applicationEnvironment)) {
            Span label = createApplicationEnvironmentLabel(applicationEnvironment);
            parent.add(label);
        }
        parent.add(createRightMenuBar());
    }

    /**
     * Creates a right aligned {@link MenuBar} that is added to the {@link ApplicationHeader} by
     * {@link #addRightComponents(HorizontalLayout)}.
     * 
     * @implSpec The created {@link MenuBar} contains a {@link #addHelpMenu(MenuBar) help menu item}
     *           by default.
     */
    protected MenuBar createRightMenuBar() {
        MenuBar rightMenuBar = new MenuBar();
        rightMenuBar.setId(APPMENU_RIGHT_ID);
        rightMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
        rightMenuBar.addClassNames(LinkkiApplicationTheme.APPLICATION_MENU);
        addHelpMenu(rightMenuBar);

        return rightMenuBar;
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
     * Creates a help menu item in the given parent.
     * 
     * @implNote Override {@link #addHelpMenuItems(MenuItem)} to add sub menu items to the created
     *           {@link #addHelpMenu(MenuBar) help menu item}.
     */
    protected MenuItem addHelpMenu(MenuBar parent) {
        MenuItem helpMenu = parent.addItem(VaadinIcon.QUESTION_CIRCLE.create());
        helpMenu.setId(APPMENU_HELP_ID);
        addHelpMenuItems(helpMenu);
        return helpMenu;
    }

    /**
     * Adds by default the {@link ApplicationInfoDialog about sub menu item} created by
     * {@link #addApplicationInfoMenuItem(MenuItem)} to the given <code>helpMenu</code> that is
     * created in {@link #addHelpMenu(MenuBar)}.
     * 
     * @implNote Override to add more sub menu items.
     *           <p>
     *           Extend {@link ApplicationInfoPmo} and override {@link #createApplicationInfoPmo()}
     *           to customize the created {@link ApplicationInfoDialog}.
     */
    protected void addHelpMenuItems(MenuItem helpMenu) {
        addApplicationInfoMenuItem(helpMenu);
    }

    /**
     * Adds an {@link ApplicationInfoDialog about sub menu item} to the given <code>helpMenu</code>
     * that is created in {@link #addHelpMenu(MenuBar)}.
     */
    protected void addApplicationInfoMenuItem(MenuItem helpMenu) {
        new ApplicationMenuItemDefinition(NlsText.getString("ApplicationHeader.Info"), APPMENU_INFO_ID,
                this::showApplicationInfo).createItem(helpMenu.getSubMenu());
    }

    /**
     * Adds an menu item containing toggles for the given theme variants.
     * 
     * @see ThemeVariantToggleMenuItemDefinition
     */
    protected void addThemeVariantToggles(MenuItem menuItem, ThemeVariantToggleMenuItemDefinition... themeVariants) {
        if (themeVariants.length == 1) {
            themeVariants[0].createItem(menuItem.getSubMenu());
        } else if (themeVariants.length > 1) {
            new ApplicationMenuItemDefinition(NlsText.getString("ApplicationHeader.Theme"), APPMENU_THEME_ID,
                    List.of(themeVariants)).createItem(menuItem.getSubMenu());
        }
    }

    /**
     * Displays the {@link ApplicationInfo}.
     * 
     * @see #createApplicationInfoPmo()
     */
    protected void showApplicationInfo() {
        new ApplicationInfoDialog(createApplicationInfoPmo()).open();
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

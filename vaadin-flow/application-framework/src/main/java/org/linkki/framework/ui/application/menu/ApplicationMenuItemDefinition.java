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
package org.linkki.framework.ui.application.menu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Strings;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.RouteConfiguration;

/**
 * Defines an item in the application's main menu.
 * 
 * @see ApplicationMenu
 * @see ApplicationHeader
 */
public class ApplicationMenuItemDefinition {

    private static final String APPMENU_ID_PREFIX = "appmenu";

    private static final Pattern REGEX_UNKNOWN_CHARS = Pattern.compile("[^a-z0-9]");
    private static final Pattern REGEX_WHITESPACES = Pattern.compile("\\s+");

    private final String name;
    private final String id;
    private final Handler command;
    private final List<ApplicationMenuItemDefinition> subMenuItemDefinitions;

    /**
     * Creates a new menu item that executes the given command on click.
     * 
     * @param name displayed text of the menu item
     * @param command command to execute on click
     * @deprecated Use
     *             {@link ApplicationMenuItemDefinition#ApplicationMenuItemDefinition(String, String, Handler)}
     *             instead
     */
    @Deprecated(since = "2.0.0")
    public ApplicationMenuItemDefinition(String name, Handler command) {
        this(name, nameToId(name), command);
    }

    /**
     * Creates a new menu item that executes the given command on click.
     * 
     * @param name displayed text of the menu item
     * @param id id for the html element
     * @param command command to execute on click
     * @since 2.0.0
     */
    public ApplicationMenuItemDefinition(String name, String id, Handler command) {
        this.name = name;
        this.id = id;
        this.command = command;
        this.subMenuItemDefinitions = Collections.emptyList();
    }

    /**
     * Creates a new menu item that navigates to the given URL on click.
     * <p>
     * If the URL is a relative link that does not start with a protocol,
     * {@link UI#navigate(String)} is used. Otherwise, the link is treated as an external link and
     * is called by using {@link Page#setLocation(String)}.
     * <p>
     * If a internal URL is needed, use {@link RouteConfiguration#getUrl(Class)}.
     * 
     * @param name displayed text of the menu item
     * @param url URL to navigate to
     * @deprecated Use
     *             {@link ApplicationMenuItemDefinition#ApplicationMenuItemDefinition(String, String, String)}
     *             instead
     */
    @Deprecated(since = "2.0.0")
    public ApplicationMenuItemDefinition(String name, String url) {
        this(name, () -> navigateToLink(url));
    }

    /**
     * Creates a new menu item that navigates to the given URL on click.
     * <p>
     * If the URL is a relative link that does not start with a protocol,
     * {@link UI#navigate(String)} is used. Otherwise, the link is treated as an external link and
     * is called by using {@link Page#setLocation(String)}.
     * <p>
     * If a internal URL is needed, use {@link RouteConfiguration#getUrl(Class)}.
     * 
     * @param name displayed text of the menu item
     * @param id id for the html element
     * @param url URL to navigate to
     * @since 2.0.0
     */
    public ApplicationMenuItemDefinition(String name, String id, String url) {
        this(name, id, () -> navigateToLink(url));
    }

    /**
     * Creates a new menu item that navigates to the given class on click.
     * 
     * @param name displayed text of the menu item
     * @param navigationTarget class to navigate to
     * @deprecated Use
     *             {@link ApplicationMenuItemDefinition#ApplicationMenuItemDefinition(String, String, Class)}
     *             instead
     */
    @Deprecated(since = "2.0.0")
    public ApplicationMenuItemDefinition(String name, Class<? extends Component> navigationTarget) {
        this(name, () -> UI.getCurrent().navigate(navigationTarget));
    }

    /**
     * Creates a new menu item that navigates to the given class on click.
     * 
     * @param name displayed text of the menu item
     * @param id id for the html element
     * @param navigationTarget class to navigate to
     * @since 2.0.0
     */
    public ApplicationMenuItemDefinition(String name, String id, Class<? extends Component> navigationTarget) {
        this(name, id, () -> UI.getCurrent().navigate(navigationTarget));
    }

    /**
     * Creates a new menu item with the given sub-menu items.
     * 
     * @param name displayed text of the menu item
     * @param subMenuItemDefinitions items that are displayed in the sub-menu of the newly created
     *            item
     * @deprecated Use
     *             {@link ApplicationMenuItemDefinition#ApplicationMenuItemDefinition(String, String, List)}
     *             instead
     */
    @Deprecated(since = "2.0.0")
    public ApplicationMenuItemDefinition(String name, List<ApplicationMenuItemDefinition> subMenuItemDefinitions) {
        this(name, nameToId(name), subMenuItemDefinitions);
    }

    /**
     * Creates a new menu item with the given sub-menu items.
     * 
     * @param name displayed text of the menu item
     * @param id id for the html element
     * @param subMenuItemDefinitions items that are displayed in the sub-menu of the newly created
     *            item
     * @since 2.0.0
     */
    public ApplicationMenuItemDefinition(String name, String id,
            List<ApplicationMenuItemDefinition> subMenuItemDefinitions) {
        this.name = name;
        this.id = id;
        this.command = Handler.NOP_HANDLER;
        this.subMenuItemDefinitions = subMenuItemDefinitions;
    }

    /* private */ static void navigateToLink(String location) {
        try {
            URL parsedUrl = new URL(location);
            UI.getCurrent().getPage().setLocation(parsedUrl.toString());
        } catch (MalformedURLException e) {
            UI.getCurrent().navigate(location);
        }
    }

    /**
     * Uses this definition to create a new item in the give {@link ApplicationMenu}.
     * 
     * @param menuBar menu bar to create a item in
     * @return the created item
     */
    public MenuItem createItem(ApplicationMenu menuBar) {
        return createItem(menuBar::addItem);
    }

    /**
     * Uses this definition to create a new item in the give {@link SubMenu}.
     * 
     * @param subMenu sub-menu to create a item in
     * @return the created item
     */
    public MenuItem createItem(SubMenu subMenu) {
        return createItem(subMenu::addItem);
    }

    private MenuItem createItem(Function<String, MenuItem> createItemWithCaption) {
        MenuItem item = createItemWithCaption.apply(name);

        item.setId(id);
        item.addClickListener(event -> command.apply());

        for (ApplicationMenuItemDefinition subMenuItemDefinition : subMenuItemDefinitions) {
            subMenuItemDefinition.createItem(item.getSubMenu());
        }
        return item;
    }

    /**
     * Creates an ID for the name of an {@link ApplicationMenuItemDefinition}. Remove all unknown
     * characters and replace all whitespaces with an '-'.
     */
    private static String nameToId(String name) {
        String id = name.toLowerCase()
                .replaceAll(REGEX_UNKNOWN_CHARS.pattern(), " ")
                .replaceAll(REGEX_WHITESPACES.pattern(), "-");

        id = Strings.CS.removeEnd(id, "-");

        return APPMENU_ID_PREFIX + "-" + id;
    }

}

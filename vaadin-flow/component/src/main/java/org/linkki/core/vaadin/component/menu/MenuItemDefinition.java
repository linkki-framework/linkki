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

package org.linkki.core.vaadin.component.menu;

import static org.linkki.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Strings;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Defines a menu item for the {@link SingleItemMenuBar}.
 * 
 * @since 2.8.0
 */
public class MenuItemDefinition {

    public static final Pattern PATTERN_UNKNOWN_CHARS = Pattern.compile("[^a-z0-9]");
    public static final Pattern PATTERN_WHITESPACES = Pattern.compile("\\s+");

    private final String caption;
    @CheckForNull
    private final VaadinIcon icon;
    private final Handler command;
    private final String id;
    private final boolean visible;
    private final boolean enabled;
    private final List<MenuItemDefinition> subItems;

    /**
     * Creates a new menu item that executes the given command on click.
     *
     * @param caption displayed text of the menu item
     * @param icon an optional {@link VaadinIcon icon} for the menu
     * @param command command to execute on click
     */
    public MenuItemDefinition(String caption, @CheckForNull VaadinIcon icon, Handler command) {
        this(captionToId(caption), caption, icon, command, true, true, Collections.emptyList());
    }

    /**
     * Creates a new menu item that executes the given command on click. The item is
     * {@linkplain #isVisible() visible} and {@linkplain #isEnabled() enabled}.
     *
     * @param caption displayed text of the menu item
     * @param icon an optional {@link VaadinIcon icon} for the menu
     * @param command command to execute on click
     * @param id id for the HTML element
     * @since 2.8.0
     */
    public MenuItemDefinition(String caption, @CheckForNull VaadinIcon icon, Handler command, String id) {
        this(id, caption, icon, command, true, true, Collections.emptyList());
    }

    /**
     * Creates a new menu item with the given visibility and enabled state that executes the given
     * command on click.
     *
     * @param id id for the HTML element
     * @param caption displayed text of the menu item
     * @param icon an optional {@link VaadinIcon icon} for the menu
     * @param command command to execute on click
     * @param visible whether the menu item is visible
     * @param enabled whether the menu item is enabled
     * @param subItems nested sub-menu items; non-empty makes this item a submenu parent
     * @since 2.8.0
     */
    private MenuItemDefinition(String id, String caption,
            @CheckForNull VaadinIcon icon,
            Handler command,
            boolean visible,
            boolean enabled,
            List<MenuItemDefinition> subItems) {
        this.caption = caption;
        this.icon = icon;
        this.command = command;
        this.id = id;
        this.visible = visible;
        this.enabled = enabled;
        this.subItems = subItems;
    }

    public String getCaption() {
        return caption;
    }

    @CheckForNull
    public VaadinIcon getIcon() {
        return icon;
    }

    public Handler getCommand() {
        return command;
    }

    public String getId() {
        return id;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns the nested sub-menu items. Non-empty means this item is a submenu parent.
     *
     * @since 2.11.0
     */
    public List<MenuItemDefinition> getSubItems() {
        return subItems;
    }

    /**
     * Returns {@code true} if this item has sub-items and acts as a submenu parent rather than a
     * leaf action item.
     *
     * @since 2.11.0
     */
    public boolean isSubmenu() {
        return !subItems.isEmpty();
    }

    /**
     * Creates an ID from the caption of a {@link MenuItemDefinition}. Removes all unknown
     * characters and replaces all whitespaces with a '-'.
     */
    private static String captionToId(String caption) {
        String id = PATTERN_WHITESPACES
                .matcher(
                         PATTERN_UNKNOWN_CHARS.matcher(caption.toLowerCase()).replaceAll(" "))
                .replaceAll("-");

        id = Strings.CS.removeEnd(id, "-");

        return id;
    }

    public static Builder builder(String id) {
        return new Builder(requireNonNull(id, "id must not be null"));
    }

    /**
     * Builder for a {@code MenuItemDefinition}.
     */
    public static class Builder {
        private final String id;

        private String caption = "";
        @CheckForNull
        private VaadinIcon icon = null;
        private Handler command = Handler.NOP_HANDLER;
        private boolean visible = true;
        private boolean enabled = true;
        private List<MenuItemDefinition> subItems = Collections.emptyList();

        private Builder(String id) {
            this.id = id;
        }

        /**
         * Defines the caption of the item.
         * <p>
         * The caption will <b>not</b> be updated and should never change.
         */
        public Builder caption(String caption) {
            this.caption = requireNonNull(caption, "caption must not be null");
            return this;
        }

        /**
         * Defines the icon of the item.
         * <p>
         * The icon will <b>not</b> be updated and should never change.
         */
        public Builder icon(@CheckForNull VaadinIcon icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Defines the command to execute on click.
         * <p>
         * The command will <b>not</b> be updated and should never change.
         */
        public Builder command(Handler command) {
            this.command = requireNonNull(command, "command must not be null");
            return this;
        }

        /**
         * Defines whether the item is visible.
         * <p>
         * If an item with the same ID is already present, the visibility of the item will be
         * updated with the given value of this method.
         */
        public Builder visibleIf(boolean visible) {
            this.visible = visible;
            return this;
        }

        /**
         * Defines whether the item is enabled.
         * <p>
         * If an item with the same ID is already present, the enabled state of the item will be
         * updated with the given value of this method.
         */
        public Builder enabledIf(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Defines nested sub-menu items. When sub-items are present, this item acts as a submenu
         * parent and its {@linkplain #command(Handler) command} is not executed on click.
         *
         * @since 2.11.0
         */
        public Builder subItems(List<MenuItemDefinition> subItems) {
            this.subItems = requireNonNull(subItems, "subItems must not be null");
            return this;
        }

        public MenuItemDefinition build() {
            return new MenuItemDefinition(id, caption, icon, command, visible, enabled, subItems);
        }
    }
}
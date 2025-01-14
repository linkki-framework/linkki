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

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.linkki.core.vaadin.component.HasCaption;
import org.linkki.core.vaadin.component.HasIcon;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A variant of {@link MenuBar} that only contains a single {@link MenuItem}, to which the common
 * linkki aspects such as caption and icon are applied directly. Trying to add additional menu items
 * will throw an {@link UnsupportedOperationException}.
 * 
 * @since 2.8.0
 */
public class SingleItemMenuBar extends Composite<MenuBar> implements HasCaption, HasEnabled, HasIcon {

    @Serial
    private static final long serialVersionUID = 1L;

    private final MenuItem theItem;

    @CheckForNull
    private VaadinIcon icon;

    public SingleItemMenuBar(String caption) {
        this(caption, null);
    }

    public SingleItemMenuBar(String caption, @CheckForNull VaadinIcon icon) {
        this(caption, icon, new MenuBarVariant[] {});
    }

    public SingleItemMenuBar(String caption, @CheckForNull VaadinIcon icon, MenuBarVariant[] variants) {
        this.theItem = getContent().addItem(caption);
        this.icon = icon;
        if (icon != null) {
            this.theItem.addComponentAsFirst(icon.create());
        }
        getContent().addThemeVariants(variants);
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        this.theItem.setId(id);
    }

    public List<MenuItem> getItems() {
        return Collections.singletonList(this.theItem);
    }

    @Override
    public String getCaption() {
        return this.theItem.getText();
    }

    @Override
    public void setCaption(String caption) {
        updateContent(caption, this.icon);
    }

    @Override
    @CheckForNull
    public VaadinIcon getIcon() {
        return this.icon;
    }

    @Override
    public void setIcon(@CheckForNull VaadinIcon icon) {
        updateContent(getCaption(), icon);
    }

    private void updateContent(String caption, @CheckForNull VaadinIcon newIcon) {
        this.icon = newIcon;

        theItem.removeAll();
        if (this.icon != null) {
            theItem.add(this.icon.create(), new Text(caption));
        } else {
            theItem.add(caption);
        }
    }

    /**
     * Internal method that adds sub menu items to the single item.
     *
     * @param itemDefinitions the {@link MenuItemDefinition definitions} for the sub menu items.
     * @param modelChanged the handler to invoke after the execution of a sub menu item's
     *            {@linkplain MenuItemDefinition#getCommand() command}.
     */
    public void createSubMenuItems(List<MenuItemDefinition> itemDefinitions, Handler modelChanged) {
        var subMenu = theItem.getSubMenu();
        for (var itemDefinition : itemDefinitions) {
            var subMenuItem = subMenu.addItem(itemDefinition.getCaption());
            subMenuItem.setId(getSubMenuItemId(itemDefinition));
            setSubMenuItemIcon(subMenuItem, itemDefinition);
            subMenuItem.setVisible(itemDefinition.isVisible());
            subMenuItem.setEnabled(itemDefinition.isEnabled());
            subMenuItem.addClickListener(event -> {
                if (subMenuItem.isVisible() && subMenuItem.isEnabled()) {
                    itemDefinition.getCommand().andThen(modelChanged).apply();
                }
            });
        }
    }

    private String getSubMenuItemId(MenuItemDefinition itemDefinition) {
        return theItem.getId()
                .map(id -> id + "-" + itemDefinition.getId())
                .orElse(itemDefinition.getId());
    }

    private void setSubMenuItemIcon(MenuItem subMenuItem, MenuItemDefinition definition) {
        var subMenuIcon = definition.getIcon();
        if (subMenuIcon != null) {
            var iconComponent = subMenuIcon.create();
            iconComponent.setSize("1em");
            iconComponent.getElement().getStyle().set("margin-right", "0.5em");
            subMenuItem.addComponentAsFirst(iconComponent);
        }
    }

    /**
     * Internal method that updates the sub menu items' visibility and enabled state using the given
     * {@link MenuItemDefinition}s.
     * <p>
     * Items are mapped to definitions using their IDs. Items without corresponding definitions are
     * not updated, definitions without corresponding items are ignored.
     */
    public void updateSubMenuItems(List<MenuItemDefinition> itemDefinitions) {
        var itemDefinitionById = itemDefinitions.stream()
                .collect(Collectors.toMap(this::getSubMenuItemId, Function.identity()));
        theItem.getSubMenu()
                .getItems()
                .forEach(item -> item.getId().map(itemDefinitionById::get).ifPresent(itemDefinition -> {
                    item.setVisible(itemDefinition.isVisible());
                    item.setEnabled(itemDefinition.isEnabled());
                }));
    }
}

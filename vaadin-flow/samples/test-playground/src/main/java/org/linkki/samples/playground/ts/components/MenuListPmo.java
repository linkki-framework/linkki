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

package org.linkki.samples.playground.ts.components;

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIMenuList;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.menu.MenuItemDefinition;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;

@UISection
public class MenuListPmo {

    private static final String START_NEW_GAME_MENU_ID = "start-new-game";
    private static final String JOIN_EXISTING_GAME_MENU_ID = "join-existing-game";
    private static final String EXIT_GAME_MENU_ID = "exit-game";

    private boolean allowNewGames = true;
    private boolean showExistingGames = false;

    @SectionHeader
    @UICheckBox(position = -10,
            caption = "Show existing games?")
    public boolean isShowExistingGames() {
        return showExistingGames;
    }

    public void setShowExistingGames(boolean showExistingGames) {
        this.showExistingGames = showExistingGames;
    }

    @SectionHeader
    @UICheckBox(position = -20, caption = "Allow starting new games?")
    public boolean isAllowNewGames() {
        return allowNewGames;
    }

    public void setAllowNewGames(boolean allowNewGames) {
        this.allowNewGames = allowNewGames;
    }

    @UIMenuList(position = 30,
            label = "Menu List with dynamic child items",
            caption = "Play",
            showIcon = true,
            icon = VaadinIcon.PLAY)
    public List<MenuItemDefinition> getPlayMenu() {
        var newGameItem = MenuItemDefinition.builder(START_NEW_GAME_MENU_ID)
                .caption("Start new game")
                .icon(VaadinIcon.PLUS)
                .command(() -> Notification.show("New game"))
                .enabledIf(allowNewGames)
                .build();
        var joinGameItem = MenuItemDefinition.builder(JOIN_EXISTING_GAME_MENU_ID)
                .caption("Join existing game")
                .command(() -> Notification.show("Join game"))
                .visibleIf(showExistingGames)
                .build();
        return List.of(newGameItem, joinGameItem);
    }

    @BindIcon(VaadinIcon.GAMEPAD)
    @UIMenuList(position = 40,
            label = "Menu List with @BindIcon",
            caption = "Exit")
    public List<MenuItemDefinition> getBindIconPlayMenu() {
        return List.of(MenuItemDefinition.builder(EXIT_GAME_MENU_ID)
                .caption("Exit game")
                .icon(VaadinIcon.EXIT)
                .command(() -> Notification.show("Exit game"))
                .build());
    }

    @UIMenuList(position = 50,
            label = "Menu List with Variant Primary",
            caption = "Primary Menu List",
            variants = MenuBarVariant.LUMO_PRIMARY,
            icon = VaadinIcon.HOME)
    public List<MenuItemDefinition> getVariantPrimaryMenu() {
        List<MenuItemDefinition> menuItems = new ArrayList<>();
        menuItems.add(new MenuItemDefinition("Primary Menu Item", VaadinIcon.HOME,
                () -> Notification.show("Primary item clicked"), "primaryMenuId"));
        return menuItems;
    }

    @UIMenuList(position = 60, label = "Menu List with Variant Tertiary Inline",
            caption = "Tertiary Menu List",
            variants = MenuBarVariant.LUMO_TERTIARY_INLINE,
            icon = VaadinIcon.HOME)
    public List<MenuItemDefinition> getVariantTertiaryInlineMenu() {
        List<MenuItemDefinition> menuItems = new ArrayList<>();
        menuItems.add(new MenuItemDefinition("Tertiary Menu Item", VaadinIcon.HOME,
                () -> Notification.show("Tertiary item clicked"), "tertiaryMenuId"));
        return menuItems;
    }
}

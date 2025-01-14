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
package org.linkki.samples.f10;

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;

import de.faktorzehn.commons.linkki.ui.menu.MenuItemDefinition;
import de.faktorzehn.commons.linkki.ui.menu.UIMenuButton;
import de.faktorzehn.commons.linkki.ui.menu.UIMenuList;

@SuppressWarnings("deprecation")
public class SampleApplicationMainSection {

    private SampleApplicationMainSection() {
        // no instances
    }

    public static Component create() {
        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("menuList").caption("@UIMenuList")
                                       .content(() -> VaadinUiCreator.createComponent(new MenuListPmo(),
                                                                                      new BindingContext()))
                                       .build(),
                               LinkkiTabSheet.builder("menuButton").caption("@MenuButton")
                                       .content(() -> VaadinUiCreator.createComponent(new MenuButtonPmo(),
                                                                                      new BindingContext()))
                                       .build(),
                               LinkkiTabSheet.builder("menuBarVariant").caption("MenuBarVariant")
                                       .content(() -> VaadinUiCreator.createComponent(new MenuBarVariantPmo(),
                                                                                      new BindingContext()))
                                       .build());
        return tabLayout;
    }

    @UIVerticalLayout
    public static class MenuListPmo {

        private static final String START_NEW_GAME_MENU_ID = "menu-start-new-game-id";
        private static final String JOIN_EXISTING_GAME_MENU_ID = "menu-join-existing-game-id";
        private static final String EXIT_GAME_MENU_ID = "menu-exit-game-id";

        private boolean allowNewGames = true;
        private boolean showExistingGames = false;

        @UILabel(position = 10)
        public String getPlayMenuInfo() {
            return "With child items that open notifications when clicked. " +
                    "The main button and its child items have icons.";
        }

        @UICheckBox(position = 11, caption = "Show existing games?")
        public boolean isShowExistingGames() {
            return showExistingGames;
        }

        public void setShowExistingGames(boolean showExistingGames) {
            this.showExistingGames = showExistingGames;
        }

        @UICheckBox(position = 12, caption = "Allow starting new games?")
        public boolean isAllowNewGames() {
            return allowNewGames;
        }

        public void setAllowNewGames(boolean allowNewGames) {
            this.allowNewGames = allowNewGames;
        }

        @UIMenuList(position = 20, caption = "Play", showIcon = true, icon = VaadinIcon.PLAY)
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

        @UILabel(position = 30)
        public String getBindIconPlayMenuInfo() {
            return "With @BindIcon annotation.";
        }

        @BindIcon(VaadinIcon.GAMEPAD)
        @UIMenuList(position = 40, caption = "Play with BindIcon")
        public List<MenuItemDefinition> getBindIconPlayMenu() {
            return List.of(MenuItemDefinition.builder(EXIT_GAME_MENU_ID)
                    .caption("Exit")
                    .icon(VaadinIcon.EXIT)
                    .command(() -> Notification.show("Exit game"))
                    .build());
        }
    }

    @UIVerticalLayout
    public static class MenuButtonPmo {

        private String dynamicCaption = "Dynamic caption";
        private boolean dynamicEnabled = true;

        @UILabel(position = 10)
        public String getDynamicInfo() {
            return "With dynamic caption. The button does not have an icon.";
        }

        @UIMenuButton(captionType = CaptionType.DYNAMIC, position = 20,
                caption = "",
                icon = VaadinIcon.AIRPLANE,
                showIcon = false)
        public void dynamicButton() {
            Notification.show("Dynamic button clicked");
        }

        public String getDynamicButtonCaption() {
            return getDynamicCaption();
        }

        @UITextField(position = 30, label = "TextField to change caption of button")
        public String getDynamicCaption() {
            return dynamicCaption;
        }

        public void setDynamicCaption(String dynamicCaption) {
            this.dynamicCaption = dynamicCaption;
        }

        @UILabel(position = 40)
        public String getDynamicBindIconInfo() {
            return "With @BindIcon annotation and dynamic enabled status. " +
                    "The icon changes depending on the enabled status.";
        }

        @BindIcon
        @UIMenuButton(position = 50, enabled = EnabledType.DYNAMIC,
                caption = "Dynamic bind icon",
                icon = VaadinIcon.AIRPLANE, showIcon = false)
        public void dynamicBindIconButton() {
            Notification.show("Dynamic bind icon");
        }

        public VaadinIcon getDynamicBindIconButtonIcon() {
            return dynamicEnabled ? VaadinIcon.CHECK : VaadinIcon.LOCK;
        }

        public boolean isDynamicBindIconButtonEnabled() {
            return isDynamicEnabled();
        }

        @UICheckBox(position = 60, caption = "Button enabled?")
        public boolean isDynamicEnabled() {
            return dynamicEnabled;
        }

        public void setDynamicEnabled(boolean dynamicEnabled) {
            this.dynamicEnabled = dynamicEnabled;
        }

    }

    @UIVerticalLayout
    public static class MenuBarVariantPmo {

        @UILabel(position = 10)
        public String getVariantPrimaryInfo() {
            return "@UIMenuButton with icon and MenuBarVariant.LUMO_PRIMARY";
        }

        @UIMenuButton(position = 20, caption = "Variant Primary",
                variants = MenuBarVariant.LUMO_PRIMARY,
                icon = VaadinIcon.HOME)
        public void buttonVariantPrimary() {
            // nop
        }

        @UILabel(position = 30)
        public String getVariantTertiaryInlineMenuInfo() {
            return "@UIMenuButton with icon and MenuBarVariant.LUMO_TERTIARY_INLINE";
        }

        @UIMenuList(position = 40, caption = "Variant Tertiary Inline",
                variants = MenuBarVariant.LUMO_TERTIARY_INLINE,
                icon = VaadinIcon.HOME)
        public List<MenuItemDefinition> getVariantTertiaryInlineMenu() {
            List<MenuItemDefinition> menuItems = new ArrayList<>();
            menuItems.add(new MenuItemDefinition("Another Inline Variant", VaadinIcon.BED,
                    () -> Notification.show("Go to bed"), "anotherMenuId"));
            return menuItems;
        }

    }
}
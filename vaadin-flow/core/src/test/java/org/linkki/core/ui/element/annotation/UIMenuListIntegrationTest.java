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

package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.vaadin.component.menu.MenuItemDefinition;
import org.linkki.core.vaadin.component.menu.SingleItemMenuBar;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

class UIMenuListIntegrationTest {

    private BindingContext bindingContext;
    private VerticalLayout layout;
    private MenuListTestPmo testPmo;

    @BeforeEach
    void setup() {
        bindingContext = new BindingContext();
        testPmo = new MenuListTestPmo();
        layout = (VerticalLayout)VaadinUiCreator.createComponent(testPmo, bindingContext);
    }

    @Test
    void testMenuList_CreatesSingleItemMenuBar() {
        assertThat(layout.getComponentAt(0)).isInstanceOf(SingleItemMenuBar.class);
        assertThat(layout.getComponentAt(1)).isInstanceOf(SingleItemMenuBar.class);
    }

    @Test
    void testMenuList_CaptionAppliedToMenuItem() {
        SingleItemMenuBar menuList = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuList.getItems().get(0);

        assertThat(menuList.getCaption()).isEqualTo(MenuListTestPmo.LIST_CAPTION);
        assertThat(theItem.getText()).isEqualTo(MenuListTestPmo.LIST_CAPTION);
    }

    @Test
    void testMenuList_IconAppliedToMenuItem() {
        SingleItemMenuBar menuList = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuList.getItems().get(0);

        assertThat(menuList.getIcon()).isEqualTo(VaadinIcon.LIST);
        Icon icon = (Icon)theItem.getChildren().toList().get(0);
        assertThat(icon.getElement().getAttribute("icon")).isEqualTo("vaadin:list");
    }

    @Test
    void testMenuList_CaptionOverwrittenWithBindCaption() {
        SingleItemMenuBar menuList = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuList.getCaption()).isEqualTo(MenuListTestPmo.BOUND_CAPTION);
    }

    @Test
    void testMenuList_HideIcon() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(2);
        MenuItem theItem = menuButton.getItems().get(0);

        // no icon
        List<Class<?>> collect = theItem.getChildren().map(Component::getClass).collect(Collectors.toList());
        assertThat(collect).contains(Text.class);
    }

    @Test
    void testMenuList_NullItems() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(3);
        MenuItem theItem = menuButton.getItems().get(0);

        // no subitems
        assertThat(theItem.getSubMenu().getItems().size()).isZero();
    }

    @Test
    void testMenuList_IconOverwrittenWithBindIcon() {
        SingleItemMenuBar menuList = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuList.getIcon()).isEqualTo(VaadinIcon.ABACUS);
    }

    @Test
    void testMenuList_DynamicEnabled() {
        SingleItemMenuBar dynamicList = (SingleItemMenuBar)layout.getComponentAt(0);
        assertThat(dynamicList.isEnabled()).isTrue();
        testPmo.setDynamicEnabled(false);
        bindingContext.modelChanged();

        assertThat(dynamicList.isEnabled()).isFalse();
    }

    @Test
    void testMenuList_MenuItemsCreated() {
        SingleItemMenuBar menuList = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuList.getItems().get(0);

        List<MenuItem> subItems = theItem.getSubMenu().getItems();
        assertThat(subItems.size()).isEqualTo(2);
        assertThat(subItems.get(0).getText()).isEqualTo(MenuListTestPmo.MENU_ITEM1_TEXT);
        Icon icon1 = (Icon)subItems.get(0).getChildren().toList().get(0);
        assertThat(icon1.getElement().getAttribute("icon")).isEqualTo("vaadin:bug");
        assertThat(subItems.get(1).getText()).isEqualTo(MenuListTestPmo.MENU_ITEM2_TEXT);
        // no icon
        assertThat(subItems.get(1).getChildren().count()).isZero();
    }

    @Test
    void testMenuList_MenuItemsHandler() {
        SingleItemMenuBar menuList = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuList.getItems().get(0);
        MenuItem subItem = theItem.getSubMenu().getItems().get(1);

        ComponentUtil.fireEvent(subItem, new ClickEvent<>(subItem));

        assertThat(testPmo.getDummyValue()).isEqualTo(MenuListTestPmo.MENU_ITEM2_TEXT);
    }

    @Test
    void testMenuList_IdsAppliedToMenuItems() {
        SingleItemMenuBar menuList = (SingleItemMenuBar)layout.getComponentAt(0);
        List<MenuItem> menuItems = menuList.getItems().get(0).getSubMenu().getItems();

        assertThat(menuItems.get(0).getId()).hasValue("menuList-" + MenuListTestPmo.MENU_ITEM1_ID);
        assertThat(menuItems.get(1).getId()).hasValue("menuList-" + MenuListTestPmo.MENU_ITEM2_ID);
    }

    @Test
    void testMenuList_WithVariant() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);

        assertThat(menuButton.getContent().getThemeNames()).contains(MenuBarVariant.LUMO_PRIMARY.getVariantName());
    }

    @Test
    void testMenuList_NoVariant() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuButton.getContent().getThemeNames()).isEmpty();
    }

    @Test
    void testMenuList_DynamicMenuItems() {
        var menuButton = (SingleItemMenuBar)layout.getComponentAt(4);
        var theItem = menuButton.getItems().get(0);
        testPmo.setSubMenuDynamicVisible(true);
        testPmo.setSubMenuDynamicEnabled(true);
        bindingContext.modelChanged();
        assertThat(theItem.getSubMenu().getItems().get(0).isVisible()).isTrue();
        assertThat(theItem.getSubMenu().getItems().get(1).isEnabled()).isTrue();

        testPmo.setSubMenuDynamicVisible(false);
        testPmo.setSubMenuDynamicEnabled(true);
        bindingContext.modelChanged();
        assertThat(theItem.getSubMenu().getItems().get(0).isVisible()).isFalse();
        assertThat(theItem.getSubMenu().getItems().get(1).isEnabled()).isTrue();

        testPmo.setSubMenuDynamicVisible(true);
        testPmo.setSubMenuDynamicEnabled(false);
        bindingContext.modelChanged();
        assertThat(theItem.getSubMenu().getItems().get(0).isVisible()).isTrue();
        assertThat(theItem.getSubMenu().getItems().get(1).isEnabled()).isFalse();

        testPmo.setSubMenuDynamicVisible(false);
        testPmo.setSubMenuDynamicEnabled(false);
        bindingContext.modelChanged();
        assertThat(theItem.getSubMenu().getItems().get(0).isVisible()).isFalse();
        assertThat(theItem.getSubMenu().getItems().get(1).isEnabled()).isFalse();
    }

    @Test
    void testMenuList_DefaultIcon() {
        SingleItemMenuBar menuListWithDefaultIcon = (SingleItemMenuBar)layout.getComponentAt(5);

        // Verify that the default icon is VaadinIcon.ELLIPSIS_DOTS_H
        assertThat(menuListWithDefaultIcon.getIcon()).isEqualTo(VaadinIcon.ELLIPSIS_DOTS_H);

        // Optionally, if you want to verify the actual icon attribute in the DOM:
        MenuItem theItem = menuListWithDefaultIcon.getItems().get(0);
        Icon icon = (Icon)theItem.getChildren().toList().get(0);
        assertThat(icon.getElement().getAttribute("icon")).isEqualTo("vaadin:ellipsis-dots-h");
    }

    @UIVerticalLayout
    public static class MenuListTestPmo {

        private static final String MENU_ITEM1_TEXT = "menuItem1";
        private static final String MENU_ITEM2_TEXT = "menuItem2";
        private static final String BOUND_CAPTION = "BoundCaption";

        private boolean dynamicEnabled = true;
        private String dummyValue = "";
        private boolean subMenuDynamicVisible = true;
        private boolean subMenuDynamicEnabled = true;

        static final String LIST_CAPTION = "MenuListCaption";
        static final String MENU_ITEM1_ID = "menuItem1Id";
        static final String MENU_ITEM2_ID = "menuItem2Id";

        @UIMenuList(position = 0, caption = LIST_CAPTION, icon = VaadinIcon.LIST, enabled = EnabledType.DYNAMIC,
                variants = MenuBarVariant.LUMO_PRIMARY)
        public List<MenuItemDefinition> getMenuList() {
            return List.of(
                           MenuItemDefinition.builder(MENU_ITEM1_ID)
                                   .caption(MENU_ITEM1_TEXT)
                                   .icon(VaadinIcon.BUG)
                                   .command(() -> setDummyValue(MENU_ITEM1_TEXT))
                                   .build(),
                           MenuItemDefinition.builder(MENU_ITEM2_ID)
                                   .caption(MENU_ITEM2_TEXT)
                                   .command(() -> setDummyValue(MENU_ITEM2_TEXT))
                                   .build());
        }

        public boolean isMenuListEnabled() {
            return dynamicEnabled;
        }

        @UIMenuList(caption = LIST_CAPTION, icon = VaadinIcon.LIST, position = 1)
        @BindIcon(VaadinIcon.ABACUS)
        @BindCaption(captionType = CaptionType.STATIC, value = BOUND_CAPTION)
        public List<MenuItemDefinition> getMenuListWithBindAnnotations() {
            return List.of(
                           MenuItemDefinition.builder(MENU_ITEM1_ID)
                                   .caption(MENU_ITEM1_TEXT)
                                   .icon(VaadinIcon.BUG)
                                   .command(() -> setDummyValue(MENU_ITEM1_TEXT))
                                   .build(),
                           MenuItemDefinition.builder(MENU_ITEM2_ID)
                                   .caption(MENU_ITEM2_TEXT)
                                   .icon(VaadinIcon.BUG)
                                   .command(() -> setDummyValue(MENU_ITEM2_TEXT))
                                   .build());
        }

        @UIMenuList(caption = LIST_CAPTION, icon = VaadinIcon.LIST, position = 2, showIcon = false)
        public List<MenuItemDefinition> getMenuListWithNoIcon() {
            return Collections.emptyList();
        }

        @UIMenuList(caption = LIST_CAPTION, icon = VaadinIcon.LIST, position = 3, showIcon = false)
        public List<MenuItemDefinition> getNullMenuList() {
            // for backward compatibility null is treated like an empty list
            return null;
        }

        @UIMenuList(caption = LIST_CAPTION, icon = VaadinIcon.LIST, position = 4, showIcon = false)
        public List<MenuItemDefinition> getDynamicMenuList() {
            return List.of(
                           MenuItemDefinition.builder("1")
                                   .visibleIf(subMenuDynamicVisible)
                                   .build(),
                           MenuItemDefinition.builder("2")
                                   .enabledIf(subMenuDynamicEnabled)
                                   .build());
        }

        @UIMenuList(position = 5, caption = "Default Icon Menu List")
        public List<MenuItemDefinition> getMenuListWithDefaultIcon() {
            return Collections.emptyList();
        }

        private void setDummyValue(String value) {
            this.dummyValue = value;
        }

        public void setDynamicEnabled(boolean dynamicEnabled) {
            this.dynamicEnabled = dynamicEnabled;
        }

        public String getDummyValue() {
            return dummyValue;
        }

        public void setSubMenuDynamicVisible(boolean subMenuDynamicVisible) {
            this.subMenuDynamicVisible = subMenuDynamicVisible;
        }

        public void setSubMenuDynamicEnabled(boolean subMenuDynamicEnabled) {
            this.subMenuDynamicEnabled = subMenuDynamicEnabled;
        }
    }
}
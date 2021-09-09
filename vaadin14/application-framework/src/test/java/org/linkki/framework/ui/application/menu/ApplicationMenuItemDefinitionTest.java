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

package org.linkki.framework.ui.application.menu;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.util.handler.Handler;
import org.mockito.Mockito;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.page.Page;

class ApplicationMenuItemDefinitionTest {

    private UI testUi;
    private Page testPage;

    @BeforeEach
    void initCurrentUi() {
        testUi = mock(UI.class);
        testPage = mock(Page.class);

        when(testUi.getPage()).thenReturn(testPage);
        UI.setCurrent(testUi);
    }

    @AfterEach
    void cleanUpCurrentUi() {
        UI.setCurrent(null);
    }

    @Test
    void testCreateItem_MenuBar_NoSubMenu() {
        ApplicationMenuItemDefinition itemDefinition = new ApplicationMenuItemDefinition("name", Handler.NOP_HANDLER);
        ApplicationMenu menuBar = new ApplicationMenu();

        itemDefinition.createItem(menuBar);

        assertThat(menuBar.getItems(), hasSize(1));
        assertThat(menuBar.getItems().get(0).getText(), is("name"));
        assertThat(menuBar.getItems().get(0).getSubMenu().getItems(), hasSize(0));
    }

    @Test
    void testCreateItem_MenuBar_SubMenu() {
        ApplicationMenuItemDefinition itemDefinition = new ApplicationMenuItemDefinition("name",
                Arrays.asList(new ApplicationMenuItemDefinition("sub", Handler.NOP_HANDLER)));
        ApplicationMenu menuBar = new ApplicationMenu();

        itemDefinition.createItem(menuBar);

        assertThat(menuBar.getItems(), hasSize(1));
        assertThat(menuBar.getItems().get(0).getText(), is("name"));
        assertThat(menuBar.getItems().get(0).getSubMenu().getItems(), hasSize(1));
        assertThat(menuBar.getItems().get(0).getSubMenu().getItems().get(0).getText(), is("sub"));
    }

    @Test
    void testCreateItem_SubMenu_NoSubMenu() {
        ApplicationMenuItemDefinition itemDefinition = new ApplicationMenuItemDefinition("name", Handler.NOP_HANDLER);
        MenuBar menuBar = new MenuBar();
        SubMenu subMenu = menuBar.addItem("item").getSubMenu();

        itemDefinition.createItem(subMenu);

        assertThat(subMenu.getItems(), hasSize(1));
        assertThat(subMenu.getItems().get(0).getText(), is("name"));
    }

    @Test
    void testCreateItem_SubMenu_SubMenu() {
        ApplicationMenuItemDefinition itemDefinition = new ApplicationMenuItemDefinition("name",
                Arrays.asList(new ApplicationMenuItemDefinition("sub", Handler.NOP_HANDLER)));
        MenuBar menuBar = new MenuBar();
        SubMenu subMenu = menuBar.addItem("item").getSubMenu();

        itemDefinition.createItem(subMenu);

        assertThat(subMenu.getItems(), hasSize(1));
        assertThat(subMenu.getItems().get(0).getText(), is("name"));
        assertThat(subMenu.getItems().get(0).getSubMenu().getItems(), hasSize(1));
        assertThat(subMenu.getItems().get(0).getSubMenu().getItems().get(0).getText(), is("sub"));
    }

    @Test
    void testNavigateToLink_Relative() {
        ApplicationMenuItemDefinition.navigateToLink("route");

        Mockito.verify(testUi).navigate("route");
    }

    @Test
    void testNavigateToLink_Absolute() {
        ApplicationMenuItemDefinition.navigateToLink("http://some");

        verify(testPage).setLocation("http://some");
    }

    @Test
    void testNavigateToLink_QueryParams() {
        ApplicationMenuItemDefinition.navigateToLink("http://some?param=test");

        verify(testPage).setLocation("http://some?param=test");
    }
}

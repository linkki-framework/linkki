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

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.framework.ui.application.menu.ThemeVariantToggleMenuItemDefinition.LINKKI_CARD;
import static org.linkki.framework.ui.application.menu.ThemeVariantToggleMenuItemDefinition.LINKKI_COMPACT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.nls.NlsText;
import org.linkki.util.Sequence;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

class ApplicationHeaderTest {

    private UI testUi;

    @BeforeEach
    public void initCurrentUi() {
        testUi = new UI();
        UI.setCurrent(testUi);
    }

    @AfterEach
    public void cleanUpCurrentUi() {
        UI.setCurrent(null);
    }

    @Test
    void testLeftMenu() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(),
                Sequence.of(new ApplicationMenuItemDefinition("name1", "id1", Handler.NOP_HANDLER),
                            new ApplicationMenuItemDefinition("name2", "id2", Handler.NOP_HANDLER)));

        header.init();

        assertThat(header.getContent().getComponentAt(0)).isInstanceOf(ApplicationMenu.class);
        ApplicationMenu leftComponent = (ApplicationMenu)header.getContent().getComponentAt(0);
        assertThat(leftComponent.getItems()).hasSize(2);
    }

    @Test
    void testLeftMenu_Empty() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(), Sequence.empty());

        header.init();

        ApplicationMenu leftComponent = (ApplicationMenu)header.getContent().getComponentAt(0);
        assertThat(leftComponent.getItems()).isEmpty();
    }

    @Test
    void testRightMenu() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(), Sequence.empty());

        header.init();

        MenuBar rightComponent = (MenuBar)((HorizontalLayout)header.getContent().getComponentAt(1)).getComponentAt(0);
        assertThat(rightComponent.getId()).hasValue(ApplicationHeader.APPMENU_RIGHT_ID);
    }

    @Test
    void testRightMenu_HelpMenu() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(), Sequence.empty());

        header.init();

        MenuBar rightComponent = (MenuBar)((HorizontalLayout)header.getContent().getComponentAt(1)).getComponentAt(0);
        assertThat(rightComponent.getItems()).hasSize(1);
        MenuItem helpMenuItem = rightComponent.getItems().get(0);
        assertThat(helpMenuItem.getId()).hasValue(ApplicationHeader.APPMENU_HELP_ID);
        assertThat(helpMenuItem.getChildren().findFirst().get().getElement().getAttribute("icon"))
                .isEqualTo(VaadinIcon.QUESTION_CIRCLE.create().getElement().getAttribute("icon"));
    }

    @Test
    void testRightMenu_ApplicationInfoItem() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(), Sequence.empty());

        header.init();

        MenuBar rightComponent = (MenuBar)((HorizontalLayout)header.getContent().getComponentAt(1)).getComponentAt(0);
        MenuItem helpMenuItem = rightComponent.getItems().get(0);
        assertThat(helpMenuItem.getSubMenu().getItems()).hasSize(1);
        MenuItem applicationInfoItem = helpMenuItem.getSubMenu().getItems().get(0);
        assertThat(applicationInfoItem.getId()).hasValue(ApplicationHeader.APPMENU_INFO_ID);
    }

    @Test
    void testAddThemeVariantToggles_SingleItem() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(), Sequence.empty());

        header.init();

        MenuBar rightComponent = (MenuBar)((HorizontalLayout)header.getContent().getComponentAt(1)).getComponentAt(0);
        MenuItem helpMenuItem = rightComponent.getItems().get(0);
        header.addThemeVariantToggles(helpMenuItem, LINKKI_CARD);

        assertThat(helpMenuItem.getSubMenu().getItems()).anyMatch(mi -> mi.getText().equals("Card Theme"));
    }

    @Test
    void testAddThemeVariantToggles_MultipleItems() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(), Sequence.empty());

        header.init();

        MenuBar rightComponent = (MenuBar)((HorizontalLayout)header.getContent().getComponentAt(1)).getComponentAt(0);
        MenuItem helpMenuItem = rightComponent.getItems().get(0);
        header.addThemeVariantToggles(helpMenuItem, LINKKI_CARD, LINKKI_COMPACT);

        assertThat(helpMenuItem.getSubMenu().getItems()).anyMatch(mi -> mi.getText().equals("Themes"));
        assertThat(helpMenuItem.getSubMenu().getItems().get(1).getText())
                .isEqualTo(NlsText.getString("ApplicationHeader.Theme"));
        assertThat(helpMenuItem.getSubMenu().getItems().get(1).getSubMenu().getItems().stream().map(MenuItem::getText))
                .contains("Card Theme", "Compact Theme");
    }

    @Test
    void testAddThemeVariantToggles_NoItems() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(), Sequence.empty());

        header.init();

        MenuBar rightComponent = (MenuBar)((HorizontalLayout)header.getContent().getComponentAt(1)).getComponentAt(0);
        MenuItem helpMenuItem = rightComponent.getItems().get(0);
        header.addThemeVariantToggles(helpMenuItem);

        assertThat(helpMenuItem.getSubMenu().getItems().stream().map(MenuItem::getText))
                .doesNotContain("Themes", "Card Theme", "Compact Theme");
    }

    @Test
    void testShowApplicationInfo() {
        ApplicationHeader header = new ApplicationHeader(new TestApplicationInfo(), Sequence.empty());

        // Is is technically difficult to check if a dialog is opened...
        Assertions.assertDoesNotThrow(() -> header.showApplicationInfo());
    }

    private static class TestApplicationInfo implements ApplicationInfo {

        @Override
        public String getApplicationName() {
            return "name";
        }

        @Override
        public String getApplicationVersion() {
            return "version";
        }

        @Override
        public String getApplicationDescription() {
            return "description";
        }

        @Override
        public String getCopyright() {
            return "copyright";
        }

    }
}

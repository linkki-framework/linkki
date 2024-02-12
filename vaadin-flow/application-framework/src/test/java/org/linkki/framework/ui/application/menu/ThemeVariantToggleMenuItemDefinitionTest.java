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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;

class ThemeVariantToggleMenuItemDefinitionTest {

    @BeforeEach
    void mockUi() {
        MockVaadin.setup();
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testCreateItem_SubMenu_ReflectsVariantCheckedInitially() {
        MenuItem item = new ApplicationMenu().addItem("item");
        UI.getCurrent().getElement().getThemeList().add("variant");

        new ThemeVariantToggleMenuItemDefinition("name", "id", "variant").createItem(item.getSubMenu());

        assertThat(item.getSubMenu().getItems()).hasSize(1);
        MenuItem createdItem = item.getSubMenu().getItems().get(0);
        assertThat(createdItem.getText()).isEqualTo("name");
        assertThat(createdItem.getId()).hasValue("id");
        assertThat(createdItem.isCheckable()).isTrue();
        assertThat(createdItem.isChecked()).isTrue();
    }

    @Test
    void testCreateItem_ApplicationMenu() {
        ApplicationMenu menu = new ApplicationMenu();
        UI.getCurrent().getElement().getThemeList().add("variant");

        new ThemeVariantToggleMenuItemDefinition("name", "id", "variant").createItem(menu);

        assertThat(menu.getItems()).hasSize(1);
        MenuItem createdItem = menu.getItems().get(0);
        assertThat(createdItem.getText()).isEqualTo("name");
        assertThat(createdItem.getId()).hasValue("id");
    }

    @Test
    void testToggleTheme() {
        MenuItem item = new ApplicationMenu().addItem("").getSubMenu().addItem("item");
        item.setCheckable(true);
        assertThat(UI.getCurrent().getElement().getThemeList()).doesNotContain("variant");
        assertThat(item.isChecked()).isFalse();
        ThemeVariantToggleMenuItemDefinition itemDefinition = new ThemeVariantToggleMenuItemDefinition("whatever",
                "whatever", "variant");

        itemDefinition.toggleTheme(item);

        assertThat(item.isChecked()).isTrue();
        assertThat(UI.getCurrent().getElement().getThemeList()).contains("variant");

        itemDefinition.toggleTheme(item);

        assertThat(item.isChecked()).isFalse();
        assertThat(UI.getCurrent().getElement().getThemeList()).doesNotContain("variant");
    }
}

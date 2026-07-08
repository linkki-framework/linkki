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

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;

class SingleItemMenuBarTest {

    @Test
    void testSingleItemMenuBar() {
        var menuBar = new SingleItemMenuBar("caption");

        assertThat(menuBar.getCaption()).isEqualTo("caption");
        assertThat(menuBar.getIcon()).isNull();
        assertThat(menuBar.getItems())
                .map(MenuItem::getText)
                .containsExactly("caption");
        var menuItem = menuBar.getItems().get(0);
        assertThat(menuItem.getChildren()).isEmpty();
    }

    @Test
    void testSingleItemMenuBar_WithIcon() {
        var menuBar = new SingleItemMenuBar("caption", VaadinIcon.AMBULANCE);

        assertThat(menuBar.getIcon()).isEqualTo(VaadinIcon.AMBULANCE);
        assertThat(menuBar.getItems()).map(this::getIconAttribute)
                .containsExactly("vaadin:ambulance");
    }

    @Test
    void testSetCaption() {
        var menuBar = new SingleItemMenuBar("old caption");

        menuBar.setCaption("new caption");

        assertThat(menuBar.getCaption()).isEqualTo("new caption");
        assertThat(menuBar.getItems()).map(MenuItem::getText).containsExactly("new caption");
    }

    @Test
    void testSetIcon() {
        var menuBar = new SingleItemMenuBar("caption", VaadinIcon.NEWSPAPER);

        menuBar.setIcon(VaadinIcon.RSS);

        assertThat(menuBar.getIcon()).isEqualTo(VaadinIcon.RSS);
        assertThat(menuBar.getItems()).map(this::getIconAttribute)
                .containsExactly("vaadin:rss");
    }

    private String getIconAttribute(MenuItem menuItem) {
        var firstChild = menuItem.getChildren().findFirst();
        return firstChild
                .filter(Icon.class::isInstance)
                .map(Icon.class::cast)
                .map(icon -> icon.getElement().getAttribute("icon"))
                .orElse("");
    }

    @Test
    void testSetId() {
        var menuBar = new SingleItemMenuBar("caption");

        menuBar.setId("newId");

        _assertOne(menuBar, SingleItemMenuBar.class, spec -> spec.withId("newId"));
        _assertOne(menuBar, MenuBar.class, spec -> spec.withId("newId"));
        _assertOne(menuBar, MenuItem.class, spec -> spec.withId("newId"));
    }

    @Test
    void testCreateSubMenuItems() {
        var menuBar = new SingleItemMenuBar("caption");

        menuBar.createSubMenuItems(List.of(MenuItemDefinition.builder("id1")
                .icon(VaadinIcon.MENU)
                .enabledIf(false)
                .visibleIf(false)
                .build(), new MenuItemDefinition("id2", null, Handler.NOP_HANDLER)), Handler.NOP_HANDLER);

        var subMenu = menuBar.getItems().get(0).getSubMenu();
        assertThat(subMenu.getItems())
                .map(MenuItem::getId)
                .filteredOn(Optional::isPresent)
                .map(Optional::get)
                .containsExactly("id1", "id2");
        assertThat(subMenu.getItems())
                .map(this::getIconAttribute)
                .containsExactly("vaadin:menu", "");
        assertThat(subMenu.getItems())
                .map(Component::isVisible)
                .containsExactly(false, true);
        assertThat(subMenu.getItems())
                .map(HasEnabled::isEnabled)
                .containsExactly(false, true);
    }

    @Test
    void testCreateSubMenuItems_Command() {
        var menuBar = new SingleItemMenuBar("caption");

        var command1 = spy(new NopHandler());
        var command2 = spy(new NopHandler());
        var modelChanged = spy(new NopHandler());

        menuBar.createSubMenuItems(List.of(new MenuItemDefinition("id1", null, command1),
                                           new MenuItemDefinition("id2", null, command2)),
                                   modelChanged);

        var subMenu = menuBar.getItems().get(0).getSubMenu();
        _click(subMenu.getItems().get(0));
        verify(command1).apply();
        verifyNoInteractions(command2);
        verify(modelChanged).apply();
    }

    @Test
    void testUpdateSubMenuItems() {
        var menuBar = new SingleItemMenuBar("caption");
        var unchangedMenuItem = new MenuItemDefinition("id2", null, Handler.NOP_HANDLER);
        menuBar.createSubMenuItems(List.of(new MenuItemDefinition("id1", null, Handler.NOP_HANDLER),
                                           unchangedMenuItem),
                                   Handler.NOP_HANDLER);
        assertThat(menuBar.getItems().get(0).getSubMenu().getItems()).map(Component::isVisible)
                .containsExactly(true, true);
        assertThat(menuBar.getItems().get(0).getSubMenu().getItems()).map(HasEnabled::isEnabled)
                .containsExactly(true, true);

        menuBar.updateSubMenuItems(List.of(
                                           MenuItemDefinition.builder("id1").visibleIf(false).enabledIf(false).build(),
                                           unchangedMenuItem));

        assertThat(menuBar.getItems().get(0).getSubMenu().getItems()).map(Component::isVisible)
                .containsExactly(false, true);
        assertThat(menuBar.getItems().get(0).getSubMenu().getItems()).map(HasEnabled::isEnabled)
                .containsExactly(false, true);
    }

    @Test
    void testCreateSubMenuItems_WithNestedSubmenu() {
        var menuBar = new SingleItemMenuBar("caption");
        menuBar.setId("root");

        var child1 = MenuItemDefinition.builder("child1").caption("Child 1")
                .command(() -> {
                }).build();
        var child2 = MenuItemDefinition.builder("child2").caption("Child 2")
                .command(() -> {
                }).build();
        var parent = MenuItemDefinition.builder("parent").caption("Parent")
                .subItems(List.of(child1, child2))
                .build();

        menuBar.createSubMenuItems(List.of(parent), Handler.NOP_HANDLER);

        var topLevelItems = menuBar.getItems().get(0).getSubMenu().getItems();
        assertThat(topLevelItems).hasSize(1);
        assertThat(topLevelItems.get(0).getId()).hasValue("root-parent");

        var nestedItems = topLevelItems.get(0).getSubMenu().getItems();
        assertThat(nestedItems).hasSize(2);
        assertThat(nestedItems.get(0).getId()).hasValue("root-parent-child1");
        assertThat(nestedItems.get(1).getId()).hasValue("root-parent-child2");
        assertThat(nestedItems.get(0).getText()).isEqualTo("Child 1");
        assertThat(nestedItems.get(1).getText()).isEqualTo("Child 2");
    }

    @Test
    void testUpdateSubMenuItems_WithNestedSubmenu() {
        var menuBar = new SingleItemMenuBar("caption");
        menuBar.setId("root");

        var child = MenuItemDefinition.builder("child").caption("Child").build();
        var parent = MenuItemDefinition.builder("parent").caption("Parent")
                .subItems(List.of(child))
                .build();
        menuBar.createSubMenuItems(List.of(parent), Handler.NOP_HANDLER);

        var updatedChild = MenuItemDefinition.builder("child").visibleIf(false).enabledIf(false).build();
        var updatedParent = MenuItemDefinition.builder("parent").caption("Parent")
                .visibleIf(false)
                .subItems(List.of(updatedChild))
                .build();
        menuBar.updateSubMenuItems(List.of(updatedParent));

        var topItem = menuBar.getItems().get(0).getSubMenu().getItems().get(0);
        assertThat(topItem.isVisible()).isFalse();

        var nestedItem = topItem.getSubMenu().getItems().get(0);
        assertThat(nestedItem.isVisible()).isFalse();
        assertThat(nestedItem.isEnabled()).isFalse();
    }

    @Test
    void testSubMenuItemId_Nested() {
        var menuBar = new SingleItemMenuBar("caption");
        menuBar.setId("bar");

        var grandchild = MenuItemDefinition.builder("gc").caption("GC").build();
        var child = MenuItemDefinition.builder("ch").caption("CH").subItems(List.of(grandchild)).build();
        var parent = MenuItemDefinition.builder("p").caption("P").subItems(List.of(child)).build();
        menuBar.createSubMenuItems(List.of(parent), Handler.NOP_HANDLER);

        var level1 = menuBar.getItems().get(0).getSubMenu().getItems().get(0);
        var level2 = level1.getSubMenu().getItems().get(0);
        var level3 = level2.getSubMenu().getItems().get(0);

        assertThat(level1.getId()).hasValue("bar-p");
        assertThat(level2.getId()).hasValue("bar-p-ch");
        assertThat(level3.getId()).hasValue("bar-p-ch-gc");
    }

    private static class NopHandler implements Handler {

        @Override
        public void apply() {
            // nop
        }
    }
}

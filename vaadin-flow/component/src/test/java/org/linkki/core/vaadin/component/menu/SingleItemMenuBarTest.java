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

    private static class NopHandler implements Handler {

        @Override
        public void apply() {
            // nop
        }
    }
}

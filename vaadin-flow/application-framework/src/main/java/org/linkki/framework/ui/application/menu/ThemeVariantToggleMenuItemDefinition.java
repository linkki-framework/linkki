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

import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Menu Item to toggle theme variant globally.
 */
public class ThemeVariantToggleMenuItemDefinition extends ApplicationMenuItemDefinition {

    public static final ThemeVariantToggleMenuItemDefinition LUMO_DARK = new ThemeVariantToggleMenuItemDefinition(
            "Dark Theme", "appmenu-theme-dark", Lumo.DARK);
    public static final ThemeVariantToggleMenuItemDefinition LINKKI_CARD = new ThemeVariantToggleMenuItemDefinition(
            "Card Theme", "appmenu-theme-card", LinkkiTheme.VARIANT_CARD_SECTION_PAGES);
    public static final ThemeVariantToggleMenuItemDefinition LINKKI_COMPACT = new ThemeVariantToggleMenuItemDefinition(
            "Compact Theme", "appmenu-theme-compact", LinkkiTheme.VARIANT_COMPACT);

    private final String variantName;

    public ThemeVariantToggleMenuItemDefinition(String name, String id, String variantName) {
        super(name, id, Handler.NOP_HANDLER);
        this.variantName = variantName;
    }

    @Override
    public MenuItem createItem(SubMenu subMenu) {
        MenuItem createdItem = super.createItem(subMenu);
        createdItem.setCheckable(true);
        createdItem.setChecked(UI.getCurrent().getElement().getThemeList().contains(variantName));
        return toggleThemeOnClick(createdItem);
    }

    @Override
    public MenuItem createItem(ApplicationMenu menuBar) {
        return toggleThemeOnClick(super.createItem(menuBar));
    }

    private MenuItem toggleThemeOnClick(MenuItem createdItem) {
        createdItem.addClickListener(event -> toggleTheme(event.getSource()));
        return createdItem;
    }

    /* private */ void toggleTheme(MenuItem createdItem) {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        if (themeList.contains(variantName)) {
            themeList.remove(variantName);
        } else {
            themeList.add(variantName);
        }
        if (createdItem.isCheckable()) {
            createdItem.setChecked(UI.getCurrent().getElement().getThemeList().contains(variantName));
        }
    }
}

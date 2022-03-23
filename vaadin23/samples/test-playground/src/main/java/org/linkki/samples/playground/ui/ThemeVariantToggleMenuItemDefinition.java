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

package org.linkki.samples.playground.ui;

import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.dom.ThemeList;

/**
 * Menu Item to toggle theme variant globally.
 */
public class ThemeVariantToggleMenuItemDefinition extends ApplicationMenuItemDefinition {

    private final String variantName;

    public ThemeVariantToggleMenuItemDefinition(String name, String id, String variantName) {
        super(name, id, Handler.NOP_HANDLER);
        this.variantName = variantName;
    }

    @Override
    public MenuItem createItem(SubMenu subMenu) {
        return toggleTheme(super.createItem(subMenu));
    }

    @Override
    public MenuItem createItem(ApplicationMenu menuBar) {
        return toggleTheme(super.createItem(menuBar));
    }

    private MenuItem toggleTheme(MenuItem createdItem) {
        createdItem.setCheckable(true);
        createdItem.setChecked(UI.getCurrent().getElement().getThemeList().contains(variantName));
        createdItem.addClickListener(event -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            if (themeList.contains(variantName)) {
                themeList.remove(variantName);
            } else {
                themeList.add(variantName);
            }
            createdItem.setChecked(UI.getCurrent().getElement().getThemeList().contains(variantName));
        });
        return createdItem;
    }
}

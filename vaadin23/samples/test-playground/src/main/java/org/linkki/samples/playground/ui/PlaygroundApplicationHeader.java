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

import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.theme.lumo.Lumo;

public class PlaygroundApplicationHeader extends ApplicationHeader {

    private static final long serialVersionUID = 1L;

    public PlaygroundApplicationHeader(ApplicationInfo applicationInfo,
            Sequence<ApplicationMenuItemDefinition> menuItemDefinitions) {
        super(applicationInfo, menuItemDefinitions);
    }

    @Override
    protected void addHelpMenuItems(MenuItem helpMenu) {
        super.addHelpMenuItems(helpMenu);
        new ApplicationMenuItemDefinition("Locale", "appmenu-locale",
                () -> new PmoBasedDialogFactory()
                        .newOkDialog("Browser Locale", new LocaleInfoPmo()).open()).createItem(helpMenu.getSubMenu());
        new ThemeVariantToggleMenuItemDefinition("Dark theme", "appmenu-theme-dark", Lumo.DARK)
                .createItem(helpMenu.getSubMenu());
        new ThemeVariantToggleMenuItemDefinition("Card theme", "appmenu-theme-card",
                LinkkiTheme.VARIANT_CARD_SECTION_PAGES)
                        .createItem(helpMenu.getSubMenu());
        new ThemeVariantToggleMenuItemDefinition("Compact theme", "appmenu-theme-compact", "compact")
                .createItem(helpMenu.getSubMenu());
    }
}

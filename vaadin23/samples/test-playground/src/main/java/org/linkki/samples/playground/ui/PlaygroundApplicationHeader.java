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

import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.application.menu.ThemeVariantToggleMenuItemDefinition;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;

public class PlaygroundApplicationHeader extends ApplicationHeader {

    private static final long serialVersionUID = 1L;

    public PlaygroundApplicationHeader(ApplicationInfo applicationInfo,
            Sequence<ApplicationMenuItemDefinition> menuItemDefinitions) {
        super(applicationInfo, menuItemDefinitions);
    }

    // tag::applicationheader-createRightMenuBar[]
    @Override
    protected MenuBar createRightMenuBar() {
        MenuBar rightMenuBar = super.createRightMenuBar();
        MenuItem settings = rightMenuBar.addItem(VaadinIcon.COG.create());
        settings.setId("appmenu-settings");
        addThemeVariantToggles(settings, ThemeVariantToggleMenuItemDefinition.LUMO_DARK,
                               ThemeVariantToggleMenuItemDefinition.LINKKI_CARD,
                               ThemeVariantToggleMenuItemDefinition.LINKKI_COMPACT);
        // end::applicationheader-createRightMenuBar[]
        new ApplicationMenuItemDefinition("Locale", "appmenu-locale",
                () -> new PmoBasedDialogFactory()
                        .newOkDialog("Browser Locale", new LocaleInfoPmo()).open())
                                .createItem(settings.getSubMenu());
        return rightMenuBar;
    }

}

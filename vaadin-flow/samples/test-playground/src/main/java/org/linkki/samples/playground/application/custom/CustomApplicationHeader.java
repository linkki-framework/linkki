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

package org.linkki.samples.playground.application.custom;

import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.application.UserAwareApplicationHeader;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.application.menu.ThemeVariantToggleMenuItemDefinition;
import org.linkki.framework.ui.pmo.ApplicationInfoPmo;
import org.linkki.samples.playground.application.custom.CustomApplicationConfig.CustomApplicationInfo;
import org.linkki.samples.playground.nls.PlaygroundNlsText;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * {@link UserAwareApplicationHeader} with an additional help <code>MenuItem</code> as well as a
 * {@link CustomApplicationInfoPmo custom application info dialog}.
 */
public class CustomApplicationHeader extends UserAwareApplicationHeader {

    private static final long serialVersionUID = 1L;

    public CustomApplicationHeader(ApplicationInfo applicationInfo,
            Sequence<ApplicationMenuItemDefinition> menuItemDefinitions) {
        super(applicationInfo, menuItemDefinitions);
    }

    @Override
    public String getUsername() {
        return "user";
    }

    @Override
    protected void addUserMenuItems(MenuItem userMenu) {
        addThemeVariantToggles(userMenu,
                               ThemeVariantToggleMenuItemDefinition.LUMO_DARK,
                               ThemeVariantToggleMenuItemDefinition.LINKKI_CARD,
                               ThemeVariantToggleMenuItemDefinition.LINKKI_COMPACT);
        super.addUserMenuItems(userMenu);
    }

    @Override
    protected void addHelpMenuItems(MenuItem helpMenu) {
        new ApplicationMenuItemDefinition(PlaygroundNlsText.getString("ApplicationHeader.Feedback"), "appmenu-custom",
                () -> Notification.show("Thank you for customizing me!"))
                        .createItem(helpMenu.getSubMenu());
        addApplicationInfoMenuItem(helpMenu);
    }

    @Override
    protected ApplicationInfoPmo createApplicationInfoPmo() {
        return new CustomApplicationInfoPmo((CustomApplicationInfo)getApplicationInfo());
    }

    @Override
    protected void addRightComponents(HorizontalLayout parent) {
        System.setProperty(PROPERTY_APPLICATION_ENVIRONMENT, "Custom Environment");
        super.addRightComponents(parent);
    }
}
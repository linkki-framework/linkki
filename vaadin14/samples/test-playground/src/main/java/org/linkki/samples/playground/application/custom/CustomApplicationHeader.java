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

package org.linkki.samples.playground.application.custom;

import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.pmo.ApplicationInfoPmo;
import org.linkki.samples.playground.nls.NlsText;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

/**
 * {@link ApplicationHeader} with an additional help <code>MenuItem</code> as well as a
 * {@link CustomApplicationInfoPmo custom application info dialog}.
 */
public class CustomApplicationHeader extends ApplicationHeader {

    private static final long serialVersionUID = 1L;

    public CustomApplicationHeader(ApplicationMenu applicationMenu, CustomApplicationConfig config) {
        super(applicationMenu, config);
    }

    @Override
    protected void addHelpMenuItems(MenuItem helpMenu) {
        helpMenu.getSubMenu()
                .addItem(new Button(NlsText.getString("ApplicationHeader.Feedback"), VaadinIcon.COMMENT.create()), //$NON-NLS-1$
                         i -> Notification.show("Thank you for customizing me!"));
        addApplicationInfoMenuItem(helpMenu);
    }

    @Override
    protected ApplicationInfoPmo createApplicationInfoPmo() {
        return new CustomApplicationInfoPmo((CustomApplicationConfig)getApplicationConfig());
    }

}
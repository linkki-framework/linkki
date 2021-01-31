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
package org.linkki.samples.playground.state;

import java.time.LocalDate;
import java.util.Optional;

import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationFooter;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.samples.playground.nls.NlsText;
import org.linkki.samples.playground.ui.PlaygroundApplicationUI;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.router.RouterLink;

/**
 * An {@link ApplicationConfig} using the default {@link ApplicationHeader application header} and
 * {@link ApplicationFooter application footer}.
 */
public class PlaygroundApplicationConfig implements ApplicationConfig {

    @Override
    public String getApplicationName() {
        return NlsText.getString("PlaygroundApplicationConfig.Name");
    }

    @Override
    public String getApplicationVersion() {
        return NlsText.getString("PlaygroundApplicationConfig.Version");
    }

    @Override
    public String getApplicationDescription() {
        return NlsText.getString("PlaygroundApplicationConfig.Description");
    }

    @Override
    public String getCopyright() {
        return "Copyright © 2020 - " + LocalDate.now().getYear() + " Faktor Zehn GmbH"; //$NON-NLS-1$
    }

    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.of(new ApplicationMenuItemDefinition("Start", 0) {

            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                MenuItem playgroundItem = menu.addItem("Playground");
                playgroundItem.getSubMenu()
                        .addItem(new RouterLink("writable", PlaygroundApplicationUI.class));

                playgroundItem.getSubMenu()
                        .addItem(new RouterLink("read-only", PlaygroundApplicationUI.class,
                                PlaygroundApplicationUI.PARAM_READONLY));
                return playgroundItem;
            }
        }, new ApplicationMenuItemDefinition("Dialogs", 1) {

            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                // TODO LIN-2226 DialogView from V8 sample
                return menu.addItem(new RouterLink("Dialogs", PlaygroundApplicationUI.class));
            }
        });
    }

    @Override
    public Optional<ApplicationFooterDefinition> getFooterDefinition() {
        return Optional.of(ApplicationFooter::new);
    }

}

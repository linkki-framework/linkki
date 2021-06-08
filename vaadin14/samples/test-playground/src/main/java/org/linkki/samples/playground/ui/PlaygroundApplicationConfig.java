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

import java.time.LocalDate;
import java.util.Optional;

import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationFooter;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.samples.playground.application.SampleView;
import org.linkki.samples.playground.application.custom.CustomView;
import org.linkki.samples.playground.binding.BindingSampleView;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.nls.PlaygroundNlsText;
import org.linkki.samples.playground.products.ProductsSampleView;
import org.linkki.samples.playground.ui.dialogs.DialogsLayout;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;

/**
 * An {@link ApplicationConfig} using the default {@link ApplicationHeader application header} and
 * {@link ApplicationFooter application footer}.
 */
public class PlaygroundApplicationConfig implements ApplicationConfig {

    @Override
    public String getApplicationName() {
        return PlaygroundNlsText.getString("PlaygroundApplicationConfig.Name");
    }

    @Override
    public String getApplicationVersion() {
        return PlaygroundNlsText.getString("PlaygroundApplicationConfig.Version");
    }

    @Override
    public String getApplicationDescription() {
        return PlaygroundNlsText.getString("PlaygroundApplicationConfig.Description");
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
                        .addItem("writable", e -> UI.getCurrent().navigate(PlaygroundApplicationUI.class));

                playgroundItem.getSubMenu()
                        .addItem("read-only", e -> UI.getCurrent().navigate(PlaygroundApplicationUI.class,
                                                                            PlaygroundApplicationUI.PARAM_READONLY));
                return playgroundItem;
            }
        }, new ApplicationMenuItemDefinition("Dialogs", 1) {
            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                return menu.addItem("Dialogs", e -> UI.getCurrent().navigate(DialogsLayout.class));
            }
        }, new ApplicationMenuItemDefinition("Sample Layout", 2) {
            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                return menu.addItem("Sample Layout", e -> UI.getCurrent().navigate(SampleView.class));
            }
        }, new ApplicationMenuItemDefinition("Custom Layout", 3) {
            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                return menu.addItem("Custom Layout", e -> UI.getCurrent().navigate(CustomView.class));
            }
        }, new ApplicationMenuItemDefinition("Binding", 4) {
            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                return menu.addItem("Binding", e -> UI.getCurrent().navigate(BindingSampleView.class));
            }
        }, new ApplicationMenuItemDefinition("F10 Produkte", 5) {
            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                return menu.addItem("F10 Produkt", e -> UI.getCurrent().navigate(ProductsSampleView.class));
            }
        }, new ApplicationMenuItemDefinition(BugCollectionLayout.PAGE_TITLE, 6) {
            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                return menu.addItem(BugCollectionLayout.PAGE_TITLE,
                                    e -> UI.getCurrent().navigate(BugCollectionLayout.class));
            }
        });
    }

    @Override
    public ApplicationHeaderDefinition getHeaderDefinition() {
        return PlaygroundApplicationHeader::new;
    }

    @Override
    public Optional<ApplicationFooterDefinition> getFooterDefinition() {
        return Optional.of(ApplicationFooter::new);
    }

}

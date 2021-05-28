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
package org.linkki.samples.playground.products;

import java.time.LocalDate;

import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationFooter;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.samples.playground.nls.NlsText;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;

/**
 * An {@link ApplicationConfig} using the default {@link ApplicationHeader application header} and
 * {@link ApplicationFooter application footer}.
 */
public class ProductsSampleApplicationConfig implements ApplicationConfig {

    @Override
    public String getApplicationName() {
        return NlsText.getString("ProductsSampleApplicationConfig.Name");
    }

    @Override
    public String getApplicationVersion() {
        return NlsText.getString("ProductsSampleApplicationConfig.Version");
    }

    @Override
    public String getApplicationDescription() {
        return NlsText.getString("ProductsSampleApplicationConfig.Description");
    }

    @Override
    public String getCopyright() {
        return "Copyright © 2020 - " + LocalDate.now().getYear() + " Faktor Zehn GmbH"; //$NON-NLS-1$
    }

    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.of(new ApplicationMenuItemDefinition("Start", 1) {
            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                return menu.addItem("Start", e -> UI.getCurrent().navigate(ProductsSampleView.class));
            }
        }, new ApplicationMenuItemDefinition("Produkt", 2) {
            @Override
            protected MenuItem internalCreateItem(ApplicationMenu menu) {
                return menu.addItem("Produkt", e -> UI.getCurrent().navigate(ProductsSampleDetailView.class));
            }
        });
    }

}

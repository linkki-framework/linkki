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

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.samples.playground.allelements.AllUiElementsPage;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.locale.LocaleInfoPage;
import org.linkki.samples.playground.tablayout.TabLayoutPage;
import org.linkki.samples.playground.table.TablePage;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(Lumo.class)
@Route("")
public class PlaygroundApplicationUI extends Div {

    private static final long serialVersionUID = 1L;

    public PlaygroundApplicationUI() {
        setSizeFull();

        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.VERTICAL);
        tabLayout.addTab("All", "All", new AllUiElementsPage(() -> false));
        tabLayout.addTab("Dynamic", "Dynamic", new DynamicAnnotationsLayout());
        tabLayout.addTab("Bugs", "Bugs", new BugCollectionLayout());
        tabLayout.addTab("Tables", "Tables", new TablePage());
        tabLayout.addTab("Locale", "Locale", new LocaleInfoPage());
        tabLayout.addTab("Tab Layout", "Tab Layout", new TabLayoutPage());

        tabLayout.getTabsComponent().setWidth("110px");
        add(tabLayout);
    }

}

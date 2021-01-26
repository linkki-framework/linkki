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
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
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

    public static final String ALL_COMPONENTS_TAB_ID = "all";
    public static final String DYNAMIC_ASPECT_TAB_ID = "dynamic";
    public static final String BUGS_TAB_ID = "bugs";
    public static final String TABLES_TAB_ID = "tables";
    public static final String LOCALE_TAB_ID = "locale";
    public static final String TAB_LAYOUT_TAB_ID = "tab-layout";

    public PlaygroundApplicationUI() {
        setSizeFull();

        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.VERTICAL);
        tabLayout.addTab(new LinkkiTabSheet(ALL_COMPONENTS_TAB_ID, "All", "All UI Components",
                new AllUiElementsPage(() -> false)));
        tabLayout.addTab(new LinkkiTabSheet(DYNAMIC_ASPECT_TAB_ID, "Dynamic", "Dynmaic Aspects",
                new DynamicAnnotationsLayout()));
        tabLayout.addTab(new LinkkiTabSheet(BUGS_TAB_ID, "Bugs", "Bugs", new BugCollectionLayout()));
        tabLayout.addTab(new LinkkiTabSheet(TABLES_TAB_ID, "Tables", "Tables", new TablePage()));
        tabLayout.addTab(new LinkkiTabSheet(LOCALE_TAB_ID, "Locale", "Locale", new LocaleInfoPage()));
        tabLayout.addTab(new LinkkiTabSheet(TAB_LAYOUT_TAB_ID, "Tab Layout", "Tab Layout", new TabLayoutPage()));

        tabLayout.getTabsComponent().setWidth("120px");
        add(tabLayout);
    }

}

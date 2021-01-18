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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.linkki.samples.playground.allelements.AllUiElementsPage;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.locale.LocaleInfoPage;
import org.linkki.samples.playground.table.TablePage;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class PlaygroundView extends Div {

    public static final String PARAM_READONLY = "readonly";
    public static final String PARAM_SHEET = "sheet";

    public static final String NAME = "";

    private static final long serialVersionUID = 1L;

    private final Tabs sidebarTabs;
    private final Map<Tab, Component> sheetMap = new HashMap<>();
    private final Div content = new Div();

    public PlaygroundView() {
        setSizeFull();
        getStyle().set("overflow", "hidden");
        this.sidebarTabs = new Tabs();
        sidebarTabs.setOrientation(Orientation.VERTICAL);
        sidebarTabs.getElement().getStyle().set("float", "left");
        add(sidebarTabs);
        sidebarTabs.addSelectedChangeListener(e -> {
            Optional.ofNullable(sheetMap.get(e.getPreviousTab()))
                    .ifPresent(c -> c.setVisible(false));
            Optional.ofNullable(sheetMap.get(e.getSelectedTab()))
                    .ifPresent(c -> c.setVisible(true));
        });
        add(content);
        content.setHeight("100%");
        content.getStyle().set("overflow", "auto");

        createSheets();
    }

    public Tab addSheet(Component c, String caption) {
        Tab tab = new Tab(caption);
        content.add(c);
        c.setVisible(false);
        sheetMap.put(tab, c);
        sidebarTabs.add(tab);
        return tab;
    }

    public void createSheets() {
        addSheet(new AllUiElementsPage(() -> false), "All");
        addSheet(new DynamicAnnotationsLayout(), "Dynamic");
        addSheet(new BugCollectionLayout(), "Bugs");
        addSheet(new TablePage(), "Tables");
        addSheet(new LocaleInfoPage(), "Locale");
    }

}
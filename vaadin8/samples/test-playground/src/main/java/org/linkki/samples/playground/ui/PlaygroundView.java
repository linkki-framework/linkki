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

import org.linkki.framework.ui.component.sidebar.SidebarLayout;
import org.linkki.samples.playground.allelements.AllUiElementsTabsheetArea;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.nestedcomponent.NestedComponentPage;
import org.linkki.samples.playground.table.TablePage;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;

public class PlaygroundView extends SidebarLayout implements View {

    public static final String PARAM_READONLY = "readonly";
    public static final String PARAM_SHEET = "sheet";

    public static final String NAME = "";

    private static final long serialVersionUID = 1L;

    private void addSidebarSheet(SidebarSheetDefinition sidebarSheetDef) {
        addSheet(sidebarSheetDef.createSheet());
    }

    @Override
    public void enter(ViewChangeEvent event) {
        boolean readOnlyMode = event.getParameterMap().containsKey(PARAM_READONLY);
        addSidebarSheet(new AllUiElementsTabsheetArea(readOnlyMode));
        addSidebarSheet(new DynamicAnnotationsLayout());
        addSidebarSheet(new BugCollectionLayout());
        addSidebarSheet(new TablePage());
        addSidebarSheet(new NestedComponentPage());

        select(event.getParameterMap().get(PARAM_SHEET));

        addSelectionListener(e -> Page.getCurrent()
                .setUriFragment("!" + NAME + "/"
                        + PARAM_SHEET + "=" + e.getSelectedSheet().getId(), false));
    }

}
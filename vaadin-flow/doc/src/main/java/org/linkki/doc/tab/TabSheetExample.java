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

package org.linkki.doc.tab;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

public class TabSheetExample {

    public void addNormalTabSheets() {
        // tag::createTabSheet[]
        LinkkiTabLayout linkkiTabLayout = new LinkkiTabLayout();
        linkkiTabLayout.addTabSheet(LinkkiTabSheet.builder("tabSheetId")
                .caption("Tab Sheet Caption")
                .content(this::createTabContent)
                .build());
        // end::createTabSheet[]
    }

    public void addSidebarLayout() {
        // tag::createSidebar[]
        LinkkiTabLayout linkkiTabLayout = LinkkiTabLayout.newSidebarLayout();
        linkkiTabLayout.addTabSheet(LinkkiTabSheet.builder("sidebarSheetId")
                .caption(VaadinIcon.ABACUS.create())
                .description("Sidebar One")
                .content(this::createSidebarContent)
                .build());
        // end::createSidebar[]
    }

    private Component createTabContent() {
        return new Div();
    }

    private Component createSidebarContent() {
        return new Div();
    }

}

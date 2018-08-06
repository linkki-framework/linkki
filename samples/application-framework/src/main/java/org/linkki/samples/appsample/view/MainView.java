/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.samples.appsample.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.framework.ui.component.Headline;
import org.linkki.framework.ui.component.sidebar.SidebarLayout;
import org.linkki.framework.ui.component.sidebar.SidebarSheet;
import org.linkki.samples.appsample.model.Report;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.VerticalLayout;

public class MainView extends SidebarLayout implements View {

    public static final String NAME = "";

    private static final long serialVersionUID = 1L;

    private static final List<Report> reports = new ArrayList<>();

    @Nullable
    private ReportListPage listPage;

    public MainView() {
        // tag::sidebar-addSheet[]
        addSheets(new SidebarSheet(FontAwesome.STAR_HALF_FULL, "Create Report", createReportLayout()),
                  new SidebarSheet(FontAwesome.FILE_O, "Report List", this::createReportListLayout, this::update));
        // end::sidebar-addSheet[]
    }

    @Override
    public void enter(@SuppressWarnings("null") ViewChangeEvent event) {
        // nothing to do
    }

    private VerticalLayout createReportLayout() {
        VerticalLayout layout = new VerticalLayout();
        // tag::addHeadline-call[]
        layout.addComponent(new Headline("Create Report"));
        // end::addHeadline-call[]
        ReportPage page = new ReportPage(reports::add);
        page.createContent();
        layout.addComponent(page);
        return layout;
    }

    private VerticalLayout createReportListLayout() {
        ReportListPage reportListPage = new ReportListPage(reports);
        reportListPage.createContent();
        listPage = reportListPage;
        return reportListPage;
    }

    private void update() {
        if (listPage != null) {
            listPage.update();
        }
    }

}

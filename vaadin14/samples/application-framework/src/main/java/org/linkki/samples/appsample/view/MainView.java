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

package org.linkki.samples.appsample.view;

import java.util.ArrayList;
import java.util.List;

import org.linkki.framework.ui.component.Headline;
import org.linkki.samples.appsample.model.Report;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@Route("")
public class MainView extends VerticalLayout {

    public static final String NAME = "";

    private static final long serialVersionUID = 1L;

    private static final List<Report> reports = new ArrayList<>();

    @CheckForNull
    private ReportListPage listPage;

    public MainView() {
        add(createReportLayout(), createReportListLayout());
        // TODO LIN-2222
        // addSheets(new SidebarSheet(VaadinIcons.STAR_HALF_LEFT_O, "Create Report",
        // createReportLayout()),
        // new SidebarSheet(VaadinIcons.FILE_O, "Report List", this::createReportListLayout,
        // this::update));
    }

    private VerticalLayout createReportLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);
        layout.add(new Headline("Create Report"));
        ReportPage page = new ReportPage(reports::add);
        page.init();
        layout.add(page);
        return layout;
    }

    private VerticalLayout createReportListLayout() {
        ReportListPage reportListPage = new ReportListPage(reports);
        reportListPage.init();
        listPage = reportListPage;
        return reportListPage;
    }

    private void update() {
        if (listPage != null) {
            listPage.update();
        }
    }

}
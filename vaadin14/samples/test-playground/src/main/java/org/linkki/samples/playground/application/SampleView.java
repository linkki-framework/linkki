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

package org.linkki.samples.playground.application;

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.framework.ui.component.Headline;
import org.linkki.samples.playground.application.model.Report;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.Route;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@Route(value = SampleView.NAME, layout = SampleApplicationLayout.class)
public class SampleView extends LinkkiTabLayout {

    public static final String NAME = "sample-layout";

    private static final long serialVersionUID = 1L;

    private static final List<Report> reports = new ArrayList<>();

    @CheckForNull
    private ReportListPage listPage;

    public SampleView() {
        super(Orientation.VERTICAL);

        // tag::sidebar-addSheet[]
        addTabSheets(LinkkiTabSheet.builder("CreateReport")
                .caption(VaadinIcon.STAR_HALF_LEFT_O.create())
                .content(createReportLayout())
                .build(),
                     LinkkiTabSheet.builder("ReportList")
                             .caption(VaadinIcon.FILE_O.create())
                             .content(createReportListLayout())
                             .onSelectionHandler(this::update)
                             .build());
        // end::sidebar-addSheet[]

    }

    private VerticalLayout createReportLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.add(new Headline("Create Report"));
        ReportPage page = new ReportPage(reports::add);
        page.init();
        layout.add(page);
        return layout;
    }

    private VerticalLayout createReportListLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        listPage = new ReportListPage(reports);
        listPage.setPadding(false);
        listPage.init();
        layout.add(listPage);
        return layout;
    }

    private void update() {
        if (listPage != null) {
            listPage.update();
        }
    }

}
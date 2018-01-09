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

import org.linkki.framework.ui.component.Headline;
import org.linkki.framework.ui.component.sidebar.SidebarLayout;
import org.linkki.framework.ui.component.sidebar.SidebarSheet;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.VerticalLayout;

@CDIView("")
@ViewScoped
public class MainView extends SidebarLayout implements View {

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(ViewChangeEvent event) {
        addSheet(new SidebarSheet(FontAwesome.STAR_HALF_FULL, createReportLayout(), "Create Report"));
    }

    private VerticalLayout createReportLayout() {
        VerticalLayout layout = new VerticalLayout();
        // tag::addHeadline-call[]
        layout.addComponent(new Headline("Create Report"));
        // end::addHeadline-call[]
        ReportPage page = new ReportPage();
        page.createContent();
        layout.addComponent(page);
        return layout;
    }

}

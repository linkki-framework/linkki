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

import java.util.List;

import org.linkki.core.binding.Binder;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.core.vaadin.component.tablayout.AfterTabSelectedObserver;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet.TabSheetSelectionChangeEvent;
import org.linkki.framework.ui.component.Headline;
import org.linkki.samples.playground.application.model.Report;

public class ReportListPage extends AbstractPage implements AfterTabSelectedObserver {

    private static final long serialVersionUID = 1L;

    private static final String ID = "ReportListPage";

    private final BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE,
            PropertyBehaviorProvider.with(PropertyBehavior.readOnly(() -> true)));
    private final List<Report> reports;

    public ReportListPage(List<Report> reports) {
        this.reports = reports;
    }

    @Override
    public void createContent() {
        setPadding(false);
        setId(ID);
        Headline headline = new Headline();
        add(headline);

        HeadlinePmo headlinePmo = new HeadlinePmo(reports);

        // tag::bind-headline[]
        new Binder(headline, headlinePmo).setupBindings(getBindingContext());
        // end::bind-headline[]

        AbstractSection section = addSection(new ReportTablePmo(reports));
        section.setSizeFull();
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }

    @Override
    public void afterTabSelected(TabSheetSelectionChangeEvent event) {
        update();
    }

    public void update() {
        getBindingContext().modelChanged();
    }

    @Override
    public BindingContext getBindingContext() {
        return super.getBindingContext();
    }
}
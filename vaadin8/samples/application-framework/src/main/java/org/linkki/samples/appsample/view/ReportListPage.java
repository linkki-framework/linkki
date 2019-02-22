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

import java.util.List;

import org.linkki.core.binding.Binder;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.BindingManager;
import org.linkki.core.binding.DefaultBindingManager;
import org.linkki.core.binding.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.page.AbstractPage;
import org.linkki.framework.ui.component.Headline;
import org.linkki.samples.appsample.model.Report;
import org.linkki.samples.appsample.pmo.HeadlinePmo;
import org.linkki.samples.appsample.pmo.ReportTablePmo;

public class ReportListPage extends AbstractPage {

    private static final long serialVersionUID = 1L;

    private final BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE,
            PropertyBehaviorProvider.with(PropertyBehavior.readOnly(() -> true)));
    private final List<Report> reports;

    public ReportListPage(List<Report> reports) {
        this.reports = reports;
    }

    @Override
    public void createContent() {
        Headline headline = new Headline();
        addComponent(headline);
        // tag::bind-headline[]
        new Binder(headline, new HeadlinePmo(reports)).setupBindings(getBindingContext());
        // end::bind-headline[]

        addSection(new ReportTablePmo(reports));
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }

    public void update() {
        getBindingContext().modelChanged();
    }

    @Override
    public BindingContext getBindingContext() {
        return super.getBindingContext();
    }

}
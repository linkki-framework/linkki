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
package org.linkki.samples.gettingstarted;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.samples.gettingstarted.model.Report;
import org.linkki.samples.gettingstarted.pmo.ReportSectionPmo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

// tag::ui-class[]
@Route("") // <1>
public class GettingStartedUI extends Div {

    private static final long serialVersionUID = 1L;

    public GettingStartedUI() {
        UI.getCurrent().getPage().setTitle("linkki :: Getting Started");
        // <2>
        Component section = VaadinUiCreator.createComponent(new ReportSectionPmo(new Report()),
                                                            new BindingContext("report-context"));

        add(section);
    }
}
// end::ui-class[]
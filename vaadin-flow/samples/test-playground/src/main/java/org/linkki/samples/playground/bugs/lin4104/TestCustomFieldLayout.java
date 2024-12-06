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
package org.linkki.samples.playground.bugs.lin4104;

import java.io.Serial;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TestCustomFieldLayout extends VerticalLayout {

    public static final String CAPTION = "LIN-4104";

    @Serial
    private static final long serialVersionUID = -3813506898143371549L;

    public TestCustomFieldLayout() {
        var customField = new TestCustomField();
        customField.setId("custom-field-vaadin");
        // this is the critical step
        customField.setRequiredIndicatorVisible(false);
        var valueOfCustomField = new Span("Value in custom field: " + customField.getValue());
        valueOfCustomField.setId("custom-field-vaadin-value");
        customField.addValueChangeListener(e -> valueOfCustomField.setText("Value in custom field: " + e.getValue()));
        var reproductionWithVaadin = new LinkkiSection("Reproduction with Vaadin");
        reproductionWithVaadin.getContentWrapper().add(customField, valueOfCustomField);
        add(reproductionWithVaadin);

        add(VaadinUiCreator.createComponent(new TestCustomFieldPmo(), new BindingContext()));
    }
}

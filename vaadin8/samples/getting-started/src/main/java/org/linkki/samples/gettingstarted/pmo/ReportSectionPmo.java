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
package org.linkki.samples.gettingstarted.pmo;

import static java.util.Objects.requireNonNull;

import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.gettingstarted.model.Report;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Notification;

// tag::report-pmo[]
@UISection
public class ReportSectionPmo implements PresentationModelObject {

    private final Report report;

    public ReportSectionPmo(Report report) {
        this.report = requireNonNull(report, "report must not be null");
    }

    // end::report-pmo[]

    // tag::model-binding[]
    @ModelObject
    public Report getReport() {
        return report;
    }
    // end::model-binding[]

    // tag::textfield[]
    @UITextArea(position = 10, label = "Description", modelAttribute = "description", required = RequiredType.REQUIRED, rows = 5, width = "50em")
    public void description() {
        // Use description from report (model object) directly
    }
    // end::textfield[]

    // tag::combobox[]
    @UIComboBox(position = 20, label = "Type", modelAttribute = "type", required = RequiredType.REQUIRED)
    public void type() {
        // - bind value to the property "type" from report
        // - use enum constants from ReportType as available values
    }
    // end::combobox[]

    // tag::button[]
    @UIButton(position = 30, caption = "Send", icon = VaadinIcons.PAPERPLANE, showIcon = true, enabled = EnabledType.DYNAMIC)
    public void send() {
        report.save();
        Notification.show(
                          String.format("Report with id %d filed!", report.getId()),
                          "Thank you for reporting!",
                          Notification.Type.TRAY_NOTIFICATION);
    }

    /**
     * Enable button only if description and type is present.
     *
     * @return {@code true} if button is enabled otherwise {@code false}
     */
    public boolean isSendEnabled() {
        String description = report.getDescription();
        return description != null && !description.isEmpty()
                && report.getType() != null;
    }
    // end::button[]
}
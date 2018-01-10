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
package org.linkki.samples.appsample.pmo;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextArea;
import org.linkki.samples.appsample.model.Report;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;

@UISection(caption = "Report")
public class ReportSectionPmo implements PresentationModelObject {

    private Report report;

    public ReportSectionPmo() {
        this.report = new Report();
    }

    @ModelObject
    public Report getReport() {
        return report;
    }

    @UITextArea(position = 10, label = "Description", modelAttribute = "description", required = RequiredType.REQUIRED, rows = 2, columns = 50)
    public void description() {
        /* Use description from report (model object) directly */
    }

    @UIComboBox(position = 20, label = "Type", modelAttribute = "type", required = RequiredType.REQUIRED)
    public void type() {
        /*
         * bind value to the property "type" from report and use enum constants from ReportType as
         * available values
         */
    }

    @UIDateField(position = 30, label = "Occurrence date", modelAttribute = "occurrenceDate")
    public void occurenceDate() {
        /* bind valud to pmo property */
    }

    @UIButton(position = 40, caption = "Send", icon = FontAwesome.SEND, showIcon = true, enabled = EnabledType.DYNAMIC)
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
}
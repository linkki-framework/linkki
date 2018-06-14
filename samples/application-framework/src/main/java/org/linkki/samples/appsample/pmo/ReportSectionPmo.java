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

import java.util.function.Consumer;

import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.samples.appsample.model.Report;

import com.vaadin.server.FontAwesome;

@UISection(caption = "Report")
public class ReportSectionPmo extends ReportPmo {

    private final Consumer<Report> addReport;

    public ReportSectionPmo(Consumer<Report> addReport) {
        this(new Report(), addReport);
    }

    public ReportSectionPmo(Report report, Consumer<Report> addReport) {
        super(report);
        this.addReport = addReport;
    }

    @UIButton(position = 40, caption = "Send", icon = FontAwesome.SEND, showIcon = true, enabled = EnabledType.DYNAMIC)
    public void send() {
        getReport().save();
        ConfirmationDialog.open("Thank you!",
                                String.format("Report with id \"%d\" is sent to our team. Thank you for reporting!",
                                              getReport().getId()));
        addReport.accept(getReport());
        newReport();
    }

    /**
     * Enable button only if description and type is present.
     *
     * @return {@code true} if button is enabled otherwise {@code false}
     */
    public boolean isSendEnabled() {
        String description = getReport().getDescription();
        return description != null && !description.isEmpty()
                && getReport().getType() != null;
    }
}
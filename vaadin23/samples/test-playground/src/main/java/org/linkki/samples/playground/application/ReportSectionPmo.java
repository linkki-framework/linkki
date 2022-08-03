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

import java.util.function.Consumer;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.component.MessageUiComponents;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.samples.playground.application.model.Report;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

@UISection(caption = "Report")
@BindTooltip("Create a report")
public class ReportSectionPmo extends ReportPmo {

    private final Consumer<Report> addReport;

    public ReportSectionPmo(Consumer<Report> addReport) {
        this(new Report(), addReport);
    }

    public ReportSectionPmo(Report report, Consumer<Report> addReport) {
        super(report);
        this.addReport = addReport;
    }

    @UIButton(position = 40, caption = "Send", icon = VaadinIcon.PAPERPLANE, showIcon = true,
            enabled = EnabledType.DYNAMIC)
    public void send() {
        MessageList messages = validate();
        if (messages.containsErrorMsg()) {
            ConfirmationDialog
                    .open("Report was not saved",
                          MessageUiComponents.createMessageTable("Invalid date", () -> (messages),
                                                                 new BindingContext()))
                    .setWidth("20em");
        } else {
            getReport().save();
            doSendReport();
        }
    }

    private void doSendReport() {
        addReport.accept(getReport());
        Notification.show("Report successfully sent to our team. Thank you for the feedback!");
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
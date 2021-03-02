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

import java.time.LocalDate;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;
import org.linkki.samples.playground.application.model.Report;
import org.linkki.samples.playground.application.model.ReportType;

import edu.umd.cs.findbugs.annotations.NonNull;

public class ReportPmo {

    protected Report report;

    public ReportPmo(Report report) {
        this.report = report;
    }

    @ModelObject
    public Report getReport() {
        return report;
    }

    protected void newReport() {
        report = new Report();
    }

    @UIComboBox(position = 10, label = "Type", modelAttribute = "type", required = RequiredType.REQUIRED)
    @BindReadOnly(ReadOnlyType.DERIVED)
    public void type() {
        /*
         * bind value to the property "type" from report and use enum constants from ReportType as
         * available values
         */
    }

    @UIDateField(position = 20, label = "Occurrence date", modelAttribute = "occurrenceDate")
    @BindReadOnly(ReadOnlyType.DYNAMIC)
    public void occurenceDate() {
        /* bind value to pmo property */
    }

    public boolean isOccurenceDateReadOnly() {
        return report.getType() != ReportType.BUG;
    }

    @UITableColumn(collapsible = CollapseMode.COLLAPSIBLE)
    @UITextArea(position = 30, label = "Description", modelAttribute = "description", required = RequiredType.REQUIRED, rows = 2, width = "50em")
    public void description() {
        /* Use description from report (model object) directly */
    }

    @NonNull
    public MessageList validate() {
        MessageList messages = new MessageList();
        if (ReportType.BUG == report.getType() && report.getOccurrenceDate() == null) {
            messages.add(Message.builder("Date must not be empty", Severity.ERROR).create());
        } else if (report.getOccurrenceDate() != null && report.getOccurrenceDate().isAfter(LocalDate.now())) {
            messages.add(Message.builder("Date must not be in the future", Severity.ERROR).create());
        }

        if (report.getDescription().length() < 100) {
            messages.add(Message.newWarning("warning.emptyDescription",
                                            "A detailed description would help us better understand your report. Your description \""
                                                    + report.getDescription()
                                                    + "\" contains less than 100 charaters."));
        }

        if (ReportType.BUG == report.getType()) {
            messages.add(Message
                    .newInfo("info.screenshot",
                             "A screenshot always helps us better understand the issue you are encountering."));
        }
        return messages;
    }
}
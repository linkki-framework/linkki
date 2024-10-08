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

package org.linkki.tooling.apt.test;


import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class ReportSectionPmo {

    private final Report report;

    public ReportSectionPmo(Report report) {
        this.report = requireNonNull(report, "report must not be null");
    }

    @ModelObject
    public Report getReport() {
        return report;
    }

    @UITextArea(position = 10, label = "Description", modelAttribute = "description")
    public void description() {
        // model binding
    }

    @UICheckBox(position = 40, caption = "asdf")
    public void type() {
    }

    public boolean isTypeEnabled() {
        String description = report.getDescription();
        return description != null && !description.isEmpty() && report.getType() != null;
    }

    public Class<?> getTypeComponentType() {
        return null;
    }

    public List<ReportType> getTypeAvailableValues() {
        return Arrays.asList(ReportType.values());
    }
}
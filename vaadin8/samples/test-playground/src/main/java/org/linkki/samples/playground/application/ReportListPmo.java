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

import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.samples.playground.application.model.Report;
import org.linkki.samples.playground.application.model.ReportType;

public class ReportListPmo extends ReportPmo {

    public ReportListPmo(Report report) {
        super(report);
    }

    @Override
    @UIComboBox(position = 10, label = "Type", modelAttribute = "type")
    @BindReadOnly(ReadOnlyType.ALWAYS)
    @BindStyleNames()
    public void type() {
        /*
         * bind value to the property "type" from report and use enum constants from ReportType as
         * available values
         */
    }

    public String getTypeStyleNames() {
        if (report.getType() == ReportType.BUG) {
            return "error-error";
        } else if (report.getType() == ReportType.IMPROVEMENT) {
            return "error-warning";
        } else if (report.getType() == ReportType.QUESTION) {
            return "error-info";
        } else {
            return "";
        }
    }

}

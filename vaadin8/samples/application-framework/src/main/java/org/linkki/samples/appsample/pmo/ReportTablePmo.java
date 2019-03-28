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

package org.linkki.samples.appsample.pmo;

import java.util.List;

import org.linkki.core.ui.table.SimpleTablePmo;
import org.linkki.samples.appsample.model.Report;

import edu.umd.cs.findbugs.annotations.NonNull;

public class ReportTablePmo extends SimpleTablePmo<Report, ReportListPmo> {

    public ReportTablePmo(@NonNull List<Report> reports) {
        super(reports);
    }

    @Override
    protected ReportListPmo createRow(Report report) {
        return new ReportListPmo(report);
    }
}

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

import org.linkki.samples.appsample.model.Report;

public class HeadlinePmo {

    private List<Report> reports;

    public HeadlinePmo(List<Report> reports) {
        this.reports = reports;
    }

    public String getHeaderTitle() {
        return "Report List - Existing reports: " + reports.size();
    }

}
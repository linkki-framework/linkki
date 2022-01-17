package org.linkki.tooling.apt.test.suppressedWarnings;

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

import static java.util.Objects.requireNonNull;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.tooling.apt.test.Report;

@UISection
public abstract class UnsuppressedMissingAttributePmo {

    private final Report report;

    public UnsuppressedMissingAttributePmo(Report report) {
        this.report = requireNonNull(report, "report must not be null");
    }

    @ModelObject
    public Report getReport() {
        return report;
    }

    @UITextArea(position = 10, label = "Description", modelAttribute = "not present", height = "5em")
    public void description() {
        // model binding
    }

}
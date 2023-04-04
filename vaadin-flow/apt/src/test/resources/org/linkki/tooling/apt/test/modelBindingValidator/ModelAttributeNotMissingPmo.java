package org.linkki.tooling.apt.test.modelBindingValidator;


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

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.tooling.apt.test.Report;

@UISection
public class ModelAttributeNotMissingPmo {

    @ModelObject(name = "report")
    public Report getReport() {
        return null;
    }

    @UILabel(position = 10, modelObject = "report", modelAttribute = "type")
    public void type() {
        // model binding
    }
}

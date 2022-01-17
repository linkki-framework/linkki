package org.linkki.tooling.apt.test.suppressedWarnings.method;


import org.linkki.core.defaults.ui.aspects.types.EnabledType;

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
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.tooling.apt.test.Report;

@UISection
public class PublicModifier_NonPublicMethod {

    @SuppressWarnings("linkki")
    @ModelObject
    Report getModelObject() {
        return null;
    }

    @SuppressWarnings("linkki")
    @UITextArea(position = 10, label = "description", modelObject = "modelObject", modelAttribute = "description")
    @UITextField(position = 10, label = "description", modelObject = "modelObject", modelAttribute = "description", enabled = EnabledType.DYNAMIC)
    void description() {
        // model binding
    }

    @SuppressWarnings("linkki")
    boolean isDescriptionEnabled() {
        return true;
    }

    @SuppressWarnings("linkki")
    Class<?> getDescriptionComponentType() {
        return UITextField.class;
    }
}
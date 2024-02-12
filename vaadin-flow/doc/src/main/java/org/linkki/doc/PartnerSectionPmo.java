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

package org.linkki.doc;

import java.time.LocalDate;

import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

// tag::pmo[]
@UISection(caption = "Partner")
public class PartnerSectionPmo {

    private Partner partner;

    public PartnerSectionPmo(Partner partner) {
        this.partner = partner;
    }

    @UITextField(position = 1, label = "Name", width = "20em")
    public String getName() {
        return partner.getName();
    }

    public void setName(String newName) {
        partner.setName(newName);
    }

    @UIDateField(position = 2, label = "Date of Birth")
    public LocalDate getDateOfBirth() {
        return partner.getDateOfBirth();
    }

    public void setDateOfBirth(LocalDate newDateOfBirth) {
        partner.setDateOfBirth(newDateOfBirth);
    }
}
// end::pmo[]

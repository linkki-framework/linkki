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
package org.linkki.samples.appsample.pmo.section;

import java.time.LocalDate;

import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.appsample.model.BusinessPartner;
import org.linkki.samples.appsample.model.BusinessPartner.Status;

// tag::partnerdetails[]
@UISection(caption = "Partner Details")
public class PartnerDetailsSectionPmo {

    private final BusinessPartner partner;

    public PartnerDetailsSectionPmo(BusinessPartner partner) {
        this.partner = partner;
    }

    @UITextField(position = 10, label = "Name")
    public String getName() {
        return partner.getName();
    }

    public void setName(String name) {
        partner.setName(name);
    }

    @UIDateField(position = 20, label = "Date of Birth")
    public LocalDate getDateOfBirth() {
        return partner.getDateOfBirth();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        partner.setDateOfBirth(dateOfBirth);
    }

    @UIComboBox(position = 30, label = "Status")
    public Status getStatus() {
        return partner.getStatus();
    }

    public void setStatus(Status status) {
        partner.setStatus(status);
    }

    @UITextArea(position = 40, label = "Note")
    public String getNote() {
        return partner.getNote();
    }

    public void setNote(String note) {
        partner.setNote(note);
    }
    // end::partnerdetails[]
}

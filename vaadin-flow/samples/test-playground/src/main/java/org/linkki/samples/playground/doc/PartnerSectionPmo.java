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

package org.linkki.samples.playground.doc;

import java.time.LocalDate;

import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

/**
 * This class is just used for documentation (screenshots) reasons. Use this class as a section in
 * Playground App to take screenshot for linkki documentation
 */
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

    @UIComboBox(position = 3, label = "Type")
    public PartnerType getPartnerType() {
        return partner.getPartnerType();
    }

    public void setPartnerType(PartnerType partnerType) {
        partner.setPartnerType(partnerType);
    }

    public static class Partner {

        private String name;
        private LocalDate dateOfBirth;
        private PartnerType type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public PartnerType getPartnerType() {
            return type;
        }

        public void setPartnerType(PartnerType type) {
            this.type = type;
        }

    }

    public enum PartnerType {
        NATURAL_PERSON
    }
}

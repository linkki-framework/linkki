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

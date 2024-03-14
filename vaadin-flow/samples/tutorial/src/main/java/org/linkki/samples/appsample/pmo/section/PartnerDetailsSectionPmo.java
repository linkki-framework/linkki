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

package org.linkki.doc.dynamic;

import static org.linkki.core.ui.section.annotations.EnabledType.DYNAMIC;

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.doc.Partner;
import org.linkki.doc.Partner.PartnerType;

@UISection(caption = "Partner")
public class PartnerSectionPmo {

    private Partner partner;

    public PartnerSectionPmo(Partner partner) {
        this.partner = partner;
    }

    @ModelObject
    public Partner getPartner() {
        return partner;
    }

    @UITextField(position = 1, label = "Name", width = "20em", modelAttribute = "name")
    public void name() {
        // just binding
    }

    // tag::dynamic[]
    @UIDateField(position = 2, label = "Date of Birth", modelAttribute = "dateOfBirth", enabled = DYNAMIC)
    public void dateOfBirth() {
        // just binding
    }

    public boolean isDateOfBirthEnabled() {
        return partner.getType() == PartnerType.NATURAL_PERSON;
    }
    // end::dynamic[]

}

package org.linkki.doc.modelbinding;

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.doc.Partner;

// tag::pmo[]
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

    @UIDateField(position = 2, label = "Date of Birth", modelAttribute = "dateOfBirth")
    public void dateOfBirth() {
        // just binding
    }
}
// end::pmo[]

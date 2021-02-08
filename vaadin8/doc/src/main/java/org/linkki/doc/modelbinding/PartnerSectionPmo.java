package org.linkki.doc.modelbinding;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
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

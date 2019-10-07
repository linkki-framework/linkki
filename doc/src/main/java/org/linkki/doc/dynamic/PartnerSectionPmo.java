package org.linkki.doc.dynamic;

import static org.linkki.core.defaults.ui.aspects.types.EnabledType.DYNAMIC;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.doc.Partner;
import org.linkki.doc.Partner.PartnerType;

// tag::aspectOnClass[]
@BindStyleNames
@UISection(caption = "Partner")
public class PartnerSectionPmo {
    // end::aspectOnClass[]

    private Partner partner;

    public PartnerSectionPmo(Partner partner) {
        this.partner = partner;
    }

    @ModelObject
    public Partner getPartner() {
        return partner;
    }

    // tag::aspects[]
    @UITextField(position = 1, label = "Name", enabled = DYNAMIC)
    public String getName() {
        return partner.getName();
    }

    public void setName(String name) {
        partner.setName(name);
    }

    public boolean isNameEnabled() {
        return partner.getType() == PartnerType.NATURAL_PERSON;
    }
    // end::aspects[]

    // tag::dynamic[]
    @UIDateField(position = 2, label = "Date of Birth", modelAttribute = "dateOfBirth", enabled = DYNAMIC)
    public void dateOfBirth() {
        // model binding
    }

    public boolean isDateOfBirthEnabled() {
        return partner.getType() == PartnerType.NATURAL_PERSON;
    }
    // end::dynamic[]

    // tag::aspectOnClass[]

    public List<String> getStyleNames() {
        if (partner.getType() == PartnerType.NATURAL_PERSON) {
            return Arrays.asList("naturalperson");
        } else {
            return Collections.emptyList();
        }
    }
}
// end::aspectOnClass[]

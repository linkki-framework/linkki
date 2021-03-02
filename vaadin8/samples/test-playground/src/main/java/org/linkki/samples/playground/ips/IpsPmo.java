package org.linkki.samples.playground.ips;

import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.ips.decimalfield.UIDecimalField;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection(caption = "Faktor-IPS with linkki")
public class IpsPmo {

    // tag::ModelObject[]
    @ModelObject
    private final IpsModelObject modelObject;
    // end::ModelObject[]

    public IpsPmo(IpsModelObject modelObject) {
        this.modelObject = modelObject;
    }

    // tag::getString[]
    @UITextField(position = 0, modelAttribute = IpsModelObject.PROPERTY_STRING)
    public void getString() {
        // model binding
    }
    // end::getString[]

    @UIDecimalField(position = 10, label = "In Range [0..100/0.5]", required = RequiredType.REQUIRED, modelAttribute = IpsModelObject.PROPERTY_DECIMAL)
    public void decimal() {
        // model binding
    }

}
package org.linkki.doc;

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;

// tag::class[]
@UISection
public class ContractPmo {

    private final Contract contract;


    public ContractPmo(Contract contract) {
        this.contract = contract;
    }


    @ModelObject
    public Person getPolicyHolder() {
        return contract.getPolicyHolder();
    }

    @ModelObject(name = "VP")
    public Person getInsuredPerson() {
        return contract.getInsuredPerson();
    }


    @UITextField(position = 10, label = "Vorname VN", modelAttribute = "firstname")
    public void vornameVersicherungsnehmer() {
    }

    @UITextField(position = 20, label = "Vorname VP", modelAttribute = "firstname", modelObject = "VP")
    public void vornameVersichertePerson() {
    }

}
// end::class[]
